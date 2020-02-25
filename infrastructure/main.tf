provider "azurerm" {}

locals {
  ase_name = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"

  instance_size = "${var.env == "prod" || var.env == "sprod" || var.env == "aat" ? "I2" : "I1"}"
  capacity      = "${var.env == "prod" || var.env == "sprod" || var.env == "aat" ? 2 : 1}"

  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview") ? "aat" : "saat" : var.env}"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview") ? "core-compute-aat" : "core-compute-saat" : local.ase_name}"

  vault_name = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview") ? "${var.raw_product}-aat" : "${var.raw_product}-saat" : "${var.raw_product}-${var.env}"}"

  # URLs
  IDAM_S2S_AUTH_URL       = "http://rpe-service-auth-provider-${local.local_env}.service.${local.local_ase}.internal"
  DOCUMENT_MANAGEMENT_URL = "http://dm-store-${local.local_env}.service.${local.local_ase}.internal"
  CORE_CASE_DATA_API_URL  = "http://ccd-data-store-api-${local.local_env}.service.${local.local_ase}.internal"
}

resource "azurerm_resource_group" "rg" {
  name     = "${var.product}-${var.component}-${var.env}"
  location = var.location

  tags = var.common_tags
}

resource "azurerm_application_insights" "appinsights" {
  name                = "${var.product}-appinsights-${var.env}"
  location            = var.appinsights_location
  resource_group_name = azurerm_resource_group.rg.name
  application_type    = "Web"

  tags = var.common_tags
}


module "key-vault" {
  source                  = "git@github.com:hmcts/cnp-module-key-vault?ref=master"
  name                    = "${var.product}-${var.env}"
  product                 = var.product
  env                     = var.env
  tenant_id               = var.tenant_id
  object_id               = var.jenkins_AAD_objectId
  resource_group_name     = azurerm_resource_group.rg.name
  product_group_object_id = "78fd709b-45c7-42f1-8411-130434575920"
  common_tags             = var.common_tags

  #aks migration
  managed_identity_object_id = var.managed_identity_object_id
}
