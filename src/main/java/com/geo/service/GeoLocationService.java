package com.geo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.transaction.annotation.Transactional;

import com.geo.dal.Location;

public interface GeoLocationService {
	
	@Transactional(readOnly=true)
	List<Location> getAllDataSets();
	
	@Transactional(readOnly=true)
	Boolean getData(BigDecimal latitude, BigDecimal longitude);
	
	@Transactional
	boolean addData(BigDecimal latitude, BigDecimal longitude);
	
	@Transactional(readOnly=true)
	void analyzeLocations();
	
	@Transactional
	void populateLocations() throws UnexpectedInputException, Exception;
}
