package com.radicle.contracts.service.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

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
@TypeAlias(value = "Asset")
public class Asset {

	@Id private String uuid;
	private long created;
	private long updated;
	private Long tokenId;
	private String paymentId;
	private String owner;
	private String txId;
	private Long transactionIndex;
	private Long logIndex;
	private String artist;
	private String privacy;
	private String itemType;
	private String assetHash;
	private String imageUrl;
	private String name;
	private List<String> keywords;
	private String description;
	private Map<String, String> attributes;
	private String backgroundColor;
	private String animationUrl;
	private String imageData;
	private String externalUrl;
	private String youtubeUrl;

	public Asset() {
		super();
		this.uuid = UUID.randomUUID().toString();
	}

}
