package com.redislabs.metering.ratelimiter;

import redis.clients.jedis.Jedis;


/*
 * This is an abstract class that connects to a Redis 
 * data store for storing cell information.
 * 
 * RateLimiter defines the method, arrival(String cell).
 * A cell is the message or the item that's tracked continuously. 
 * The implementation of this method returns true if the rate of  
 * arrival is within the limits, but false if it exceeds the limit.
 * 
 * This class can be implemented in two patterns:
 * 
 * 1. As a limit checker - the RateLimiter tells you whether you 
 *    are within the limits based on the rules set by the specific
 *    implementation. It's the application's responsibility to 
 *    maintain the queue.
 *    
 *    Examples: UniformRateLimiter.java, BurstyRateLimiter.java
 *    
 * 2. As a limit enforcer - In this case, the implementation will
 *    queue the cells in a Redis List data structure, and consume
 *    the data in a separate thread  
 *    
 *    Example: GenericCellRateLimiter.java
 * 
 */
public abstract class RateLimiter
{
	protected RateLimiter rateLimiter = null;
	protected RedisConnection conn = null;
	protected Jedis jedis = null; 
	
	
	protected RateLimiter() throws Exception{
		initRedisConnection();
	}
	
	/*
	 * Connect to Redis
	 */
	protected void initRedisConnection() throws Exception{
		conn = RedisConnection.getRedisConnection();
		jedis = conn.getJedis();

	}
	
	
	/*
	 * @param cell - the message or the item that's ratelimited upon
	 * @return true if accepted; false if not   
	 * 
	 */
	public boolean arrival(String cell){
		return false;
	}
	
}