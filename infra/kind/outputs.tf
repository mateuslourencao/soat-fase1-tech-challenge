output "cluster_name" {
  description = "Nome do cluster Kind criado."
  value       = kind_cluster.oficina.name
}

output "api_url" {
  description = "URL local da API."
  value       = "http://localhost:8081"
}

output "mailhog_url" {
  description = "URL local da interface do MailHog."
  value       = "http://localhost:8025"
}
