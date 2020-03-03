provider "azurerm" {
  version = "=1.41.0"
}

locals {
  instance_size = "${var.env == "prod" || var.env == "sprod" || var.env == "aat" ? "I2" : "I1"}"
  capacity      = "${var.env == "prod" || var.env == "sprod" || var.env == "aat" ? 2 : 1}"
  local_env     = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview") ? "aat" : "saat" : var.env}"
  vault_name    = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview") ? "${var.raw_product}-aat" : "${var.raw_product}-saat" : "${var.raw_product}-${var.env}"}"

  # URLs
  CORE_CASE_DATA_API_URL  = "http://ccd-data-store-api-${local.local_env}.service.${local.local_ase}.internal"
}

resource "azurerm_resource_group" "rg" {
  name     = "${var.product}-${var.component}-${var.env}"
  location = "${var.location}"

  tags     = "${var.common_tags}"
}

data "azurerm_key_vault_secret" "idam_client_secret" {
  name      = "adoption-idam-client-secret"
  vault_uri = "${module.key-vault.key_vault_uri}"
}

data "azurerm_key_vault_secret" "s2s_key" {
  name      = "microservicekey-adoption-backend"
  vault_uri = "https://s2s-${var.env}.vault.azure.net/"
}

resource "azurerm_application_insights" "appinsights" {
  name                = "${var.product}-appinsights-${var.env}"
  location            = "${var.appinsights_location}"
  resource_group_name = "${azurerm_resource_group.rg.name}"
  application_type    = "web"

  tags                = "${var.common_tags}"
}

module "key-vault" {
  source                     = "git@github.com:hmcts/cnp-module-key-vault?ref=master"
  name                       = "${var.product}-${var.env}"
  product                    = "${var.product}"
  env                        = "${var.env}"
  tenant_id                  = "${var.tenant_id}"
  object_id                  = "${var.jenkins_AAD_objectId}"
  resource_group_name        = "${azurerm_resource_group.rg.name}"
  product_group_object_id    = "78fd709b-45c7-42f1-8411-130434575920"
  common_tags                = "${var.common_tags}"

  #aks migration
  managed_identity_object_id = "${var.managed_identity_object_id}"
}

module "backend" {
  source              = "git@github.com:hmcts/cnp-module-webapp?ref=master"
  product             = "${var.product}-${var.component}"
  location            = "${var.location}"
  enable_ase          = "${var.enable_ase}"
  resource_group_name = "${azurerm_resource_group.rg.name}"
  env                 = "${var.env}"
  ilbIp               = "${var.ilbIp}"
  subscription        = "${var.subscription}"
  instance_size       = "${local.instance_size}"
  capacity            = "${local.capacity}"
  common_tags         = "${var.common_tags}"
  java_version        = "11"

  app_settings = {
    IDAM_API_URL                                                = "${var.idam_api_url}"
    IDAM_S2S_AUTH_URL                                           = "${local.IDAM_S2S_AUTH_URL}"
    IDAM_S2S_AUTH_TOTP_SECRET                                   = "${data.azurerm_key_vault_secret.s2s_key.value}"
    IDAM_CLIENT_SECRET                                          = "${data.azurerm_key_vault_secret.idam_client_secret.value}"
    IDAM_CLIENT_ID                                              = "${var.idam_client_id}"
    IDAM_CLIENT_REDIRECT_URI                                    = "${var.idam_client_redirect_uri}"
    SPRING_SECURITY_ENABLED                                     = "${var.security_enabled}"
    SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUERURI         = "${var.idam_token_issuer_uri}"
    SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWKSETURI         = "${var.idam_token_jwk_set_uri}"

    LOGBACK_REQUIRE_ALERT_LEVEL = false
    LOGBACK_REQUIRE_ERROR_CODE  = false
  }
}
