package com.geo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.transaction.annotation.Transactional;

import com.geo.dal.Location;

public interface GeoLocationService {
	
	/**
	 * The method returns all the location records in the DB.
	 * @return all location records.
	 */
	@Transactional(readOnly=true)
	List<Location> getAllDataSets();
	
	/**
	 * The method checks if the given coordinates exist in the DB.
	 * @param latitude the latitude coordinate.
	 * @param longitude the longitude coordinate.
	 * @return true if coordinates exist in the DB. Otherwise false.
	 */
	@Transactional(readOnly=true)
	Boolean getData(BigDecimal latitude, BigDecimal longitude);
	
	/**
	 * The method adds the given coordinate to the DB if it doesn't exist.
	 * @param latitude the latitude coordinate
	 * @param longitude the longitude coordinate.
	 * @return true if location was added to the DB. Otherwise false (even if the record already existed).
	 */
	@Transactional
	boolean addData(BigDecimal latitude, BigDecimal longitude);
	
	/**
	 * The method generates a TSV spreadsheet containing the location's analysis information.
	 * The spreadsheet is written to the disk according to the path specified in the  application properties file.
	 */	
	@Transactional(readOnly=true)
	void doLocationsAnalysis();
	
	/**
	 * The method inserts random locations (for valid latitude, longitude coordinates) into the DB.
	 * The method generates the locations from the public geonames cities DB (http://www.geonames.org/).
	 * The method goes over all city rows in the cities file and randomly selects a city locations to be entered into the DB.
	 * @throws UnexpectedInputException
	 * @throws Exception
	 */
	@Transactional
	void populateLocations() throws UnexpectedInputException, Exception;
}
