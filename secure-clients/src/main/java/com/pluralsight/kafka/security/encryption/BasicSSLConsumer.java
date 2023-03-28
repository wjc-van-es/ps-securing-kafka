package com.pluralsight.kafka.security.encryption;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class BasicSSLConsumer {

    private static final Logger log = LoggerFactory.getLogger(BasicSSLConsumer.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9191,localhost:9192,localhost:9193");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "basic.consumer");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());


        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");

        // We need to have an absolute path for this property
        // Which is only needed for mutual TLS
//        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/home/willem/git/ps-securing-kafka/security/keystore/consumer.keystore.jks"); // Replace with the absolute path on your machine
//        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "password");
//        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "password");

        // We need to have an absolute path for this property
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/home/willem/git/ps-securing-kafka/security/truststore/consumer.truststore.jks"); // Replace with the absolute path on your machine
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        Thread haltedHook = new Thread(consumer::close);
        Runtime.getRuntime().addShutdownHook(haltedHook);

        consumer.subscribe(Collections.singletonList("basic-topic"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            records.forEach(record -> log.info("Consumed message: " + record.key() + ":" + record.value()));
        }
    }
}
