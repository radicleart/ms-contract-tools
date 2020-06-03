package com.radicle.contracts.api;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.radicle.contracts.service.domain.Asset;

@RestController
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class DeployContractController {

	private static final Logger logger = LogManager.getLogger(DeployContractController.class);

	@MessageMapping("/mynews")
	@SendTo("/topic/news")
	public String processMessageFromClient(@Payload String message, Principal principal) {
		logger.info("Assets: /topic/news - principal: ", principal);
		return principal.getName();
	}

	@GetMapping(value = "/buy-now")
	public String initBuyNow(HttpServletRequest request) {
		// access granted - return valuable resource
		return "okay";
	}

	/**
	 * Called from fe-lsat to initiate payment 402 flow..
	 * @param request
	 * @param purchaseOrder - simulates a real purchase order
	 * @return
	 */
	@PostMapping(value = "/buy-now")
	public Asset buyNow(HttpServletRequest request, @RequestBody Asset purchaseOrder) {
		return purchaseOrder;
	}
}
