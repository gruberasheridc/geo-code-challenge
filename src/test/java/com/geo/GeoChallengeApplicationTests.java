package com.geo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.geo.service.GeoLocationService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GeoApplication.class)
@WebAppConfiguration
public class GeoChallengeApplicationTests {
	
	@Autowired
	GeoLocationService geoService;

	@Test
	public void contextLoads() {
		geoService.analyzeLocations();
	}

}
