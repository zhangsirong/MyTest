package com.zhang.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * ϵͳ��Ϣ�Ĺ�����
 * 
 * @author hp
 *
 */
public class SystemInfoUtils {
	/**
	 * ��ȡ��������
	 * 
	 * @param context
	 * @return ��������
	 */
	public static int getRunningProcessCount(Context context) {
		// PackageManager//�������� ��̬������
		// ActivityManager //���̹����� �����Ϣ ��̬����
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		return infos.size();
	}

	/**
	 * ��ȡ�����ڴ��С
	 * 
	 * @param context
	 * @return
	 */
	public static long getAvailMem(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	/**
	 * ��ȡ�ֻ��ڴ��ܴ�С
	 * 
	 * @param context
	 * @return long byte
	 */
	public static long getTotalMem(Context context) {
		// ActivityManager am = (ActivityManager)
		// context.getSystemService(Context.ACTIVITY_SERVICE);
		// MemoryInfo outInfo = new MemoryInfo();
		// am.getMemoryInfo(outInfo);
		// return outInfo.totalMem;
		try {
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = br.readLine();
			// MemTotal: 1917916 kB
			// MemFree: 244596 kB
			StringBuffer sb = new StringBuffer();
			for (char c : line.toCharArray()) {
				if (c >= '0' && c <= '9') {
					sb.append(c);
				}
			}
			System.out.println(sb);
			return Long.parseLong(sb.toString())*1024 ;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
