package uk.gov.hmcts.reform.adoption;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static java.net.http.HttpResponse.BodyHandlers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class SmokeTest {

    @Test
    public void shouldProveAppIsRunningAndHealthy() throws NoSuchAlgorithmException, KeyManagementException,
        IOException, InterruptedException {
        TrustManager[] trustAllCerts = getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        HostnameVerifier allHostsValid = (hostname, session) -> true;

        // set the  allTrusting verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        HttpClient httpClient = HttpClient.newBuilder()
            .sslContext(sslContext)
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("http://localhost:4550/health"))
            .build();

        HttpResponse<String> httpResponse = httpClient.send(request, BodyHandlers.ofString());

        assertThat(httpResponse.statusCode())
            .isEqualTo(OK.value());

        assertThat(httpResponse.body())
            .contains("UP");
    }

    private TrustManager[] getTrustManagers() {
        return new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                    X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                    X509Certificate[] certs, String authType) {
                }
            }
        };
    }
}
