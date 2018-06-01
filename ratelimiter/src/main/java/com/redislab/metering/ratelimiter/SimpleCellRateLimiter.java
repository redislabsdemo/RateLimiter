package com.redislabs.metering.ratelimiter;

import java.util.Properties;

import redis.clients.jedis.Jedis;


/*
 * This implementation is an example of a limit enforcer -- it queues all 
 * incoming cells in a Redis List. An independent thread pops the cell
 * when it is within the rate limits.
 * 
 * Note: Replace "Take action here....." and implement your own logic 
 * 
 */
public class SimpleCellRateLimiter extends RateLimiter implements Runnable{	
	private String key = "GenericCell"; //default key
	
	private RateLimiter uniformRateLimiter = null;
	
	public SimpleCellRateLimiter(Properties props) throws Exception{
		super();
		
		props.setProperty("type", RateLimiterFactory.DISTRIBUTION_TYPE_UNIFORM);
		uniformRateLimiter = RateLimiterFactory.getRateLimiter(props);
		
		Thread t = new Thread(this);
		t.start();
	}
	
	
	public boolean arrival(String cell){
		System.out.println("In: "+(System.currentTimeMillis()/1000)+" "+cell);
		jedis.lpush(key, cell);		
		return true;
	}
	
	public void run(){
		
		try{
			RedisConnection conn = RedisConnection.getRedisConnection();
			Jedis jedis = conn.getJedis(); 
			
			while(true){
				String cell = jedis.rpop(key);
				boolean success = false;
				while(!success){
					success = uniformRateLimiter.arrival(cell);
					if(success){
						
						// Take action here.....						
						
						System.out.println("Out: "+(System.currentTimeMillis()/1000)+" "+cell);
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}