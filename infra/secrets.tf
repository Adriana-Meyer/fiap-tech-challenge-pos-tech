resource "kubernetes_secret" "app" {
  metadata {
    name      = "app-secret"
    namespace = "workshop"
  }

  type = "Opaque"

  data = {
    JWT_SECRET                = var.jwt_secret
    WEBHOOK_TOKEN              = var.webhook_token
    SPRING_DATASOURCE_USERNAME = "workshop"
    SPRING_DATASOURCE_PASSWORD = var.mysql_password
  }

  depends_on = [kubernetes_manifest.namespace]
}

resource "kubernetes_secret" "mysql" {
  metadata {
    name      = "mysql-secret"
    namespace = "workshop"
  }

  type = "Opaque"

  data = {
    MYSQL_ROOT_PASSWORD = var.mysql_root_password
    MYSQL_PASSWORD       = var.mysql_password
  }

  depends_on = [kubernetes_manifest.namespace]
}

resource "kubernetes_secret" "dockerhub" {
  metadata {
    name      = "dockerhub-secret"
    namespace = "workshop"
  }

  type = "kubernetes.io/dockerconfigjson"

  data = {
    ".dockerconfigjson" = jsonencode({
      auths = {
        "https://index.docker.io/v1/" = {
          username = var.dockerhub_username
          password = var.dockerhub_password
          auth     = base64encode("${var.dockerhub_username}:${var.dockerhub_password}")
        }
      }
    })
  }

  depends_on = [kubernetes_manifest.namespace]
}
