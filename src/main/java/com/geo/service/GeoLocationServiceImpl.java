package com.geo.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.geo.dal.City;
import com.geo.dal.GeoRepository;
import com.geo.dal.Location;
import com.geo.dal.LocationId;
import com.geo.infra.util.CalcUtils;
import com.geo.infra.util.ValidationUtils;
import com.jayway.restassured.path.json.JsonPath;

@Service
class GeoLocationServiceImpl implements GeoLocationService {

	private static final String MILES_LABEL = "mi";
	private static final double CITY_DISTANCE_THRESHOLD = 500.0;
	private static final String WITHIN_CITY_DISTANCE_THRESHOLD_LABEL = "(<=" + CITY_DISTANCE_THRESHOLD + " " + MILES_LABEL + ")";
	private static final String MORE_CITY_DISTANCE_THRESHOLD_LABEL = "(>" + CITY_DISTANCE_THRESHOLD + " " + MILES_LABEL + ")";

	@Value("${report.output.file}") 
	private String reportFile;	

	private static final String TAB = "\t";
	
	private static final String UNITED_STATES_SHORT_NAME = "US";
	private static final List<City> cities = Arrays.asList(
			new City("Tokyo", "35.6895", "139.69171"),
			new City("Sydney", "-33.86785", "151.20732"),
			new City("Riyadh", "24.68773", "46.72185"),
			new City("Zurich", "47.36667", "8.55"),
			new City("Reykjavik", "64.13548", "-21.89541"),
			new City("Mexico City", "19.42847", "-99.12766"),
			new City("Lima", "-12.04318", "-77.02824"));
	
	private static final int NUM_LOCATIONS_TO_GENERATE = 10000;
	private static final int LOCATIONE_POPULATION_ERROR_THRESHOLD = 1000;
	
	@Autowired
	GeoRepository geoRepo;

	@Override
	public List<Location> getAllDataSets() {
		List<Location> locations = geoRepo.findAll();
		return locations;
	}

	@Override
	public Boolean getData(BigDecimal latitude, BigDecimal longitude) {
		validateCoordinates(latitude, longitude);
		LocationId locationId = new LocationId(latitude, longitude);
		boolean locationExists = geoRepo.exists(locationId);
		return locationExists;
	}

	@Override
	public boolean addData(BigDecimal latitude, BigDecimal longitude) {
		validateCoordinates(latitude, longitude);		
		boolean locationExists = getData(latitude, longitude); 
		if (!locationExists) {
			Location location = new Location(latitude, longitude);
			geoRepo.save(location);
		}
		
		return !locationExists;
	}

	private void validateCoordinates(BigDecimal latitude, BigDecimal longitude) {
		ValidationUtils.notNull(latitude, "The parameter latitude must exist.");
		ValidationUtils.notNull(longitude, "The parameter longitude must exist.");
	}

	@Override
	public void doLocationsAnalysis() {
		List<Location> locations = getAllDataSets();		
		if (CollectionUtils.isNotEmpty(locations)) {
			// For each of the locations generate a spreadsheet row with the location's analysis data.
			RestTemplate restTemplate = new RestTemplate();
			List<String> rows = new ArrayList<>();
			for (Location location : locations) {
				generateLocationRow(restTemplate, location, rows);
			}
			
			if (CollectionUtils.isNotEmpty(rows)) {
				generateLocationsSpreadsheet(rows);
			}
		}		
	}

	/**
	 * The method generates a spreadsheet row containing the location's analysis information.
	 * @param restTemplate used in order to obtain the location's country using the Google Maps REST API.
	 * @param location the Location for whom to generate the row. 
	 * @param rows location's rows with analysis information.
	 */
	private void generateLocationRow(RestTemplate restTemplate, Location location, List<String> rows) {
		StringBuffer row = new StringBuffer(location.getLatitude() + TAB + location.getLongitude());
		String country = getLocationCountry(restTemplate, location);
		if (country == null) {
			writeIsInUnitedStatesCell("N/A", row);
		} else if (UNITED_STATES_SHORT_NAME.equals(country)) {
			writeIsInUnitedStatesCell("Yes", row);
		} else {
			writeIsInUnitedStatesCell("No", row);
			calcLocationDistanceFromCities(location, row);
		}
		
		rows.add(row.toString());
	}

