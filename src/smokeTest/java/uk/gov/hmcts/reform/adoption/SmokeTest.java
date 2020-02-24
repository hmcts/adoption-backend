package uk.gov.hmcts.reform.adoption;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpResponse.BodyHandlers;
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
    public void shouldProveAppIsRunningAndHealthy() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(targetInstance + "/health"))
            .build();

        HttpResponse<String> httpResponse =
            httpClient.send(request, BodyHandlers.ofString());

        assertThat(httpResponse.statusCode())
            .isEqualTo(OK.value());

        assertThat(httpResponse.body())
            .contains("UP");
    }
}
