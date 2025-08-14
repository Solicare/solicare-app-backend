package com.example.solicare.domain.sms.domain;

import com.example.solicare.domain.sms.application.dto.FcmMessageDTO;
import com.example.solicare.domain.sms.application.dto.FcmSendDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class FcmServiceImpl implements FcmService {

    @Value("${fcm.credentials-classpath}")
    private String firebaseConfigPath; // e.g. firebase/solicare-firebase-admin.json

    @Value("${fcm.project-id}")
    private String projectId; // e.g. solicare-app

    private static final String FCM_API_FMT =
            "https://fcm.googleapis.com/v1/projects/solicare-app/messages:send";

    @Override
    public int sendMessageTo(FcmSendDTO fcmSendDto) throws IOException {
        String message = makeMessage(fcmSendDto);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0,
                new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAccessToken()); // <- set("Authorization", "Bearer ...") 대신 권장

        HttpEntity<String> entity = new HttpEntity<>(message, headers);

        String apiUrl = String.format(FCM_API_FMT, projectId);
        ResponseEntity<String> response =
                restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        System.out.println(response.getStatusCode());

        return response.getStatusCode() == HttpStatus.OK ? 1 : 0;
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                // ✅ 올바른 스코프 (cloud-platform도 되지만 firebase.messaging이 더 명확)
                .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private String makeMessage(FcmSendDTO fcmSendDto) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        FcmMessageDTO fcmMessageDto = FcmMessageDTO.builder()
                .message(FcmMessageDTO.Message.builder()
                        .token(fcmSendDto.getToken())
                        .notification(FcmMessageDTO.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmMessageDto);
    }
}
