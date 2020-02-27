package uk.gov.hmcts.reform.adoption.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.adoption.services.CoreCaseDataService;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@DisplayName("Start a case")
@WebMvcTest(StartCaseController.class)
@OverrideAutoConfiguration(enabled = true)
class StartCaseControllerTest {

    @MockBean
    private CoreCaseDataService coreCaseDataService;

    @Autowired
    private MockMvc mockMvc;

    private static final String userAuthToken = "Bearer user-xyz";

    @Test
    @DisplayName("User can create a case")
    void userCanCreateCase() throws Exception {
        when(coreCaseDataService.startCase(userAuthToken)).thenReturn(CaseDetails.builder().build());

        mockMvc.perform(post("/start-a-case")
            .header("authorization", userAuthToken))
            .andExpect(status().is2xxSuccessful());
    }
}