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
	// �����Ƿ��Զ�����
	private SettingItemView siv_update;
	// �����Ƿ���������ʾ������
	private SettingItemView siv_show_address;
	/**
	 * �����������
	 */
	private SharedPreferences sp;

	private Intent showAddressIntent;
	// ������ʾ�򱳾�
	private SettingClickView scv_changebg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.activity_setting);
		siv_update = (SettingItemView) findViewById(R.id.siv_update);

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

		// �����Ƿ������������
		siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
		showAddressIntent = new Intent(this, AddressService.class);
		Boolean isRunning = ServiceUtils.isServiceRunning(this,
				"com.zhang.mobilesafe.service.AddressService");
		if (isRunning) {
			// ����������ʾ�ķ����Ѿ�������
			siv_show_address.setChecked(true);
		} else {
			// ����������ʾ�ķ����Ѿ��ر�
			siv_show_address.setChecked(false);
		}

		siv_show_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ����������ʾ�ķ����Ѿ�������
				if (siv_show_address.isChecked()) {
					// �ر�
					stopService(showAddressIntent);
					siv_show_address.setChecked(false);
				} else {
					// ����
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
	}
}
