package com.zhang.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Debug.MemoryInfo;

import com.zhang.mobilesafe.domain.AppInfo;
import com.zhang.mobilesafe.domain.TaskInfo;
import com.zhang.mobilesafe.domain.TrafficInfo;
import com.zhang.mobliesafe.R;

public class TrafficInfoProvider {

	/**
	 * 获取所有流量信息。
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static List<TrafficInfo> getTrafficInfos(Context context) {
		// 1获取一个包管理器
		PackageManager pm = context.getPackageManager();
		// 2遍历手机操作系统,获取所有应用的uid
		List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(0);
		List<TrafficInfo> trafficInfos = new ArrayList<TrafficInfo>();

		for (ApplicationInfo applicationInfo : applicationInfos) {
			TrafficInfo trafficInfo = new TrafficInfo();
			int uid = applicationInfo.uid;
			long tx = TrafficStats.getUidTxBytes(uid);// 上传的流量
			long rx = TrafficStats.getUidRxBytes(uid);// 下载的流量byte
			long total = rx + tx;
			Drawable icon = applicationInfo.loadIcon(pm);
			String name = applicationInfo.loadLabel(pm).toString();

			trafficInfo.setIcon(icon);
			trafficInfo.setName(name);
			trafficInfo.setRx_traffic(rx);
			trafficInfo.setTx_traffic(tx);
			trafficInfo.setTotal_traffic(total);
			trafficInfos.add(trafficInfo);
		}
		return trafficInfos;
	}

}
