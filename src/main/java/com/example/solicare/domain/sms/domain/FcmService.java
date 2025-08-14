package com.example.solicare.domain.sms.domain;

import com.example.solicare.domain.sms.application.dto.FcmSendDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface FcmService {

    int sendMessageTo(FcmSendDTO fcmSendDto) throws IOException;

}
