package com.redislabs.metering.ratelimiter;

import java.util.Properties;


/*
 * Follows the factory method pattern to instantiate 
 * the appropriate object.
 * 
 */
public class RateLimiterFactory{

	final static String DISTRIBUTION_TYPE_UNIFORM = "uniform"; 
	final static String DISTRIBUTION_TYPE_BURSTY = "bursty"; 
	final static String DISTRIBUTION_TYPE_GENERIC_CELL = "genericcell"; 
		
	public static RateLimiter getRateLimiter(Properties props) throws Exception{
		
		RateLimiter rateLimiter = null;
		
		String type = props.getProperty("type");
		if(type.equals(DISTRIBUTION_TYPE_UNIFORM)){
			rateLimiter = new UniformRateLimiter(props);
		}else if(type.equals(DISTRIBUTION_TYPE_BURSTY)){
			rateLimiter = new BurstyRateLimiter(props);
		}else if(type.equals(DISTRIBUTION_TYPE_GENERIC_CELL)){
			rateLimiter = new SimpleCellRateLimiter(props);
		}
		
		return rateLimiter;
	}
}