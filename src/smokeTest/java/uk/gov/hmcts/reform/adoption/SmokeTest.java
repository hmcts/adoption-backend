package uk.gov.hmcts.reform.adoption;

import io.restassured.RestAssured;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Test;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class SmokeTest {
    private final String targetInstance =
        defaultIfBlank(
            System.getenv("TEST_URL"),
            "http://localhost:4550"
        );

    @Test
    public void shouldProveAppIsRunningAndHealthy() {
        RestAssured.baseURI = targetInstance;
        RestAssured.useRelaxedHTTPSValidation();

        String response = SerenityRest
            .when()
            .get("/health")
            .then()
            .statusCode(OK.value())
            .and()
            .extract().body().asString();

        assertThat(response).contains("UP");
    }
}
