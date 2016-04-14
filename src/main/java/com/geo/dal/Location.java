package com.geo.dal;



import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(LocationId.class)
@Table(name = "location")
public class Location {
	
	Location() {
		// Needed for ORM. Keep with limited access as possible.
	}
	
	public Location(BigDecimal latitude, BigDecimal longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Id
	@Column(name="latitude")
	private BigDecimal latitude;
	
	@Id
	@Column(name="longitude")
	private BigDecimal longitude;

	public BigDecimal getLatitude() {
		return latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "(latitude=" + latitude + ", longitude=" + longitude + ")";
	}
		
}
