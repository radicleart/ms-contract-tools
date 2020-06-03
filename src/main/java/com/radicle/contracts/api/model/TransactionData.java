package com.radicle.contracts.api.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class TransactionData implements Serializable {

	private static final long serialVersionUID = -6563152937262785781L;
	private String assetHash;
	private String txId;
	private Long tokenId;

	public TransactionData() {
		super();
	}
}
