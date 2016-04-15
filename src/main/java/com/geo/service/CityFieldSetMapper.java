package com.geo.service;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.geo.dal.City;

public class CityFieldSetMapper implements FieldSetMapper<City>{

	@Override
	public City mapFieldSet(FieldSet fieldSet) {
		if (fieldSet == null) {
			return null;
		}

		City city = new City(fieldSet.readString(CityConstants.CITY_ASCIINAME),
				fieldSet.readString(CityConstants.CITY_LATITUDE),
				fieldSet.readString(CityConstants.CITY_LONGITUDE));
		
		return city;
	}

}