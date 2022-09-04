# our docker default network won´t be m5_default, because the directory our docker compose files are in is called
# ps-securing-kafka, therefore the docker network will be called ps-securing-kafka_default
# hence all m5_default references in this scripts have been replaced by ps-securing-kafka_default

kadmin.local -q 'addprinc -randkey kafka/broker-1.ps-securing-kafka_default@PLURALSIGHT.COM'
kadmin.local -q "ktadd -k /etc/security/keytabs/broker1.keytab kafka/broker-1.ps-securing-kafka_default@PLURALSIGHT.COM"

kadmin.local -q 'addprinc -randkey kafka/broker-2.ps-securing-kafka_default@PLURALSIGHT.COM'
kadmin.local -q "ktadd -k /etc/security/keytabs/broker2.keytab kafka/broker-2.ps-securing-kafka_default@PLURALSIGHT.COM"

kadmin.local -q 'addprinc -randkey kafka/broker-3.ps-securing-kafka_default@PLURALSIGHT.COM'
kadmin.local -q "ktadd -k /etc/security/keytabs/broker3.keytab kafka/broker-3.ps-securing-kafka_default@PLURALSIGHT.COM"

kadmin.local -q 'addprinc -randkey producer@PLURALSIGHT.COM'
kadmin.local -q "ktadd -k /etc/security/keytabs/producer.keytab producer@PLURALSIGHT.COM"

kadmin.local -q 'addprinc -randkey consumer@PLURALSIGHT.COM'
kadmin.local -q "ktadd -k /etc/security/keytabs/consumer.keytab consumer@PLURALSIGHT.COM"
