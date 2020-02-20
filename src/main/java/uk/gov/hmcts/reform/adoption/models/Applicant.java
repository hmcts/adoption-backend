package uk.gov.hmcts.reform.adoption.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Applicant {
    private final String name;
}
