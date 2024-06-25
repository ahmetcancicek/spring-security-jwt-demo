# Spring Security JWT Demo

Change folder to generate a Private Key and Public Key:

```bash
cd src/main/resources/certs
```

Generate a Private Key with RSA:

```bash
openssl genpkey -algorithm RSA -out private-key.pem
```

Extract the Public Key from the Private Key

```bash
openssl rsa -pubout -in private-key.pem -out public-key.pem
```