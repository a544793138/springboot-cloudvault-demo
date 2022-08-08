#!/usr/bin/env bash

vaultHome=$1
tls="-ca-path=bash/work/ca/certs/localhost.cert.pem -client-cert=bash/work/ca/certs/client.cert.pem -client-key=bash/work/ca/private/client.decrypted.key.pem"

nohup $vaultHome server -config=config.hcl &

sleep 1

while [[ $($vaultHome status $tls | grep Sealed) == *Error* ]]; do
sleep 1
echo "=> Waiting vault server start..."
done

# keys from vault-cluster-vault-2022-03-12T05_31_07.406Z.json
$vaultHome operator unseal $tls 1qPyM4arkoNjMaxArTZDohROGe21rc5o1RBZD0ZrCGqg
$vaultHome operator unseal $tls cDjTR4aGj9Qm9vfcq5W3b3XHWwgUrGIn9TVy4Pn5XWM0
$vaultHome operator unseal $tls L/LTbqhVAsw5X1Z2w4xUONat6gW13afT0M5EqLC2YWMc

echo
echo "=> Vault started in  $(cat config.hcl | grep api_addr)"