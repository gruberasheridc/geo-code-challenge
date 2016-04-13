package com.geo.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.geo.dal.Location;
import com.geo.infra.dto.MappingService;
import com.geo.service.GeoLocationService;
import com.geo.service.LocationDTO;


@RestController
public class GeoController {
	
	@Autowired
	GeoLocationService geoService;
	
	@Autowired
	MappingService mappingService;
	
	@RequestMapping(value = "/geo", method = RequestMethod.GET)
	public List<LocationDTO> getLocations() {
		 List<Location> locations = geoService.getAllDataSets();
		 List<LocationDTO> retval = mappingService.map(locations, LocationDTO.class);
		 return retval;
	}
	
	@RequestMapping(value = "/geo/exist", method = RequestMethod.GET)
	public Boolean getLocation(@RequestParam @NotNull Float latitude, @RequestParam @NotNull Float longitude) {
		// TODO check the validation.
		Boolean doesLocationExist = geoService.getData(latitude, longitude);
		return doesLocationExist;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/geo", method = RequestMethod.POST)
	public void createLocation(@RequestBody LocationDTO locationDTO) {
		geoService.addData(locationDTO.getLatitude(), locationDTO.getLongitude());
	}

}
