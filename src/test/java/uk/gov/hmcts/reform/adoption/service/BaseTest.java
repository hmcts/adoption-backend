package uk.gov.hmcts.reform.adoption.service;

import static java.util.UUID.randomUUID;

abstract class BaseTest {
    static final String userAuthToken = "Bearer user-xyz";
    static final String userId = randomUUID().toString();
    static final String serviceAuthToken = "Bearer service-xyz";
    static final String eventToken = "t-xyz";
}
