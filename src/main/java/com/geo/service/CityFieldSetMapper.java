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

		City city = new City(fieldSet.readString("asciiname"), fieldSet.readString("latitude"), fieldSet.readString("longitude"));
		return city;
	}

}