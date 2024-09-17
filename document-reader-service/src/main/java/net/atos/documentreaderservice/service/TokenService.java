package net.atos.documentreaderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class TokenService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean validateToken(String token) {
        String url = "http://127.0.0.1:8081/validation";

        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, token, Boolean.class);

        return response.getBody() != null && response.getBody();
    }

    public String extractNationalid(String token) {
        String url = "http://127.0.0.1:8081/nationalId";
        System.out.println("Hamadaaa's token:  "+token );

        ResponseEntity<String> response = restTemplate.postForEntity(url, token, String.class);
        System.out.println(response.getBody());

        return response.getBody();
    }
}
