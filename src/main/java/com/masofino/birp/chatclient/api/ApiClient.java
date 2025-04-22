package com.masofino.birp.chatclient.api;

import com.google.gson.Gson;
import com.masofino.birp.chatclient.config.AppConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ApiClient {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final Gson gson = new Gson();

    private static final String baseUrl = AppConfig.getInstance().getApiUrl();

    /**
     * Get OTP token
     */
    public static CompletableFuture<ApiResult<OtpResponse>> getOtp() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(baseUrl + "session/otp"))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    System.out.println(response.body());
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        OtpResponse otp = gson.fromJson(response.body(), OtpResponse.class);
                        return ApiResult.success(otp, response.statusCode(), response.body());
                    } else {
                        return ApiResult.error(response.statusCode(), response.body());
                    }
                });
    }

    /**
     * Register a new user
     */
    public static CompletableFuture<ApiResult<RegistrationResponse>> register(String otpToken, RegistrationRequest request) {
        String requestBody = gson.toJson(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .uri(URI.create(baseUrl + "register"))
                .header("Content-Type", "application/json")
                .header("X-Otp-Token", otpToken)
                .build();

        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        RegistrationResponse register = gson.fromJson(response.body(), RegistrationResponse.class);
                        return ApiResult.success(register, response.statusCode(), response.body());
                    } else {
                        return ApiResult.error(response.statusCode(), response.body());
                    }
                });
    }
}