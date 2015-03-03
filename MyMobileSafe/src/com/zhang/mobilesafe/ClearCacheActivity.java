package com.zhang.mobilesafe;

import java.lang.reflect.Method;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.mobilesafe.AntiVirusActivity.ScanInfo;
import com.zhang.mobliesafe.R;

public class ClearCacheActivity extends Activity {
	protected static final int FINISH = 1;
	private ProgressBar pb;
	private TextView tv_scan_status;
	private PackageManager pm;
	private LinearLayout ll_container;
	private int progress = 0;
	private Handler handler = new Handler(){
	@Override
		public void handleMessage(Message msg) {
		switch (msg.what) {
		case FINISH:
			tv_scan_status.setText("扫描完毕");
			break;
		}	}
	};

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
	 * 扫描手机里面所有应用程序的缓存信息
	 */
	private void scanCache() {
		pm = getPackageManager();
		new Thread() {
			@SuppressLint("NewApi")
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
						Method myUserId = UserHandle.class.getDeclaredMethod("myUserId");  
						int userID = (Integer) myUserId.invoke(pm,null);
						getSizeInfoMethod.invoke(pm, packInfo.packageName, userID, new MyObserver());
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
				Message msg = Message.obtain();
				msg.what = FINISH;
				handler.sendMessage(msg);

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
			final long data = pStats.dataSize;
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
							tv_scan_status.setText("正在扫描:"+ appInfo.loadLabel(pm));
							if (data >0) {
								View view = View.inflate(getApplicationContext(), R.layout.list_item_cacheinfo,null);
								TextView tv_name = (TextView) view.findViewById(R.id.tv_app_name);
								tv_name.setText(appInfo.loadLabel(pm));
								TextView tv_cache = (TextView) view.findViewById(R.id.tv_cache_size);
								tv_cache.setText("缓存大小:"+Formatter.formatFileSize(getApplicationContext(), cache));
								TextView tv_data = (TextView) view.findViewById(R.id.tv_data_size);
								tv_data.setText("数据大小:"+Formatter.formatFileSize(getApplicationContext(), data));
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
				
			}
		}
		Toast.makeText(getApplicationContext(), "清理完毕", 0).show();
	}
}
