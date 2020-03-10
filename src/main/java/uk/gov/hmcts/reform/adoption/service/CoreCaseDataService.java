package uk.gov.hmcts.reform.adoption.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.adoption.request.RequestData;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.SearchResult;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;

import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoreCaseDataService {
    private final CoreCaseDataApi coreCaseDataApi;
    private final AuthTokenGenerator authTokenGenerator;
    private final RequestData requestData;

    static final String JURISDICTION = "ADOPTION";
    static final String CASE_TYPE = "ADOPTION";
    static final String START_CASE_EVENT = "openCase";

    public SearchResult getCases() {
        return coreCaseDataApi.searchCases(
            requestData.authorisation(),
            authTokenGenerator.generate(),
            CASE_TYPE,
            createSearchCriteria()
        );
    }

    private String createSearchCriteria() {
        return String.format("{\"query\": {\"match\" : {\"data.creatorId\" : \"%s\"}}}", requestData.userId());
    }

    public CaseDetails startCase() {
        String authorisation = requestData.authorisation();

        StartEventResponse startEventResponse = coreCaseDataApi.startCase(
            authorisation,
            authTokenGenerator.generate(),
            CASE_TYPE,
            START_CASE_EVENT
        );

        CaseDataContent caseDataContent = CaseDataContent.builder()
            .eventToken(startEventResponse.getToken())
            .event(Event.builder().id(startEventResponse.getEventId()).build())
            .data(Map.of("creatorId", requestData.userId()))
            .build();

        return coreCaseDataApi.submitForCitizen(
            authorisation,
            authTokenGenerator.generate(),
            requestData.userId(),
            JURISDICTION,
            CASE_TYPE,
            false,
            caseDataContent
        );
    }
}
