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
	// �����Ƿ��Զ�����
	private SettingItemView siv_update;
	// �����Ƿ���������ʾ������
	private SettingItemView siv_show_address;

	// ��������������
	private SettingItemView siv_callsms_safe;
	private Intent callSmsSafeIntent;

	// ���������Ź�������
	private SettingItemView siv_watchdog;
	private Intent watchDogIntent;
	// �����������
	private SharedPreferences sp;

	private Intent showAddressIntent;
	// ������ʾ�򱳾�
	private SettingClickView scv_changebg;

	@Override
	protected void onResume() {
		super.onResume();

		Boolean isRunning = ServiceUtils.isServiceRunning(this,
				"com.zhang.mobilesafe.service.AddressService");
		if (isRunning) {
			// ����������ʾ�ķ����Ѿ�������
			Log.i(TAG, "����������ʾ�ķ����Ѿ�������");
			siv_show_address.setChecked(true);
		} else {
			// ����������ʾ�ķ����Ѿ��ر�
			Log.i(TAG, "����������ʾ�ķ����Ѿ��ر���");
			siv_show_address.setChecked(false);
		}

		boolean update = sp.getBoolean("update", false);
		if (update) {
			// �Զ������Ѿ�����
			siv_update.setChecked(true);
			siv_update.setDesc("�Զ������Ѿ�����");
		} else {
			// �Զ������Ѿ��ر�
			siv_update.setChecked(false);
			siv_update.setDesc("�Զ������Ѿ��ر�");
		}

		Boolean isCallSmsServiceRunning = ServiceUtils.isServiceRunning(this,
				"com.zhang.mobilesafe.service.CallSmsSafeService");
		if (isCallSmsServiceRunning) {
			// ���غ����������Ѿ�������
			Log.i(TAG, "���غ����������Ѿ�������");
			siv_callsms_safe.setChecked(true);
		} else {
			// ���غ����������Ѿ��ر�
			Log.i(TAG, "���غ����������Ѿ��ر���");
			siv_callsms_safe.setChecked(false);
		}
		// siv_callsms_safe.setChecked(iscallSmsServiceRunning);
		
		Boolean isWatchDogServiceRunning = ServiceUtils.isServiceRunning(this,
				"com.zhang.mobilesafe.service.WatchDogService");
		siv_watchdog.setChecked(isWatchDogServiceRunning);
		Log.i(TAG, "�������������ر���"+isWatchDogServiceRunning);
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
				// �ж��Ƿ�ѡ��
				// �Ѿ����Զ�����
				if (siv_update.isChecked()) {
					siv_update.setChecked(false);
					siv_update.setDesc("�Զ������Ѿ��ر�");
					editor.putBoolean("update", false);
				} else {
					// û�д��Զ�����
					siv_update.setChecked(true);
					siv_update.setDesc("�Զ������Ѿ�����");
					editor.putBoolean("update", true);
				}
				editor.commit();

			}
		});

		// �����Ƿ�����������ؿؼ�
		showAddressIntent = new Intent(this, AddressService.class);
		siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
		siv_show_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ����������ʾ�ķ����Ѿ�������
				if (siv_show_address.isChecked()) {
					// �ر�
					Log.i(TAG, "����������ʾ��Ϊ��ѡ��״̬");
					stopService(showAddressIntent);
					siv_show_address.setChecked(false);
				} else {
					// ����
					Log.i(TAG, "����������ʾ��Ϊѡ��״̬");
					startService(showAddressIntent);
					siv_show_address.setChecked(true);
				}
			}
		});
		// ���ú�������ر���
		scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
		scv_changebg.setTitle("��������ʾ����");
		final String[] items = { "��͸��", "������", "��ʿ��", "������", "ƻ����" };
		int which = sp.getInt("which", 0);
		scv_changebg.setDesc(items[which]);
		scv_changebg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int dd = sp.getInt("which", 0);
				// ����һ���Ի���
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("��������ʾ����");
				builder.setSingleChoiceItems(items, dd,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// ����ѡ�����
								Editor editor = sp.edit();
								editor.putInt("which", which);
								editor.commit();
								scv_changebg.setDesc(items[which]);

								// ȡ���Ի���
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("ȡ��", null);
				builder.show();
			}
		});
		// ��������������
		callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
		siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
		siv_callsms_safe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ���������������Ƿ�����
				if (siv_callsms_safe.isChecked()) {
					// ��Ϊ��ѡ��״̬
					Log.i(TAG, "�������������ñ�Ϊ��ѡ��״̬");

					stopService(callSmsSafeIntent);
					siv_callsms_safe.setChecked(false);
				} else {
					// ѡ��״̬
					Log.i(TAG, "�������������ñ�Ϊѡ��״̬");
					startService(callSmsSafeIntent);
					siv_callsms_safe.setChecked(true);
				}

			}
		});

		// ����������
		watchDogIntent = new Intent(this, WatchDogService.class);
		siv_watchdog = (SettingItemView) findViewById(R.id.siv_watchdog);
		siv_watchdog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �����������Ƿ�����
				if (siv_watchdog.isChecked()) {
					// ��Ϊ��ѡ��״̬
					Log.i(TAG, "���������ñ�Ϊ��ѡ��״̬");

					stopService(watchDogIntent);
					siv_watchdog.setChecked(false);
				} else {
					// ѡ��״̬
					Log.i(TAG, "���������ñ�Ϊѡ��״̬");
					startService(watchDogIntent);
					siv_watchdog.setChecked(true);
				}

			}
		});
	}
}
