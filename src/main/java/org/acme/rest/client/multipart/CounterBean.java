package org.acme.rest.client.multipart;

import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class CounterBean {
	
	
	private AtomicInteger counter = new AtomicInteger();

    public int get() {  
        return counter.get();
    }

    @Scheduled(every="10s")     
    void increment() {
        counter.incrementAndGet(); 
    }

    @Scheduled(cron="0 15 10 * * ?") 
    void cronJob(ScheduledExecution execution) {
        counter.incrementAndGet();
        System.out.println("Process.....");
        System.out.println(execution.getScheduledFireTime());
    }

    @Scheduled(cron = "{cron.expr}") 
    void cronJobWithExpressionInConfig() {
       counter.incrementAndGet();
       System.out.println("Cron expression configured in application.properties");
    }
    
  

}
