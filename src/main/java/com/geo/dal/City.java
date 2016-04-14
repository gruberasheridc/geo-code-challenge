package com.geo.dal;



import java.math.BigDecimal;

public class City {
	
	private String name;
	private BigDecimal latitude;
	private BigDecimal longitude;
	
	public City(String name, String latitude, String longitude) {
		this.name = name;
		this.latitude = new BigDecimal(latitude);
		this.longitude = new BigDecimal(longitude);
	}

	public String getName() {
		return name;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "City [name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
		
}