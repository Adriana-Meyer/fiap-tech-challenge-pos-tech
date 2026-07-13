resource "kubernetes_manifest" "metrics_server" {
  for_each = fileset("${path.module}/metrics-server", "*.yaml")

  manifest = yamldecode(file("${path.module}/metrics-server/${each.value}"))
}
