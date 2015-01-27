package com.zhang.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.zhang.mobilesafe.domain.AppInfo;

/**
 * ҵ�񷽷�,�ṩ�ֻ����氲װ������Ӧ�ó������Ϣ
 * 
 * @author hp
 *
 */
public class AppInfoProvider {
	/**
	 * ��ȡ���еİ�װ��Ӧ�ó�����Ϣ��
	 * @param context ������
	 * @return
	 */
	public static List<AppInfo> getAppInfos(Context context) {
		PackageManager pm = context.getPackageManager();
		// ���а�װ���ֻ��ϵ�Ӧ�ó�����Ϣ
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		for (PackageInfo packInfo : packInfos) {
			AppInfo appInfo = new AppInfo();
			// packInfo �൱��һ��Ӧ�ó�����嵥�ļ�
			String packname = packInfo.packageName;
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			String name = packInfo.applicationInfo.loadLabel(pm).toString();
			int flags = packInfo.applicationInfo.flags;//Ӧ�ó�����Ϣ�ı�� �൱���û��ύ�Ĵ��
			int uid = packInfo.applicationInfo.uid;
			//File rcvfile = new File("/proc/uid_stat/"+uid+"tcp_rcv");
			//File sndfile  = new File("/proc/uid_stat/"+uid+"tcp_snd");
			appInfo.setUid(uid);
			
			if((flags&ApplicationInfo.FLAG_SYSTEM)==0){
				//�û�����
				appInfo.setUserApp(true);
			}else{
				//ϵͳ����
				appInfo.setUserApp(false);
			}
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
				//�ֻ����ڴ�
				appInfo.setInRom(true);
			}else{
				//�ֻ���洢�豸
				appInfo.setInRom(false);
			}
			appInfo.setName(name);
			appInfo.setIcon(icon);
			appInfo.setPackname(packname);
			appInfos.add(appInfo);
		}
		return appInfos;
	}
}
