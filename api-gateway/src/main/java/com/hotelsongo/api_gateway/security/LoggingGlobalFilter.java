package com.hotelsongo.api_gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class LoggingGlobalFilter implements GlobalFilter ,Ordered{

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request=exchange.getRequest();
		System.out.println("URI :"+request.getURI()+" Methode:"+request.getMethod());
		
		return chain.filter(exchange).then(Mono.fromRunnable(()->{
			System.out.println("Response status :"+exchange.getResponse().getStatusCode());
		}));
	}

}
