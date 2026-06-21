package com.hotelsongo.notification_service.cosumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hotelsongo.notification_service.model.EmailRequest;

//@FeignClient(
//	    name = "email-service",
//	    url = "http://localhost:8084",
//	    fallback = EmailFallback.class
//	)
//	public interface EmailFeignClient {
//
//	    @PostMapping("/api/email/")
//	    void sendEmail(@RequestBody EmailRequest request);
//	}
@FeignClient(
	    name = "email-service",
	    fallback = EmailFallback.class
	)
	public interface EmailFeignClient {

	    @PostMapping("/api/email/send")
	    void sendEmail(@RequestBody EmailRequest request);
	}