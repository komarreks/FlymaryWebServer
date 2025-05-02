package main.services;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class SmsService {

    public boolean sendSms(String phoneNumber, String message) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://sms.ru/sms/send?api_id=520800B9-C2BA-E2DB-CC68-D6DD7AA6AF99&to=" + phoneNumber + "&msg=" + message + "&json=1"))
                .build();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
