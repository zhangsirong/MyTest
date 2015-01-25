package com.zhang.myapple;

public class Counter {
	private long mLastTime = System.currentTimeMillis();
	private int mInterval = 1000;
	public boolean isTimeUp(){
		long current = System.currentTimeMillis();
		if(current - mLastTime>mInterval){	
			mLastTime = mLastTime + mInterval;
			return true;
		}	
		return false;
	}
	public void setInterval(int interval){
		mInterval = interval;
	}
}
