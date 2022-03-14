storage "raft" {
  path    = "./vault/data"
  node_id = "node1"
}

listener "tcp" {
  address     = "localhost:8200"
  tls_cert_file = "bash/work/ca/certs/localhost.cert.pem"
  tls_key_file = "bash/work/ca/private/localhost.decrypted.key.pem"
  tls_require_and_verify_client_cert = "true"
  tls_client_ca_file = "bash/work/ca/certs/ca.cert.pem"
  tls_min_version = "tls12"
  tls_max_version = "tls12"
}

api_addr = "https://localhost:8200"
cluster_addr = "https://localhost:8201"
ui = true

disable_mlock = true