	/**
	 * The method calculated the distance of the location from each of the cities.
	 * The results are written to the city's corresponding cell with an additional label
	 * indicating the the distance is within the defined threshold.
	 * @param location the location for whom to calculate the distance.
	 * @param row the row for whom to write the calculation results.
	 */
	private void calcLocationDistanceFromCities(Location location, StringBuffer row) {
		for (City city : cities) {
			double distance = CalcUtils.getDistance(location.getLatitude().doubleValue(), location.getLongitude().doubleValue(), 
					city.getLatitude().doubleValue(), city.getLongitude().doubleValue());

			row.append(TAB);
			row.append(distance);
			row.append(" ");
			row.append(MILES_LABEL);
			row.append(distance <= CITY_DISTANCE_THRESHOLD ? WITHIN_CITY_DISTANCE_THRESHOLD_LABEL : MORE_CITY_DISTANCE_THRESHOLD_LABEL);			
		}
	}

	/**
	 * The method generates a TSV spreadsheet from the given rows.
	 * @param rows the data rows from whom to generate the spreadsheet.
	 */
	private void generateLocationsSpreadsheet(List<String> rows) {
		// Join the headers and the rows and generate the report.
		List<String> report = new ArrayList<>();
		String headers = String.join(TAB, Arrays.asList("Latitude", "Longitude", "United States")) + TAB +
				cities.stream().map(city -> city.getName()).collect(Collectors.joining(TAB));

		report.add(headers);
		report.addAll(rows);
		Path outputPath = Paths.get(reportFile);
		try {
			Files.write(outputPath, report);
		} catch (IOException e) {
			System.out.println("Faild to write Locations spreadsheet to output file: " + reportFile + ".");
		}
	}

	private void writeIsInUnitedStatesCell(String cell, StringBuffer row) {
		row.append(TAB);
		row.append(cell);
	}

	/**
	 * The method finds the location's country using the Google Maps REST API.
	 * @param restTemplate used in order to perform the REST call to the Google Maps REST API.
	 * @param location the Location for whom to find the associated country. 
	 * @return the country (short name format) associated with the given location if exists (null otherwise).
	 */
	private String getLocationCountry(RestTemplate restTemplate, Location location) {
		String result = restTemplate.getForObject("http://maps.googleapis.com/maps/api/geocode/json?latlng={lat},{long}&sensor=false", 
				String.class, location.getLatitude(), location.getLongitude());
		String country = new JsonPath(result).get("results.address_components.flatten().find { it.types.flatten().contains('country') } ?.short_name");
		return country;
	}

	@Override
	public void populateLocations() throws UnexpectedInputException, Exception {
		FlatFileItemReader<City> itemReader = createItemReader();
		// Go over all city rows and randomly select a city location to be entered into the DB.
		itemReader.open(new ExecutionContext());
		try {
			int errors = 0;
			int cityWriten = 0;
			Random random = new Random();
			do {
				try {
					City city = itemReader.read();
					if (random.nextBoolean()) {
						// The city was randomly chosen to be inserted into the DB.
						boolean locationAdded = addData(city.getLatitude(), city.getLongitude());
						if (locationAdded) {
							cityWriten++;
						}
					}
				} catch (ParseException ex) {
					errors++;
				}
			}
			while (cityWriten < NUM_LOCATIONS_TO_GENERATE && errors < LOCATIONE_POPULATION_ERROR_THRESHOLD);
		} finally {
			itemReader.close();
		}
	}

	private FlatFileItemReader<City> createItemReader() {
		FlatFileItemReader<City> itemReader = new FlatFileItemReader<City>();
		itemReader.setResource(new FileSystemResource("src/main/resources/data/cities5000.txt"));
		itemReader.setLineMapper(new DefaultLineMapper<City>() {{
            setLineTokenizer(new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB) {{
            	setNames(new String[] {"geonameid",
                        "name",
                        "asciiname",
                        "alternatenames",
                        "latitude",
                        "longitude",
                        "feature class",
                        "feature code",
                        "country code",
                        "cc2",
                        "admin1 code",
                        "admin2 code",
                        "admin3 code",
                        "admin4 code",
                        "population",
                        "elevation",
                        "dem",
                        "timezone",
                        "modification date"});
            }});
            setFieldSetMapper(new CityFieldSetMapper());
        }});
		
		return itemReader;
	}

}
