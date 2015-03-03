package com.zhang.mobilesafe.utils;

import java.util.List;

import com.zhang.mobilesafe.domain.TrafficInfo;

public class SortUtils {
	public static void sortList(boolean sortFlag,List<TrafficInfo> trafficInfos){
		if(sortFlag){
			for (int j = 0; j < trafficInfos.size(); j++) {
				for (int i = trafficInfos.size()-1; i > 0; i--) {
					if(trafficInfos.get(i-1).getTotal_traffic() > trafficInfos.get(i).getTotal_traffic()){
						TrafficInfo t = trafficInfos.get(i);
						trafficInfos.set(i,trafficInfos.get(i-1));
						trafficInfos.set(i-1,t);
					}
				}
			}
			
			sortFlag = false;
		}else{
			for (int j = 0; j < trafficInfos.size(); j++) {
				for (int i = trafficInfos.size()-1; i > 0; i--) {
					if(trafficInfos.get(i-1).getTotal_traffic() < trafficInfos.get(i).getTotal_traffic()){
						TrafficInfo t = trafficInfos.get(i);
						trafficInfos.set(i,trafficInfos.get(i-1));
						trafficInfos.set(i-1,t);
					}
				}
			}
			sortFlag = true;
		}
	}
}
