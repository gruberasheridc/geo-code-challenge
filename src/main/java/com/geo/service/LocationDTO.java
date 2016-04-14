package com.geo.service;

import java.math.BigDecimal;

public class LocationDTO {

	private BigDecimal latitude;
	private BigDecimal longitude;
	
	public BigDecimal getLatitude() {
		return latitude;
	}
	
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	
	public BigDecimal getLongitude() {
		return longitude;
	}
	
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "(latitude=" + latitude + ", longitude=" + longitude + ")";
	}
}