package com.masofino.birp.chatclient.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.masofino.birp.chatclient.config.AppConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ApiClient {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .setLenient()
            .setPrettyPrinting()
            .create();

    private static final String baseUrl = AppConfig.getInstance().getApiUrl();

    /**
     * Get OTP token
     */
    public static CompletableFuture<OtpResponse> getOtp() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(baseUrl + "session/otp"))
                .header("Content-Type", "application/json")
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    System.out.println(response.body());
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        return gson.fromJson(response.body(), OtpResponse.class);
                    } else {
                        throw new RuntimeException("API error: " + response.statusCode() + " - " + response.body());
                    }
                });
    }

    /**
     * Register a new user
     */
    public static CompletableFuture<RegistrationResponse> register(String otpToken, RegistrationRequest request) {
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
                        return gson.fromJson(response.body(), RegistrationResponse.class);
                    } else {
                        throw new RuntimeException("API error: " + response.statusCode() + " - " + response.body());
                    }
                });
    }
}