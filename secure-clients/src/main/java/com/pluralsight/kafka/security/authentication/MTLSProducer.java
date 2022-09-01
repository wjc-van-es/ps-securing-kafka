package com.pluralsight.kafka.security.authentication;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.UUID;

public class MTLSProducer {

    private static final Logger log = LoggerFactory.getLogger(MTLSProducer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker-1:9191,broker-2:9192,broker-3:9193");

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");

        // We need to have an absolute path for this property
        // The keystore of the producer client is needed for mutual TLS where the client is authenticated with its own certificate during TLS handshake
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/home/willem/git/ps-securing-kafka/security/keystore/producer.keystore.jks"); // Replace with the absolute path on your machine
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "password");
        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "password");

        // We need to have an absolute path for this property
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/home/willem/git/ps-securing-kafka/security/truststore/producer.truststore.jks"); // Replace with the absolute path on your machine
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");

        try(KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {

            Thread haltedHook = new Thread(producer::close);
            Runtime.getRuntime().addShutdownHook(haltedHook);

            long i = 0;
            while (true) {
                String key = String.valueOf(i);
                String value = UUID.randomUUID().toString();

                ProducerRecord<String, String> producerRecord =
                        new ProducerRecord<>("mtls-topic", key, value);
                producer.send(producerRecord);
                log.info("Message sent: " + key + ":" + value);

                i++;
                Thread.sleep(2000);
            }
        } catch(Exception e){
            log.error("An error occurred during production", e);
        }
    }
}