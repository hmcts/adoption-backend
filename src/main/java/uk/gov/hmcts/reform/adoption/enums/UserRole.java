package uk.gov.hmcts.reform.adoption.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.types.Role;

@AllArgsConstructor
public enum UserRole implements Role {
    CITIZEN("citizen"),
    ADOPTION_CLERK("caseworker-adoption-clerk");

    @Getter
    String role;
}
