package com.pluralsight.kafka.security.authentication;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class SASLGSSAPIConsumer {

    private static final Logger log = LoggerFactory.getLogger(SASLGSSAPIConsumer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker-1:9291,broker-2:9292,broker-3:9293");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sasl.gssapi.consumer");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        props.put(SaslConfigs.SASL_MECHANISM, "GSSAPI");
        props.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, "kafka");

        // We need to have an absolute path for this property
        //The keystore of the consumer client is needed for mutual TLS where the client is authenticated with its own certificate during TLS handshake
//        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/home/willem/git/ps-securing-kafka/security/keystore/consumer.keystore.jks"); // Replace with the absolute path on your machine
//        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "password");
//        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "password");

        // We need to have an absolute path for this property
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/home/willem/git/ps-securing-kafka/security/truststore/consumer.truststore.jks"); // Replace with the absolute path on your machine
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");

        try(KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            Thread haltedHook = new Thread(consumer::close);
            Runtime.getRuntime().addShutdownHook(haltedHook);

        consumer.subscribe(Collections.singletonList("sasl-gssapi-topic"));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                records.forEach(record -> log.info("Consumed message: " + record.key() + ":" + record.value()));
            }
        } catch (Exception e) {
            log.error("An error occurred during consumption", e);
        }

    }
}
