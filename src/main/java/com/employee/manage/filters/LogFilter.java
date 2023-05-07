package com.employee.manage.filters;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.employee.manage.constants.EmployeeConstants;

import reactor.core.publisher.Mono;

@Component
public class LogFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String userId = exchange.getRequest().getHeaders().getFirst(EmployeeConstants.USER_ID);
		MDC.put(EmployeeConstants.USER_ID, userId);
		String correlationId = UUID.randomUUID().toString();
		MDC.put(EmployeeConstants.CORRELATION_ID, correlationId);
		ServerHttpRequest mutateRequest = exchange.getRequest().mutate()
				.header(EmployeeConstants.CORRELATION_ID, correlationId).build();
		ServerWebExchange mutateServerWebExchange = exchange.mutate().request(mutateRequest).build();
		return chain.filter(mutateServerWebExchange);
	}

}
