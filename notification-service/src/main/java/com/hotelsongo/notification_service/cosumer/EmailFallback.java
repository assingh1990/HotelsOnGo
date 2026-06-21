package com.hotelsongo.notification_service.cosumer;

import org.springframework.stereotype.Component;

import com.hotelsongo.notification_service.model.EmailRequest;

@Component
public class EmailFallback implements EmailFeignClient {

    @Override
    public void sendEmail(EmailRequest request) {
        System.out.println("⚠️ FALLBACK triggered. Email NOT sent. Reason: service unreachable");
        System.out.println("Email: " + request.getTo());
    }
}
