package com.redislabs.metering.ratelimiter;

import java.util.Properties;

import redis.clients.jedis.Jedis;

/*
 * This implementation of the RateLimiter evenly spreads out
 * the cells over a window. For example, if the rule allows
 * 60 cells in 60 minutes, this implementation will allow only
 * 1 cell every minute. 
 * 
 */
public class UniformRateLimiter extends RateLimiter
{
	
	
	private String key = "UniformRateLimiter"; //default key
	
	private int window = 3600; // in seconds. 1 hr default
	private int actions = 360; // 1 action every 10 seconds
	
	private int interval = 10; // default

	/*
	 * @param props: parameters within props are window and actions.
	 *               These properties define how many actions are allowed
	 *               within a window.
	 */
	public UniformRateLimiter(Properties props) throws Exception{
		super();
		
		String windowStr = props.getProperty("window");
		if(windowStr != null ){
			try{
				window = Integer.parseInt(windowStr);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		String actionsStr = props.getProperty("actions");
		if(actionsStr != null ){
			try{
				actions = Integer.parseInt(actionsStr);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		interval = window/actions;		
	}
	
	
	public boolean arrival(String cell){
		// check if the last message exists. 
		long ttl = jedis.ttl(key);
		if(ttl > 0){
			return false;
		}
	
		// The key lives through the period defined
		// by the interval
		if(key != null && cell != null){
			jedis.setex(key, interval, cell);			
			return true;
		}
		
		return false;
	}
	
}