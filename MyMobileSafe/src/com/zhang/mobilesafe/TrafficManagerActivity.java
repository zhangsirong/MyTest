package com.zhang.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.zhang.mobliesafe.R;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;

public class TrafficManagerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//1获取一个包管理器
		PackageManager pm = getPackageManager();
		//2遍历手机操作系统,获取所有应用的uid
		List<ApplicationInfo> applicationInfos = new ArrayList<ApplicationInfo>();
		for(ApplicationInfo applicationInfo :applicationInfos){
			int uid = applicationInfo.uid;
			long tx = TrafficStats.getUidTxBytes(uid);//上传的流量 
			long rx = TrafficStats.getUidRxBytes(uid);//下载的流量byte
		}
		TrafficStats.getTotalTxBytes();//获取全部 wifi 3g/2g上传的总流量
		TrafficStats.getTotalRxBytes();//获取全部 wifi 3g/2g下载的总流量
		setContentView(R.layout.activity_traffic_manager);
	}
}
