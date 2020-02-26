variable "product" {
  type = "string"
}

variable "raw_product" {
  type    = "string"
  default = "adoption"    // jenkins-library overrides product for PRs and adds e.g. pr-1-adoption
}

variable "component" {
  type = "string"
}

variable "location" {
  default = "UK South"
}

variable "env" {
  type = "string"
}

variable "ilbIp" {}

variable "subscription" {}

variable "common_tags" {
  type = "map"
}

variable "team_contact" {
  default = "#fpla_adoption_tech"
}

variable "tenant_id" {
  description = "(Required) The Azure Active Directory tenant ID that should be used for authenticating requests to the key vault. This is usually sourced from environment variables and not normally required to be specified."
}

variable "jenkins_AAD_objectId" {
  description = "(Required) The Azure AD object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies."
}

variable "idam_api_url" {
  type = "string"
}

variable "managed_identity_object_id" {
  default = ""
}

variable "enable_ase" {
  default = true
}

variable "appinsights_location" {
  type        = "string"
  default     = "West Europe"
  description = "Location for Application Insights"
}

variable "idam_client_id" {
  type = "string"
}

variable "security_enabled" {
  type = "string"
  default = "false"
}

variable "idam_token_issuer_uri" {
  type    = "string"
  default = ""
}

variable "idam_token_jwk_set_uri" {
  type    = "string"
  default = ""
}

