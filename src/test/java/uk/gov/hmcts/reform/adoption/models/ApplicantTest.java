package uk.gov.hmcts.reform.adoption.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicantTest {
    @Test
    void shouldReturnValidApplicant() {
        Applicant applicant = Applicant.builder()
            .name("Test Applicant")
            .build();

        assertThat(applicant).isNotNull();
        assertThat(applicant.getName()).isEqualTo("Test Applicant");
    }
}
