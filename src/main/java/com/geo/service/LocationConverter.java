package com.geo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.geo.dal.Location;
import com.geo.infra.dto.MappingService;

@Component
public class LocationConverter {

    @Autowired
    private MappingService mappingService;
    
    public LocationDTO toDTO(Location location) {
    	LocationDTO retval = mappingService.map(location, LocationDTO.class);
		return retval;
    }

	public List<LocationDTO> toDTOs(List<Location> accounts) {
		return mappingService.map(accounts, LocationDTO.class);
	}
    
}