package com.radicle.contracts.api.model;

import com.radicle.contracts.service.domain.Asset;

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
public class DigitalCollectible {

	private String name;
	private String description;
	private String image;
	private String background_color;
	private String animation_url;
	private String image_data;
	private String external_url;
	private String youtube_url;

	public DigitalCollectible() {
		super();
	}
	public static DigitalCollectible fromAsset(Asset asset) {
		DigitalCollectible dc = new DigitalCollectible();
		dc.setAnimation_url(asset.getAnimationUrl());
		dc.setBackground_color(asset.getBackgroundColor());
		dc.setDescription(asset.getDescription());
		dc.setExternal_url(asset.getExternalUrl());
		dc.setImage(asset.getImageUrl());
		dc.setImage_data(asset.getImageData());
		dc.setName(asset.getName());
		dc.setYoutube_url(asset.getYoutubeUrl());
		return dc;
	}
}
