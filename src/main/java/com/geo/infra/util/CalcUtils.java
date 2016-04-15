package com.geo.infra.util;

public class CalcUtils {
    
    /* The Earth's radius, in miles (an optimization radius. Earth's mean radius = 3,956.547).*/
    private static final double EARTH_RADIUS_M = 3961.0;
  
  /**
   * Gets the great circle distance in miles between two geographical points, using
   * the <a href="http://en.wikipedia.org/wiki/Haversine_formula">haversine formula</a>.
   *
   * @param latitude1 the latitude of the first point
   * @param longitude1 the longitude of the first point
   * @param latitude2 the latitude of the second point
   * @param longitude2 the longitude of the second point
   * @return the distance, in miles, between the two points
   */
  public static float getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
      double dLat = Math.toRadians(latitude2 - latitude1);
      double dLon = Math.toRadians(longitude2 - longitude1);
      double lat1 = Math.toRadians(latitude1);
      double lat2 = Math.toRadians(latitude2);
      double sqrtHaversineLat = Math.sin(dLat / 2);
      double sqrtHaversineLon = Math.sin(dLon / 2);
      double a = sqrtHaversineLat * sqrtHaversineLat + sqrtHaversineLon * sqrtHaversineLon
              * Math.cos(lat1) * Math.cos(lat2);
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  
      return (float) (EARTH_RADIUS_M * c);
  }
  
}
