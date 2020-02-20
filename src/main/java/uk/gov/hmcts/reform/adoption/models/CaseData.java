package uk.gov.hmcts.reform.adoption.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class CaseData {
    private final String caseName;
}
