package com.geo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geo.dal.GeoRepository;
import com.geo.dal.Location;
import com.geo.dal.LocationId;

@Service
class GeoLocationServiceImpl implements GeoLocationService {
	
	@Autowired
	GeoRepository geoRepo;

	@Override
	public List<Location> getAllDataSets() {
		List<Location> locations = geoRepo.findAll();
		return locations;
	}

	@Override
	public Boolean getData(float latitude, float longitude) {
		LocationId locationId = new LocationId(latitude, longitude);
		boolean locationExists = geoRepo.exists(locationId);
		return locationExists;
	}

	@Override
	public void addData(float latitude, float longitude) {
		boolean locationExists = getData(latitude, latitude); 
		if (!locationExists) {
			Location location = new Location(latitude, longitude);
			geoRepo.save(location);
		}		
	}

}
