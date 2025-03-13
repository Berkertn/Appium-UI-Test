package org.mobile.utils;

import lombok.Getter;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;

public class HttpClientUtil {

    public static final HttpClient httpClient = createHttpClient();
    @Getter
    private static HttpResponse<String> httpClientResponse = null;
    @Getter
    private static String httpClientResponseBody = null;

    private static HttpClient createHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create HttpClient: " + e.getMessage(), e);
        }
    }

    public static HttpResponse<String> sendGetRequest(String url, String type) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", type)
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            httpClientResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (httpClientResponse.statusCode() < 200 || httpClientResponse.statusCode() >= 300) {
                throw new RuntimeException("Request failed with status: " + httpClientResponse.statusCode());
            }

            httpClientResponseBody = httpClientResponse.body();
            return httpClientResponse;
        } catch (Exception e) {
            throw new RuntimeException("HTTP GET request failed: " + e.getMessage(), e);
        }
    }

    public static HttpResponse<String> sendGetRequest(String url) {
        return sendGetRequest(url, "application/json");
    }
}
