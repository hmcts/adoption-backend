package uk.gov.hmcts.reform.adoption.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.adoption.request.RequestData;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoreCaseDataService {
    private final CoreCaseDataApi coreCaseDataApi;
    private final AuthTokenGenerator authTokenGenerator;
    private final IdamClient idamClient;
    private final RequestData requestData;

    static final String JURISDICTION = "PUBLICLAW";
    static final String CASE_TYPE = "ADOPTION";
    static final String START_CASE_EVENT = "openCase";

    public CaseDetails startCase() {
        String authorisation = requestData.authorisation();
        UserDetails userDetails = idamClient.getUserDetails(authorisation);

        StartEventResponse startEventResponse = coreCaseDataApi.startCase(
            authorisation,
            authTokenGenerator.generate(),
            CASE_TYPE,
            START_CASE_EVENT
        );

        CaseDataContent caseDataContent = CaseDataContent.builder()
            .eventToken(startEventResponse.getToken())
            .event(Event.builder().id(startEventResponse.getEventId()).build())
            .build();

        return coreCaseDataApi.submitForCitizen(
            authorisation,
            authTokenGenerator.generate(),
            userDetails.getId(),
            JURISDICTION,
            CASE_TYPE,
            false,
            caseDataContent
        );
    }
}
