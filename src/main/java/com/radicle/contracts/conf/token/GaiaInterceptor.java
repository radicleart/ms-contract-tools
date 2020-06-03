package com.radicle.contracts.conf.token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

@Component
public class GaiaInterceptor implements HandlerInterceptor {

	private static final Logger logger = LogManager.getLogger(GaiaInterceptor.class);
	private static final String DEMO = "demo-";
	private static final String API_KEY = "ApiKey";
	private static final String BLOCKSTACK = "blockstack-";
	private static final String X_FORWARDED_HOST = "X-Forwarded-Host";
	private static final String X_FORWARDED_SERVER = "X-Forwarded-Server";
	private static final String AUTHORIZATION = "Authorization";
	private static final String Identity_Address = "IdentityAddress";
	private static final String ALLOWED_PATHS = " /buy-now /api/loop /lightning/alice/getInfo /api/exchange/rates /bitcoin/getRadPayConfig /bitcoin/getPaymentAddress /trading/taxonomy/fetch /trading/user/contactEmail";
	private static final String ALLOWED_PATH_BTC = "/bitcoin/address";
	private static final String ALLOWED_PATH_LND = "/bitcoin/invoice";
	private static final String ALLOWED_PATH_INV = "/lightning";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			String host = request.getHeader(X_FORWARDED_HOST);
			String server = request.getHeader(X_FORWARDED_SERVER);
			logger.info("Handling: " + handler + " path: " + request.getRequestURI());
			logger.info("Handling: " + handler + " host: " + host);
			logger.info("Handling: " + handler + " server: " + server);
			String apiKey = request.getHeader(API_KEY);
			if (apiKey != null && apiKey.startsWith(DEMO)) {
				return HandlerInterceptor.super.preHandle(request, response, handler);
			}
			
			if (handler instanceof HandlerMethod) {
				String path = request.getRequestURI();
				if (isProtected(request, path)) {
					String address = request.getHeader(Identity_Address);
					String authToken = request.getHeader(AUTHORIZATION);
					authToken = authToken.split(" ")[1]; // stripe out Bearer string before passing along..
					UserTokenAuthentication v1Authentication = UserTokenAuthentication.getInstance(authToken);
					boolean auth = v1Authentication.isAuthenticationValid(address);
					String username = v1Authentication.getUsername();
					if (!auth) {
						throw new Exception("Failed validation of jwt token");
					}
					request.getSession().setAttribute("USERNAME", username);
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
	
	private boolean isProtected(HttpServletRequest request, String path) {
		boolean protectd = false;
		String apiKey = request.getHeader(API_KEY);
		if (apiKey == null || apiKey.startsWith(BLOCKSTACK)) {
			protectd = true;
		}
		if (path.startsWith("/v1/payment") || path.startsWith("/payment/")) {
			protectd = true;
		}
		return protectd;
	}
	
	private boolean isProtected(String path) {
		boolean protectd = path.startsWith("/bitcoin") || path.startsWith("/lightning") || path.startsWith("/payment");
		if (ALLOWED_PATHS.indexOf(path) > -1) {
			protectd = false;
		} else if (path.startsWith(ALLOWED_PATH_BTC) || path.startsWith(ALLOWED_PATH_LND) || path.startsWith(ALLOWED_PATH_INV)) {
			protectd = false;
		}
		return protectd;
	}
}
