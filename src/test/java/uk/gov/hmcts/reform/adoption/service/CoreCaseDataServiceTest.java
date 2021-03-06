package uk.gov.hmcts.reform.adoption.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.adoption.request.RequestData;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.service.CoreCaseDataService.CASE_TYPE;
import static uk.gov.hmcts.reform.adoption.service.CoreCaseDataService.JURISDICTION;
import static uk.gov.hmcts.reform.adoption.service.CoreCaseDataService.START_CASE_EVENT;

@ExtendWith(MockitoExtension.class)
class CoreCaseDataServiceTest extends BaseTest {

    @Mock
    private CoreCaseDataApi coreCaseDataApi;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private RequestData requestData;

    @InjectMocks
    private CoreCaseDataService service;

    @BeforeEach
    void setup() {
        when(authTokenGenerator.generate()).thenReturn(serviceAuthToken);
        when(requestData.authorisation()).thenReturn(userAuthToken);
        when(requestData.userId()).thenReturn(userId);
    }

    @Test
    @DisplayName("Should return a new case")
    void returnNewCase() {
        when(coreCaseDataApi.startCase(userAuthToken, serviceAuthToken, CASE_TYPE, START_CASE_EVENT))
            .thenReturn(StartEventResponse.builder().token(eventToken).eventId(START_CASE_EVENT).build());

        service.startCase();

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
