package com.radicle.contracts.api.model;

public class UserInvoiceModel {

	private Long amount;
	private String memo;

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
