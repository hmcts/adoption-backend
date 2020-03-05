data "azurerm_key_vault" "s2s_vault" {
  name                = "s2s-${var.env}"
  resource_group_name = "rpe-service-auth-provider-${var.env}"
}

data "azurerm_key_vault_secret" "source_microservicekey-adoption-backend" {
  name         = "microservicekey-adoption-backend"
  key_vault_id = "${data.azurerm_key_vault.s2s_vault.id}"
}

resource "azurerm_key_vault_secret" "microservicekey-adoption-backend" {
  name         = "microservicekey-adoption-backend"
  value        = "${data.azurerm_key_vault_secret.source_microservicekey-adoption-backend.value}"
  key_vault_id = "${module.key-vault.key_vault_id}"
}
