package uk.gov.hmcts.reform.adoption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

abstract class BaseTest {
    static final String userAuthToken = "Bearer user-xyz";

    @Autowired
    protected MockMvc mockMvc;
}
