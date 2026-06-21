package com.hotelsongo.email_service.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.hotelsongo.email_service.model.EmailRequest;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	private static final Logger logger =
            LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender,
                        SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendHtmlEmail(EmailRequest request) {
    	logger.info("sendHtmlEmail called for receipient={}",request.getTo());

        Context context = new Context();
        context.setVariable("name", request.getName());
        context.setVariable("message", request.getMessage());
        context.setVariable("room", request.getRoom());
        context.setVariable("hotelName", request.getHotelName());
        context.setVariable("countryName", request.getCountry());
        context.setVariable("stateName", request.getState());
        context.setVariable("cityName", request.getCity());
        context.setVariable("room", request.getRoom());
        context.setVariable("amount", request.getAmount());
        context.setVariable("status", request.getStatus());

        String html = templateEngine.process("booking-email", context);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true);

            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(html, true); // 👈 HTML enabled

            mailSender.send(mimeMessage);

        } catch (Exception e) {
        	logger.info("sendHtmlEmail exception for exception={}",e.getMessage());

        	System.err.println("error in  sending email service"+e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
