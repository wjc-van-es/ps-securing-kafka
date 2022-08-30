COMMON_NAME=$1
ORGANIZATIONAL_UNIT="Community"
ORGANIZATION="Pluralsight"
CITY="Salt Lake City"
STATE="Utah"
COUNTRY="US"

CA_ALIAS="ca-root"
CA_CERT_FILE="ca-cert" 
VALIDITY_DAYS=36500

# Generate Keystore with Private Key
echo "Commencing keystore/$COMMON_NAME.keystore.jks generation..."
keytool -keystore keystore/$COMMON_NAME.keystore.jks -alias $COMMON_NAME -validity $VALIDITY_DAYS -genkey -keyalg RSA \
-dname "CN=$COMMON_NAME, OU=$ORGANIZATIONAL_UNIT, O=$ORGANIZATION, L=$CITY, ST=$STATE, C=$COUNTRY"
wait
echo "Generation of keystore/$COMMON_NAME.keystore.jks completed."

# Generate Certificate Signing Request (CSR) using the newly created KeyStore
echo "Commencing $COMMON_NAME.csr generation..."
keytool -keystore keystore/$COMMON_NAME.keystore.jks -alias $COMMON_NAME -certreq -file $COMMON_NAME.csr
wait
echo "Generation of $COMMON_NAME.csr completed."

# Sign the CSR using the custom CA
echo "Commencing $COMMON_NAME.signed generation by signing $COMMON_NAME.csr with $CA_CERT_FILE..."
openssl x509 -req -CA $CA_CERT_FILE -CAkey ca-key -in $COMMON_NAME.csr -out $COMMON_NAME.signed -days $VALIDITY_DAYS -CAcreateserial
wait
echo "Generation of $COMMON_NAME.signed completed."

# Import ROOT CA certificate into Keystore
echo "Commencing import of $CA_CERT_FILE into keystore/$COMMON_NAME.keystore.jks..."
keytool -keystore keystore/$COMMON_NAME.keystore.jks -alias $CA_ALIAS -importcert -file $CA_CERT_FILE
wait
echo "Import of $CA_CERT_FILE into keystore/$COMMON_NAME.keystore.jks completed."

# Import newly signed certificate into Keystore
echo "Commencing import $COMMON_NAME.signed into keystore/$COMMON_NAME.keystore.jks..."
keytool -keystore keystore/$COMMON_NAME.keystore.jks -alias $COMMON_NAME -importcert -file $COMMON_NAME.signed
wait
echo "Import $COMMON_NAME.signed into keystore/$COMMON_NAME.keystore.jks completed."

# Clean-up 
rm $COMMON_NAME.csr
rm $COMMON_NAME.signed
# This is never created
# rm ca-cert.srl