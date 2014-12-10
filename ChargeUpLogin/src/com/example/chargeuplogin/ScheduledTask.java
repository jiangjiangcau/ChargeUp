package com.example.chargeuplogin;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


//Create a class extends with TimerTask
public class ScheduledTask
{
	private Timer timer;
	private final int UPDATE_TIME_PERIOD = 10000;
	
	public ScheduledTask(){
		
	}
	// Add your task here
	public void notificationTask() {
		timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run() {
				
                  
			}
		}, new Date(), UPDATE_TIME_PERIOD);
	
	}
	
	public void exitTimer(){
		timer.cancel();
	}
}
