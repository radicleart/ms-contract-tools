package com.radicle.contracts.api.model;

import org.springframework.data.annotation.TypeAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@TypeAlias(value = "Payment")
public class Payment {

	private String paymentRequest;
	private String paymentHash;
	private Long invoiceExpiry;

	public Payment(String paymentRequest, String paymentHash) {
		super();
		this.paymentRequest = paymentRequest;
	}

	public Payment() {
		super();
	}
}
