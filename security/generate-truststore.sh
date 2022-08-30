INSTANCE=$1
CA_ALIAS="ca-root"
CA_CERT_FILE="ca-cert" 

#### Generate Truststore and import ROOT CA certificate ####
echo "Creating truststore/$INSTANCE.truststore.jks and importing $CA_CERT_FILE into it ..."
keytool -keystore truststore/$INSTANCE.truststore.jks -import -alias $CA_ALIAS -file $CA_CERT_FILE
