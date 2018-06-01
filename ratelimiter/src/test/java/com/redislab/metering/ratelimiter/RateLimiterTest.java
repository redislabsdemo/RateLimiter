package com.redislab.metering.ratelimiter;

import java.util.Properties;

import com.redislabs.metering.ratelimiter.RateLimiter;
import com.redislabs.metering.ratelimiter.RateLimiterFactory;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class RateLimiterTest {
    @Test
    public void genericCellRateLimiterTest() throws Exception {
            Properties props = new Properties();
            
            props.setProperty("type", "genericcell");
            props.setProperty("window", "3600");
            props.setProperty("actions", "3600");
            
            RateLimiter rateLimiter = RateLimiterFactory.getRateLimiter(props);
            
            rateLimiter.arrival("test 1");
            rateLimiter.arrival("test 2");
            rateLimiter.arrival("test 3");
            rateLimiter.arrival("test 4");
            rateLimiter.arrival("test 5");
            rateLimiter.arrival("test 6");
            rateLimiter.arrival("test 7");
            rateLimiter.arrival("test 8");
            rateLimiter.arrival("test 9");
            rateLimiter.arrival("test 10");
            
        }
        
        @Test
        public void uniformRateLimiterTest() throws Exception {
            Properties props = new Properties();
            
            props.setProperty("type", "uniform");
            props.setProperty("window", "5");
            props.setProperty("actions", "2");
                    
            RateLimiter rateLimiter = RateLimiterFactory.getRateLimiter(props);
            
            boolean a = rateLimiter.arrival("test 1");
            System.out.println("Result 1: "+a);
            
            boolean b = rateLimiter.arrival("test 2");
            System.out.println("Result 2: "+b);
                
            boolean c = rateLimiter.arrival("test 3");
            System.out.println("Result 3: "+c);		

            Thread.sleep(5000);

            boolean d = rateLimiter.arrival("test 4");
            System.out.println("Result 4: "+d);				
            
        }	
        
        @Test
        public void burstyRateLimiterTest() throws Exception {
            Properties props = new Properties();
            
            props.setProperty("type", "allow_burst_traffic");
            props.setProperty("window", "5");
            props.setProperty("actions", "2");
                    
            RateLimiter rateLimiter = RateLimiterFactory.getRateLimiter(props);
            
            boolean a = rateLimiter.arrival("test 1");
            System.out.println("Result 1: "+a);
            
            boolean b = rateLimiter.arrival("test 2");
            System.out.println("Result 2: "+b);
                
            boolean c = rateLimiter.arrival("test 3");
            System.out.println("Result 3: "+c);		

            Thread.sleep(5000);

            boolean d = rateLimiter.arrival("test 4");
            System.out.println("Result 4: "+d);				
        }
}
