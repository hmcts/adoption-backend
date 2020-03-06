package uk.gov.hmcts.reform.adoption.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.adoption.service.CoreCaseDataService;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

import static org.apache.http.HttpStatus.SC_OK;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("integration-test")
@DisplayName("Start a case")
@WebMvcTest(StartCaseController.class)
class StartCaseControllerTest extends BaseTest {

    @MockBean
    private CoreCaseDataService coreCaseDataService;

    @Test
    @DisplayName("User can create a case")
    void userCanCreateCase() throws Exception {
        when(coreCaseDataService.startCase()).thenReturn(CaseDetails.builder().build());

        mockMvc.perform(post("/case")
            .header("authorization", userAuthToken))
            .andExpect(status().is(SC_OK));
    }
}
