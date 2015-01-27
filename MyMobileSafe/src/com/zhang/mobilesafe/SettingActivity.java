package com.zhang.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhang.mobilesafe.service.AddressService;
import com.zhang.mobilesafe.service.CallSmsSafeService;
import com.zhang.mobilesafe.service.WatchDogService;
import com.zhang.mobilesafe.ui.SettingClickView;
import com.zhang.mobilesafe.ui.SettingItemView;
import com.zhang.mobilesafe.utils.ServiceUtils;
import com.zhang.mobliesafe.R;

public class SettingActivity extends Activity {
	protected static final String TAG = "SettingActivity";
	// 设置是否自动更新
	private SettingItemView siv_update;
	// 设置是否开启来电显示归属地
	private SettingItemView siv_show_address;

	// 黑名单拦截设置
	private SettingItemView siv_callsms_safe;
	private Intent callSmsSafeIntent;

	// 程序锁看门狗的设置
	private SettingItemView siv_watchdog;
	private Intent watchDogIntent;
	// 保存软件参数
	private SharedPreferences sp;

	private Intent showAddressIntent;
	// 设置显示框背景
	private SettingClickView scv_changebg;

	@Override
	protected void onResume() {
		super.onResume();

		Boolean isRunning = ServiceUtils.isServiceRunning(this,
				"com.zhang.mobilesafe.service.AddressService");
		if (isRunning) {
			// 监听来电显示的服务已经开起了
			Log.i(TAG, "监听来电显示的服务已经开起了");
			siv_show_address.setChecked(true);
		} else {
			// 监听来电显示的服务已经关闭
			Log.i(TAG, "监听来电显示的服务已经关闭了");
			siv_show_address.setChecked(false);
		}

		boolean update = sp.getBoolean("update", false);
		if (update) {
			// 自动升级已经开启
			siv_update.setChecked(true);
			siv_update.setDesc("自动升级已经开启");
		} else {
			// 自动升级已经关闭
			siv_update.setChecked(false);
			siv_update.setDesc("自动升级已经关闭");
		}

		Boolean isCallSmsServiceRunning = ServiceUtils.isServiceRunning(this,
				"com.zhang.mobilesafe.service.CallSmsSafeService");
		if (isCallSmsServiceRunning) {
			// 拦截黑名单服务已经开起了
			Log.i(TAG, "拦截黑名单服务已经开起了");
			siv_callsms_safe.setChecked(true);
		} else {
			// 拦截黑名单服务已经关闭
			Log.i(TAG, "拦截黑名单服务已经关闭了");
			siv_callsms_safe.setChecked(false);
		}
		// siv_callsms_safe.setChecked(iscallSmsServiceRunning);
		
		Boolean isWatchDogServiceRunning = ServiceUtils.isServiceRunning(this,
				"com.zhang.mobilesafe.service.WatchDogService");
		siv_watchdog.setChecked(isWatchDogServiceRunning);
		Log.i(TAG, "程序锁服务开启关闭了"+isWatchDogServiceRunning);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.activity_setting);
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		siv_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				// 判断是否选中
				// 已经打开自动升级
				if (siv_update.isChecked()) {
					siv_update.setChecked(false);
					siv_update.setDesc("自动升级已经关闭");
					editor.putBoolean("update", false);
				} else {
					// 没有打开自动升级
					siv_update.setChecked(true);
					siv_update.setDesc("自动升级已经开启");
					editor.putBoolean("update", true);
				}
				editor.commit();

			}
		});

		// 设置是否开启来电归属地控件
		showAddressIntent = new Intent(this, AddressService.class);
		siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
		siv_show_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 监听来电显示的服务已经开起了
				if (siv_show_address.isChecked()) {
					// 关闭
					Log.i(TAG, "监听来电显示变为非选中状态");
					stopService(showAddressIntent);
					siv_show_address.setChecked(false);
				} else {
					// 开启
					Log.i(TAG, "监听来电显示变为选中状态");
					startService(showAddressIntent);
					siv_show_address.setChecked(true);
				}
			}
		});
		// 设置号码归属地背景
		scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
		scv_changebg.setTitle("归属地提示框风格");
		final String[] items = { "半透明", "活力蓝", "卫士蓝", "金属灰", "苹果绿" };
		int which = sp.getInt("which", 0);
		scv_changebg.setDesc(items[which]);
		scv_changebg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int dd = sp.getInt("which", 0);
				// 弹出一个对话框
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("归属地提示框风格");
				builder.setSingleChoiceItems(items, dd,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 保存选择参数
								Editor editor = sp.edit();
								editor.putInt("which", which);
								editor.commit();
								scv_changebg.setDesc(items[which]);

								// 取消对话框
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消", null);
				builder.show();
			}
		});
		// 黑名单拦截设置
		callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
		siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
		siv_callsms_safe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 黑名单拦截设置是否开启了
				if (siv_callsms_safe.isChecked()) {
					// 变为非选中状态
					Log.i(TAG, "黑名单拦截设置变为非选中状态");

					stopService(callSmsSafeIntent);
					siv_callsms_safe.setChecked(false);
				} else {
					// 选择状态
					Log.i(TAG, "黑名单拦截设置变为选中状态");
					startService(callSmsSafeIntent);
					siv_callsms_safe.setChecked(true);
				}

			}
		});

		// 程序锁设置
		watchDogIntent = new Intent(this, WatchDogService.class);
		siv_watchdog = (SettingItemView) findViewById(R.id.siv_watchdog);
		siv_watchdog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 程序锁设置是否开启了
				if (siv_watchdog.isChecked()) {
					// 变为非选中状态
					Log.i(TAG, "程序锁设置变为非选中状态");

					stopService(watchDogIntent);
					siv_watchdog.setChecked(false);
				} else {
					// 选择状态
					Log.i(TAG, "程序锁设置变为选中状态");
					startService(watchDogIntent);
					siv_watchdog.setChecked(true);
				}

			}
		});
	}
}
