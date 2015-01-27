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
		//1��ȡһ����������
		PackageManager pm = getPackageManager();
		//2�����ֻ�����ϵͳ,��ȡ����Ӧ�õ�uid
		List<ApplicationInfo> applicationInfos = new ArrayList<ApplicationInfo>();
		for(ApplicationInfo applicationInfo :applicationInfos){
			int uid = applicationInfo.uid;
			long tx = TrafficStats.getUidTxBytes(uid);//�ϴ������� 
			long rx = TrafficStats.getUidRxBytes(uid);//���ص�����byte
		}
		TrafficStats.getTotalTxBytes();//��ȡȫ�� wifi 3g/2g�ϴ���������
		TrafficStats.getTotalRxBytes();//��ȡȫ�� wifi 3g/2g���ص�������
		setContentView(R.layout.activity_traffic_manager);
	}
}
