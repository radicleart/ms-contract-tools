package com.radicle.contracts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.radicle.contracts.service.domain.Asset;

@Service
public class AssetServiceImpl implements AssetService {

	@Autowired private AssetRepository assetRepository;
	@Autowired private MongoTemplate mongoTemplate;
	@Autowired private RestOperations restTemplate;
	@Value("${radicle.lsat.address-server}") String addressServer;

	@Override
	public String getPaymentAddress(String paymentId) {
		HttpHeaders headers = new HttpHeaders();
	    HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = null;
		String url = addressServer + "/" + paymentId;
		response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		return response.getBody();
	}

	@Override
	public Asset save(Asset assetFromJson) {
		Optional<Asset> assetFromDb = assetRepository.findByAssetHash(assetFromJson.getAssetHash());
		Asset asset;
		if (assetFromDb.isPresent()) {
			asset = assetFromDb.get();
			asset.setTxId(assetFromJson.getTxId());
			asset.setTokenId(assetFromJson.getTokenId());
		} else {
			asset = assetFromJson;
		}
		return assetRepository.save(asset);
	}

	@Override
	public List<Asset> findByOwner(String owner) {
		List<Asset> assets = assetRepository.findByOwner(owner);
		return assets;
	}

	@Override
	public Optional<Asset> findByAssetHash(String assethash) {
		Optional<Asset> asset = assetRepository.findByAssetHash(assethash);
		return asset;
	}

	@Override
	public Optional<Asset> findByTokenId(Long tokenId) {
		Optional<Asset> asset = assetRepository.findByTokenId(tokenId);
		return asset;
	}

	/**
	 * Token is incremented whent he user calls the mint contract
	 */
	public Long getNextTokenId(String assetHash) {
		Query q = new Query().with(Sort.by(Sort.Direction.DESC, "tokenId")).limit(1);
		Asset a = mongoTemplate.findOne(q, Asset.class);
		Long tokenId = 0L;
		if (a != null) {
			if (a.getAssetHash().contentEquals(assetHash)) {
				return a.getTokenId();
			} else if (a.getTokenId() == null) {
				tokenId = 1L;
			} else {
				tokenId = a.getTokenId();
				tokenId++;
			}
		}
		return tokenId;
	}


}
