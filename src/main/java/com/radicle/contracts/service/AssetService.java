package com.radicle.contracts.service;

import java.util.List;
import java.util.Optional;

import com.radicle.contracts.service.domain.Asset;


public interface AssetService
{
	public String getPaymentAddress(String paymentId);
	public Asset save(Asset payment);
    public List<Asset> findByOwner(String owner);
    public Optional<Asset> findByAssetHash(String assethash);
	public Optional<Asset> findByTokenId(Long tokenId);
}
