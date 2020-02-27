package uk.gov.hmcts.reform.adoption.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.services.CoreCaseDataService.CASE_TYPE;
import static uk.gov.hmcts.reform.adoption.services.CoreCaseDataService.JURISDICTION;
import static uk.gov.hmcts.reform.adoption.services.CoreCaseDataService.START_CASE_EVENT;

@ExtendWith(MockitoExtension.class)
class CoreCaseDataServiceTest {

    @Mock
    private CoreCaseDataApi coreCaseDataApi;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private IdamClient idamClient;

    private CoreCaseDataService service;

    private static final String userAuthToken = "Bearer user-xyz";
    private static final String userId = randomUUID().toString();
    private static final String serviceAuthToken = "Bearer service-xyz";
    private static final String eventToken = "t-xyz";

    @BeforeEach
    void setup() {
        service = new CoreCaseDataService(coreCaseDataApi, authTokenGenerator, idamClient);

        when(authTokenGenerator.generate()).thenReturn(serviceAuthToken);
        when(idamClient.getUserDetails(userAuthToken)).thenReturn(UserDetails.builder()
            .id(userId)
            .build());
    }

    @Test
    void shouldReturnANewCaseWhenCalled() {
        when(coreCaseDataApi.startCase(userAuthToken, serviceAuthToken, CASE_TYPE, START_CASE_EVENT))
            .thenReturn(StartEventResponse.builder()
                .token(eventToken)
                .eventId(START_CASE_EVENT)
                .build());

        service.startCase(userAuthToken);

        verify(coreCaseDataApi).submitForCitizen(
            userAuthToken,
            serviceAuthToken,
            userId,
            JURISDICTION,
            CASE_TYPE,
            false,
            caseDataContent());
    }

    private CaseDataContent caseDataContent() {
        return CaseDataContent.builder()
            .eventToken(eventToken)
            .event(Event.builder()
                .id(START_CASE_EVENT)
                .build())
            .build();
    }
}