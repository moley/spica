sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout nginx-selfsigned.key -out nginx-selfsigned.crt

sudo security add-trusted-cert -d -r trustRoot -k /Library/Keychains/System.keychain nginx-selfsigned.crt
