package com.geo.service;

import java.util.Arrays;
import java.util.List;

import com.geo.dal.City;

public class CityConstants {
	
	// City field names used in order to extract cities from the geonames DB.
	public static final String CITY_LATITUDE = "latitude";
	public static final String CITY_LONGITUDE = "longitude";
	public static final String CITY_ASCIINAME = "asciiname";
	
	public static final List<City> COMPARE_CITIES_LIST = Arrays.asList(
			new City("Tokyo", "35.6895", "139.69171"),
			new City("Sydney", "-33.86785", "151.20732"),
			new City("Riyadh", "24.68773", "46.72185"),
			new City("Zurich", "47.36667", "8.55"),
			new City("Reykjavik", "64.13548", "-21.89541"),
			new City("Mexico City", "19.42847", "-99.12766"),
			new City("Lima", "-12.04318", "-77.02824"));

}
