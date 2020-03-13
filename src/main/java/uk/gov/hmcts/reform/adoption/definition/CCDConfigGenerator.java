package uk.gov.hmcts.reform.adoption.definition;

import uk.gov.hmcts.ccd.sdk.types.CCDConfig;
import uk.gov.hmcts.ccd.sdk.types.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.enums.CaseState;
import uk.gov.hmcts.reform.adoption.enums.UserRole;
import uk.gov.hmcts.reform.adoption.model.CaseData;

public class CCDConfigGenerator implements CCDConfig<CaseData, CaseState, UserRole> {

    @Override
    public void configure(ConfigBuilder<CaseData, CaseState, UserRole> builder) {
        builder.caseType("ADOPTION");

        builder.grant(CaseState.OPEN, "CRU", UserRole.CITIZEN);
        builder.grant(CaseState.OPEN, "CRU", UserRole.ADOPTION_CLERK);

        builder.event("openCase")
            .forState(CaseState.OPEN)
            .name("Start application")
            .description("Create a new case")
            .grant("R", UserRole.ADOPTION_CLERK)
            .grant("CRU", UserRole.CITIZEN)
            .fields()
            .optional(CaseData::getCreatorId);
    }
}
