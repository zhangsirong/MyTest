package com.zhang.mobilesafe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhang.mobliesafe.R;
import com.zhang.mobliesafe.R.id;

public class ClearCacheActivity extends Activity {
	private ProgressBar pb;
	private TextView tv_scan_status;
	private PackageManager pm;
	private LinearLayout ll_container;
	private int progress = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clear_cache);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		pb = (ProgressBar) findViewById(R.id.pb);

		scanCache();
	}

	/**
	 * ɨ���ֻ���������Ӧ�ó���Ļ�����Ϣ
	 */
	private void scanCache() {
		pm = getPackageManager();
		new Thread() {
			public void run() {
				Method getSizeInfoMethod = null;
				Method[] methods = PackageManager.class.getMethods();
				for (Method method : methods) {
					if ("getPackageSizeInfo".equals(method.getName())) {
						getSizeInfoMethod = method;
					}
				}
				List<PackageInfo> packInfos = pm.getInstalledPackages(0);
				pb.setMax(packInfos.size());
				for (PackageInfo packInfo : packInfos) {
					try {
						getSizeInfoMethod.invoke(pm, packInfo.packageName,10000, new MyObserver());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					progress++;
					pb.setProgress(progress);

				}
				
				

			};
		}.start();
	}

	private class MyObserver extends IPackageStatsObserver.Stub {

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			final ApplicationInfo appInfo;
			final long cache = pStats.cacheSize;
			long code = pStats.codeSize;
			long data = pStats.dataSize;
			final String packname = pStats.packageName;
			try {
				appInfo = pm.getApplicationInfo(packname, 0);
				
				System.out.println("cache:"	+ Formatter.formatFileSize(getApplicationContext(),	cache));
				System.out.println("code:"+ Formatter.formatFileSize(getApplicationContext(),code));
				System.out.println("data:"+ Formatter.formatFileSize(getApplicationContext(),data));
				System.out.println(packname);
				System.out.println("-------");
				
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							tv_scan_status.setText("����ɨ��:"+ appInfo.loadLabel(pm));
							if (cache >=0) {
								View view = View.inflate(getApplicationContext(), R.layout.list_item_cacheinfo,null);
								TextView tv_name = (TextView) view.findViewById(R.id.tv_app_name);
								tv_name.setText(appInfo.loadLabel(pm));
								TextView tv_cache = (TextView) view.findViewById(R.id.tv_cache_size);
								tv_cache.setText("�����С:"+Formatter.formatFileSize(getApplicationContext(), cache));
								ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
								iv_delete.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										try {
											Method method = PackageManager.class.getMethod("deleteApplicationCacheFiles", String.class,IPackageDataObserver.class);
											method.invoke(pm, packname,new MyPackDataObserver());
										} catch (Exception e) {
											e.printStackTrace();
										}
										
									}
								});
								ll_container.addView(view,0);
							}
						}
					});
				
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

		}
	}
	
	private class MyPackDataObserver extends IPackageDataObserver.Stub{

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)throws RemoteException {
			System.out.println(packageName+succeeded);
			
			
		}
		
	}
	public void clearAll(View view){
//		/freeStorageAndNotify
		Method[] methods = PackageManager.class.getMethods();
		for(Method method:methods){
			if("freeStorageAndNotify".equals(method.getName())){
				try {
					method.invoke(pm, Integer.MAX_VALUE, new IPackageDataObserver.Stub() {
						@Override
						public void onRemoveCompleted(String packageName,
								boolean succeeded) throws RemoteException {
							System.out.println(succeeded);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}
}
