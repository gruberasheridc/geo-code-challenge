package com.geo.dal;

import java.io.Serializable;
import java.math.BigDecimal;

public class LocationId implements Serializable {

	private static final long serialVersionUID = 3948807150893616841L;
	
	private BigDecimal latitude;
	private BigDecimal longitude;
	
	LocationId() {
		// Needed for ORM. Keep with limited access as possible.
	}
	
	public LocationId(BigDecimal latitude, BigDecimal longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationId other = (LocationId) obj;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		return true;
	}
	
}
