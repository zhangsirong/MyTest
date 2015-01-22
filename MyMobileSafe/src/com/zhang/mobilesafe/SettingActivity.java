package com.zhang.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhang.mobilesafe.service.AddressService;
import com.zhang.mobilesafe.service.ServiceUtils;
import com.zhang.mobilesafe.ui.SettingClickView;
import com.zhang.mobilesafe.ui.SettingItemView;
import com.zhang.mobliesafe.R;

public class SettingActivity extends Activity {
	// 设置是否自动更新
	private SettingItemView siv_update;
	// 设置是否开启来电显示归属地
	private SettingItemView siv_show_address;
	/**
	 * 保存软件参数
	 */
	private SharedPreferences sp;

	private Intent showAddressIntent;
	// 设置显示框背景
	private SettingClickView scv_changebg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.activity_setting);
		siv_update = (SettingItemView) findViewById(R.id.siv_update);

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

		// 设置是否开启来电归属地
		siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
		showAddressIntent = new Intent(this, AddressService.class);
		Boolean isRunning = ServiceUtils.isServiceRunning(this,
				"com.zhang.mobilesafe.service.AddressService");
		if (isRunning) {
			// 监听来电显示的服务已经开起了
			siv_show_address.setChecked(true);
		} else {
			// 监听来电显示的服务已经关闭
			siv_show_address.setChecked(false);
		}

		siv_show_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 监听来电显示的服务已经开起了
				if (siv_show_address.isChecked()) {
					// 关闭
					stopService(showAddressIntent);
					siv_show_address.setChecked(false);
				} else {
					// 开启
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
	}
}
