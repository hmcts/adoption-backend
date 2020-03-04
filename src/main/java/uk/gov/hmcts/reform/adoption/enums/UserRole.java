package uk.gov.hmcts.reform.adoption.enums;

import uk.gov.hmcts.ccd.sdk.types.Role;

public enum UserRole implements Role {
    Citizen("citizen"),
    AdoptionClerk("caseworker-adoption-clerk");

    String role;
    UserRole(String role) {
        this.role = role;
    }

    @Override
    public String getRole() {
        return role;
    }
}
