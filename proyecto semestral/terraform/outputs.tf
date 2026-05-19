output "ec2_public_ip" {
  value = aws_instance.app_server.public_ip
}

output "ecr_backend_ventas" {
  value = aws_ecr_repository.backend_ventas.repository_url
}

output "ecr_backend_despacho" {
  value = aws_ecr_repository.backend_despacho.repository_url
}

output "ecr_frontend" {
  value = aws_ecr_repository.frontend.repository_url
}

output "ssh_private_key_pem" {
  value     = tls_private_key.deployer.private_key_pem
  sensitive = true
}
