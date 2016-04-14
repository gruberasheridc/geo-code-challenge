package com.geo.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.geo.dal.Location;

public interface GeoLocationService {
	
	@Transactional(readOnly=true)
	List<Location> getAllDataSets();
	
	@Transactional(readOnly=true)
	Boolean getData(float latitude, float longitude);
	
	@Transactional
	void addData(float latitude, float longitude);
	
	@Transactional(readOnly=true)
	void analyzeLocations();
}
