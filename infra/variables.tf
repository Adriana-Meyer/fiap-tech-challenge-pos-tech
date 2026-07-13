variable "mysql_root_password" {
  description = "MySQL root user password (MYSQL_ROOT_PASSWORD)."
  type        = string
  sensitive   = true
}

variable "mysql_password" {
  description = "MySQL application user password (MYSQL_PASSWORD and SPRING_DATASOURCE_PASSWORD)."
  type        = string
  sensitive   = true
}

variable "jwt_secret" {
  description = "Key used to sign the JWTs issued by the application."
  type        = string
  sensitive   = true
}

variable "webhook_token" {
  description = "Static token required in the X-Webhook-Token header of the webhook endpoints."
  type        = string
  sensitive   = true
}

variable "dockerhub_username" {
  description = "Docker Hub user that owns the application's private image repository."
  type        = string
}

variable "dockerhub_password" {
  description = "Docker Hub Access Token used to authenticate the private image pull."
  type        = string
  sensitive   = true
}
