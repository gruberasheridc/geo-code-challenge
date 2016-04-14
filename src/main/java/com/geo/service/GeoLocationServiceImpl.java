package com.geo.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.geo.dal.City;
import com.geo.dal.GeoRepository;
import com.geo.dal.Location;
import com.geo.dal.LocationId;
import com.geo.infra.util.Utils;
import com.jayway.restassured.path.json.JsonPath;

@Service
class GeoLocationServiceImpl implements GeoLocationService {
	
	@Value("${report.output,file}") 
	private String reportFile;
	

	private static final String TAB = "\t";
	private static final String NEW_LINE = System.lineSeparator(); 
	
	private static final String UNITED_STATES_SHORT_NAME = "US";
	private static final List<City> cities = Arrays.asList(
			new City("Tokyo", "35.6895", "139.69171"),
			new City("Sydney", "-33.86785", "151.20732"),
			new City("Riyadh", "24.68773", "46.72185"),
			new City("Zurich", "47.36667", "8.55"),
			new City("Reykjavik", "64.13548", "-21.89541"),
			new City("Mexico City", "19.42847", "-99.12766"),
			new City("Lima", "-12.04318", "-77.02824")); 
	
	@Autowired
	GeoRepository geoRepo;

	@Override
	public List<Location> getAllDataSets() {
		List<Location> locations = geoRepo.findAll();
		return locations;
	}

	@Override
	public Boolean getData(BigDecimal latitude, BigDecimal longitude) {
		LocationId locationId = new LocationId(latitude, longitude);
		boolean locationExists = geoRepo.exists(locationId);
		return locationExists;
	}

	@Override
	public void addData(BigDecimal latitude, BigDecimal longitude) {
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
			List<String> rows = new ArrayList<>();
			for (Location location : locations) {
				StringBuffer row = new StringBuffer(location.getLatitude() + TAB + location.getLongitude());
				String result = restTemplate.getForObject("http://maps.googleapis.com/maps/api/geocode/json?latlng={lat},{long}&sensor=false", 
						String.class, location.getLatitude(), location.getLongitude());
				String country = new JsonPath(result).get("results.address_components.flatten().find { it.types.flatten().contains('country') } ?.short_name");
				if (country == null) {
					row.append(TAB);
					row.append("N/A");
				} else if (UNITED_STATES_SHORT_NAME.equals(country)) {
					row.append(TAB);
					row.append("Yes");
				} else {
					row.append(TAB);
					row.append("No");
					for (City city : cities) {
						double distance = Utils.getDistance(location.getLatitude().doubleValue(), location.getLongitude().doubleValue(), 
								city.getLatitude().doubleValue(), city.getLongitude().doubleValue());

						row.append(TAB);
						row.append(distance);
						System.out.println("City: " + city.getName() + "," + "Distance: " + distance);
					}
				}
				
				rows.add(row.toString());
			}
			
			if (CollectionUtils.isNotEmpty(rows)) {
				List<String> report = new ArrayList<>();
				String headers = String.join(TAB, Arrays.asList("Latitude", "Longitude", "United States")) + TAB +
						cities.stream().map(city -> city.getName()).collect(Collectors.joining(TAB));
				report.add(headers);
				report.addAll(rows);
				Path outputPath = Paths.get(reportFile);
				try {
					Files.write(outputPath, report);
				} catch (IOException e) {
					System.out.println("Faild to write Report output file name: " + reportFile + ".");
				}
			}
		}		
	}

}
