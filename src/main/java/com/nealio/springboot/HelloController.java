package com.nealio.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() throws URISyntaxException, IOException, InterruptedException {

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/get"))
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.print(httpResponse.body().toString());

        return "Greetings from Spring Boot!";
    }

}