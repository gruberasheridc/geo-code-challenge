package com.geo.dal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoRepository extends JpaRepository<Location, LocationId> {

}
