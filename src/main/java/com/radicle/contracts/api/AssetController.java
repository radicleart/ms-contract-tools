package com.radicle.contracts.api;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.radicle.contracts.api.model.DigitalCollectible;
import com.radicle.contracts.service.AssetService;
import com.radicle.contracts.service.domain.Asset;

@RestController
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class AssetController {

    private static final Logger logger = LogManager.getLogger(AssetController.class);
	@Autowired private AssetService assetService;

	@GetMapping(value = "/api/server/time")
	public Long servertime() {
		return System.currentTimeMillis();
	}

	@GetMapping(value = "/api/loop/{tokenId}")
	public DigitalCollectible fetchLoop(HttpServletRequest request, @PathVariable Long tokenId) {
		logger.info("Asset requested: " + tokenId);
		Optional<Asset> asset = assetService.findByTokenId(tokenId);
		if (asset.isEmpty()) {
			return null;
		} else {
			DigitalCollectible dc = DigitalCollectible.fromAsset(asset.get());
			return dc;
		}
	}

	@PostMapping(value = "/asset")
	public Asset save(HttpServletRequest request, @RequestBody Asset asset) {
		asset = assetService.save(asset);
		return asset;
	}

	@PutMapping(value = "/asset")
	public Asset update(HttpServletRequest request, @RequestBody Asset asset) {
		asset = assetService.save(asset);
		return asset;
	}

	@GetMapping(value = "/assets/{owner}")
	public List<Asset> getByOwner(HttpServletRequest request, @PathVariable String owner) {
		List<Asset> assets = assetService.findByOwner(owner);
		return assets;
	}

	@GetMapping(value = "/asset/{assetHash}")
	public Optional<Asset> getByHash(HttpServletRequest request, @PathVariable String assetHash) {
		Optional<Asset> asset = assetService.findByAssetHash(assetHash);
		return asset;
	}
}
