package uk.gov.hmcts.reform.adoption.config;

import uk.gov.hmcts.ccd.sdk.types.CCDConfig;
import uk.gov.hmcts.ccd.sdk.types.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.enums.State;
import uk.gov.hmcts.reform.adoption.enums.UserRole;
import uk.gov.hmcts.reform.adoption.models.Applicant;
import uk.gov.hmcts.reform.adoption.models.CaseData;

public class CCDConfiguration implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(
        ConfigBuilder<CaseData, State, UserRole> builder) {
        builder.caseType("ADOPTION");
        builder.grant(State.Open, "CRUD", UserRole.Admin);

        builder.event("openCase")
            .forStates(State.Open, State.Closed)
            .grant("CRU", UserRole.Citizen)
            .grant("R", UserRole.AdoptionClerk)
            .fields()
            .mandatory(CaseData::getFoo)
            .complex(CaseData::getApplicant)
                .mandatory(Applicant::getName);
    }
}
