terraform {
  required_version = ">= 1.5"

  required_providers {
    kind = {
      source  = "tehcyx/kind"
      version = "~> 0.7"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.31"
    }
  }
}

provider "kind" {}

provider "kubernetes" {
  config_path    = "~/.kube/config"
  config_context = "kind-${kind_cluster.workshop.name}"
}
