package com.geo.dal;



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
	
	public Location(Float latitude, Float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Id
	@Column(name="latitude")
	private Float latitude;
	
	@Id
	@Column(name="longitude")
	private Float longitude;
	
}
