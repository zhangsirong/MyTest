package com.zhang.mobilesafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhang.mobilesafe.utils.SmsUtils;
import com.zhang.mobilesafe.utils.SmsUtils.BackUpCallback;
import com.zhang.mobliesafe.R;

public class AtoolsActivity extends Activity {
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}

	/**
	 * ����¼�,�����������ز�ѯ��ҳ��
	 */
	public void numberQuery(View view) {
		Intent intent = new Intent(this, NumberAddressQueryActivity.class);
		startActivity(intent);
	}

	/**
	 * ����¼�,���ŵı���
	 */
	public void smsBackup(View view) {
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("���ڱ��ݶ���");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					SmsUtils.backupSms(getApplicationContext(),
							new BackUpCallback() {
								@Override
								public void onSmsBackup(int progress) {
									pd.setProgress(progress);
								}
								@Override
								public void beforBackup(int max) {
									pd.setMax(max);
								}
							});
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this, "���ݳɹ�", 0)
									.show();

						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this, "����ʧ��", 0)
									.show();
						}
					});
				} finally {
					pd.dismiss();
				}

			}
		}.start();
		;
	}

	/**
	 * ����¼�,���ŵĻ�ԭ
	 */
	public void smsRestore(View view) {
		SmsUtils.restoreSms(this,false);
		Toast.makeText(AtoolsActivity.this, "��ԭ�ɹ�", 0)
		.show();
	}
}
