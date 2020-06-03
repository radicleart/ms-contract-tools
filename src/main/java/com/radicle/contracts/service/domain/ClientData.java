package com.radicle.contracts.service.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@TypeAlias(value = "ClientData")
public class ClientData implements Serializable {
	private static final long serialVersionUID = -5834939072933725987L;
	private List<String> assets;

	public ClientData() {
		super();
	}

	public boolean addAssets(List<String> assetHashes) {
		if (assets == null) {
			assets = new ArrayList<String>();
		}
		return this.assets.addAll(assetHashes);
	}
}
