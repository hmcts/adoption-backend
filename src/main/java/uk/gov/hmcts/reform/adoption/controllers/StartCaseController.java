package uk.gov.hmcts.reform.adoption.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.adoption.services.CoreCaseDataService;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StartCaseController {
    private final CoreCaseDataService coreCaseDataService;

    @PostMapping("case")
    public CaseDetails startCase() {
        return coreCaseDataService.startCase();
    }
}
