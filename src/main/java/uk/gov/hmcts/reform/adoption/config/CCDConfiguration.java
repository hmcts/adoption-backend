package uk.gov.hmcts.reform.adoption.config;

import uk.gov.hmcts.ccd.sdk.types.CCDConfig;
import uk.gov.hmcts.ccd.sdk.types.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.enums.State;
import uk.gov.hmcts.reform.adoption.enums.UserRole;
import uk.gov.hmcts.reform.adoption.models.CaseData;

public class CCDConfiguration implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(
        ConfigBuilder<CaseData, State, UserRole> builder) {
        builder.caseType("ADOPTION");

        builder.event("openCase")
            .initialState(State.Open)
            .grant("CRU", UserRole.Citizen)
            .grant("R", UserRole.AdoptionClerk)
            .fields()
            .optional(CaseData::getApplicant);
    }
}
