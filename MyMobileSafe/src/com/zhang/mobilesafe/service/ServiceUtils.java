package com.zhang.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {
	
	/**
	 * 校验某个服务是否还活着 
	 * serviceName :传进来的服务的名称
	 */
	
	public static boolean isServiceRunning(Context context,String serviceName){
		//校验服务是否还活着
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos =  am.getRunningServices(100);
		for(RunningServiceInfo info : infos){
			//得到正在运行的服务
			String name = info.service.getClassName();
			if(serviceName.equals(name)){
				return true;
			}
		}
		return false;
	}

}
