package com.radicle.contracts.conf;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTHandlerInterceptor implements HandlerInterceptor {

	private static final Logger logger = LogManager.getLogger(JWTHandlerInterceptor.class);
	private static final String AUTHORIZATION = "Authorization";
	private static final String Identity_Address = "IdentityAddress";
	@Value("${radicle.lsat.paths}") String lsatPaths;
	@Value("${radicle.lsat.lsat-server}") String lsatRedirect;
	@Value("${radicle.lsat.lsat-verify}") String lsatVerify;
	@Autowired private RestOperations restTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			if (handler instanceof HandlerMethod) {
				String path = request.getRequestURI();
				if (isProtected(path)) {
					String authToken = request.getHeader(AUTHORIZATION);
					if (authToken != null) {
						Boolean allow = verify(authToken);
						if (allow) {
						    return true;
						}
					}
				    response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
		            response.setHeader("Location", response.encodeRedirectURL(lsatRedirect));
		            response.setHeader("Content-Type", "application/json");
		            response.setHeader("Access-Control-Allow-Origin", "*");
				    return false;
				} else {
					logger.info("Authentication not required.");
				}
			} else if (handler instanceof AbstractHandlerMapping) {
				// error occurred..
				logger.info("Error mapping.");
			} else {
				logger.info("Unknown request.");
			}
		} catch (Exception e) {
			throw e;
		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	private boolean isProtected(String path) {
		boolean protectd = false;
		String[] protectedPaths = lsatPaths.split(",");
		for (String subpath : protectedPaths) {
			if (path.indexOf(subpath) > -1) {
				protectd = true;
			}
		}
		return protectd;
	}

	private Boolean verify(String auth) throws JsonProcessingException {
		HttpHeaders headers = getHeaders(auth);
	    HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<?> response = null;
		try {
			response = restTemplate.exchange(lsatVerify, HttpMethod.GET, requestEntity, Boolean.class);
		} catch (RestClientException e) {
			logger.error("Unable to verify token.." + e.getMessage());
		}
		if (response != null && response.getStatusCode() != null) {
			return response.getStatusCode() == HttpStatus.OK;
		}
		return false;
	}
	
	private HttpHeaders getHeaders(String auth) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", auth);
		return headers;
	}

}
