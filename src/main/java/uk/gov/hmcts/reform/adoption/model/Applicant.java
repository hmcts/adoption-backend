package uk.gov.hmcts.reform.adoption.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Applicant {
    private final String name;
}
