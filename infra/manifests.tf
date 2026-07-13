locals {
  k8s_dir = "${path.module}/../k8s"
}

resource "kubernetes_manifest" "namespace" {
  for_each = fileset("${local.k8s_dir}/00-namespace", "*.yaml")

  manifest = yamldecode(file("${local.k8s_dir}/00-namespace/${each.value}"))
}

resource "kubernetes_manifest" "config" {
  for_each = fileset("${local.k8s_dir}/01-config", "*.yaml")

  manifest = yamldecode(file("${local.k8s_dir}/01-config/${each.value}"))

  depends_on = [kubernetes_manifest.namespace]
}

resource "kubernetes_manifest" "mysql" {
  for_each = fileset("${local.k8s_dir}/02-mysql", "*.yaml")

  manifest = yamldecode(file("${local.k8s_dir}/02-mysql/${each.value}"))

  depends_on = [kubernetes_manifest.config, kubernetes_secret.mysql]
}

resource "kubernetes_manifest" "app" {
  for_each = fileset("${local.k8s_dir}/03-app", "*.yaml")

  manifest = yamldecode(file("${local.k8s_dir}/03-app/${each.value}"))

  depends_on = [kubernetes_manifest.mysql, kubernetes_secret.app, kubernetes_secret.dockerhub]
}
