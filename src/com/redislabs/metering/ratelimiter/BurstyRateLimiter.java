package com.redislabs.metering.ratelimiter;

import java.util.Properties;

import redis.clients.jedis.Jedis;

/*
 * In this implementation of RateLimiter class, the rate limiter
 * allows the arrival of cells in bursts. For example, if the 
 * rate limiter has a rule that allows 1,000 in an hour, this
 * class allows all 1,000 cells to arrive in the 1st second
 * itself.   
 * 
 */
public class BurstyRateLimiter extends RateLimiter
{

	private String key = "BurstyRateLimiter"; //default key
	
	private int window = 3600; // in seconds. 1 hr default
	private int actions = 360; // 1 action every 10 seconds

	/*
	 * @param props: parameters within props are window and actions.
	 *               These properties define how many actions are allowed
	 *               within a window.
	 */
	public BurstyRateLimiter(Properties props) throws Exception{
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
	}
	
	public boolean arrival(String cell){
		
		long currentTime = System.currentTimeMillis();
		long lastWindow = (currentTime - (window * 1000))/1000;
		
		System.out.println("Window:" + window);
		System.out.println("Current Time :" + Long.toString(currentTime));
		System.out.println("Last interval:" + Long.toString(lastWindow));
		
		//delete all messages outside the window
		jedis.zremrangeByScore(key, "0", Long.toString(lastWindow));
		
		//is zcard less than the allowed number of actions
		long card = jedis.zcard(key);
		
		//If yes, add message to the sorted set and return true
		if(card < actions){
			jedis.zadd(key, (currentTime/1000), cell);
			
			return true;
		}
		
		return false;
	}
}