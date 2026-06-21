package com.hotelsongo.email_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotelsongo.email_service.model.EmailRequest;
import com.hotelsongo.email_service.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

	private static final Logger logger =
            LoggerFactory.getLogger(EmailController.class);
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest request) {
    	logger.info("sendEmail called for receipient={}",request.getTo());
        emailService.sendHtmlEmail(request);
        return "HTML Email sent";
    }
}