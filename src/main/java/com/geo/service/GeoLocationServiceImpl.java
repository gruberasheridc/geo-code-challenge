package com.geo.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.geo.dal.GeoRepository;
import com.geo.dal.Location;
import com.geo.dal.LocationId;
import com.jayway.restassured.path.json.JsonPath;

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

	@Override
	public void analyzeLocations() {
		List<Location> locations = getAllDataSets();
		RestTemplate restTemplate = new RestTemplate();
		if (CollectionUtils.isNotEmpty(locations)) {
			for (Location location : locations) {
				String result = restTemplate.getForObject("http://maps.googleapis.com/maps/api/geocode/json?latlng={lat},{long}&sensor=false", 
						String.class, location.getLatitude(), location.getLatitude());
				String country = new JsonPath(result).get("results.address_components.flatten().find { it.types.flatten().contains('country') } ?.short_name");
				System.out.println(country);
			}
		}		
	}

}
