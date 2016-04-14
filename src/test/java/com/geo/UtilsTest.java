package com.geo;

import java.math.BigDecimal;

import org.junit.Test;

import com.geo.infra.util.Utils;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class UtilsTest extends TestCase
   
{
    @Test
    public void testIt() {
    	// Tokyo
    	BigDecimal lat1 = new BigDecimal("35.6895");
    	BigDecimal lon1 = new BigDecimal("139.69171");
    	
    	// Sydney
    	BigDecimal lat2 = new BigDecimal("-33.86785");
    	BigDecimal lon2 = new BigDecimal("151.20732");
    	
    	double distance = Utils.getDistance(lat1.doubleValue(), lon1.doubleValue(), lat1.doubleValue(), lon1.doubleValue());
    	System.out.println(distance);
    	
    	distance = Utils.getDistance(lat1.doubleValue(), lon1.doubleValue(), lat2.doubleValue(), lon2.doubleValue());
    	System.out.println(distance);
    }
}
