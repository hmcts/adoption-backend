package uk.gov.hmcts.reform.adoption;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static java.net.http.HttpResponse.BodyHandlers;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class SmokeTest {
    private final String targetInstance =
        defaultIfBlank(System.getenv("TEST_URL"), "http://localhost:4550");

    @BeforeEach
    void setup() throws Exception {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

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
