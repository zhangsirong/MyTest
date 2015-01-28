package com.zhang.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.mobilesafe.utils.MD5Utils;
import com.zhang.mobliesafe.R;

public class HomeActivity extends Activity {

	protected static final String TAG = "HomeActivity";
	private GridView list_home;
	private MyAdapter adapter;
	private SharedPreferences sp;
	private static String[] names = { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���", "����ͳ��",
			"�ֻ�ɱ��", "��������", "�߼�����", "��������"

	};

	private static int[] ids = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.activity_home);
		list_home = (GridView) findViewById(R.id.list_home);
		adapter = new MyAdapter();
		list_home.setAdapter(adapter);
		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {
				case 0:// �����ֻ�����
					showLostFindDialog();
					break;
				case 1:// ������������ؽ���
					intent = new Intent(HomeActivity.this, CallSmsSafeActivity.class);
					startActivity(intent);
					break;
				case 2:// �����������
					intent = new Intent(HomeActivity.this, AppManagerActivity.class);
					startActivity(intent);
					break;
				case 3:// ������̹���
					intent = new Intent(HomeActivity.this, TaskManagerActivity.class);
					startActivity(intent);
					break;
				case 4://��������ͳ��
					intent = new Intent(HomeActivity.this, TrafficManagerActivity.class);
					startActivity(intent);
					break;
				case 5://�����ֻ�ɱ��
					intent = new Intent(HomeActivity.this, AntiVirusActivity.class);
					startActivity(intent);
					break;
				case 6://���뻺������
					intent = new Intent(HomeActivity.this, ClearCacheActivity.class);
					startActivity(intent);
					break;

				case 7:// ����߼�����
					intent = new Intent(HomeActivity.this, AtoolsActivity.class);
					startActivity(intent);
					break;
				case 8:// ������������
					intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);

					break;
				default:
					break;
				}

			}
		});
	}

	protected void showLostFindDialog() {
		// �ж��Ƿ���������
		if (isSetupPwd()) {
			// �Ѿ���������,����������������Ի���
			showEnterDialog();
		} else {
			// û����������,����������������Ի���
			showSetupPwdDialog();
		}

	}

	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	private Button ok;
	private Button cancel;
	private AlertDialog dialog;

	/**
	 * ��������Ի���
	 */
	private void showSetupPwdDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		// �Զ��岼���ļ�
		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_setup_password, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȡ������Ի���
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȡ������
				String password = et_setup_pwd.getText().toString().trim();
				String password_confirm = et_setup_confirm.getText().toString()
						.trim();
				if (TextUtils.isEmpty(password)
						|| TextUtils.isEmpty(password_confirm)) {
					Toast.makeText(HomeActivity.this, "����Ϊ��", 0).show();
					return;
				}
				// �ж��Ƿ�һ�²�ȥ����
				if (password.equals(password_confirm)) {
					// һ�µĻ�,��������,�ѶԻ���ȥ��,�������ҳ��
					Editor editor = sp.edit();
					editor.putString("password", MD5Utils.md5PassWord(password));// ������ܺ������
					editor.commit();
					dialog.dismiss();
					Log.i(TAG, "һ�µĻ�,��������,�ѶԻ���ȥ��,�������ҳ��");
					Intent intent = new Intent(HomeActivity.this,
							LostFindActivity.class);
					startActivity(intent);

				} else {
					Toast.makeText(HomeActivity.this, "���벻һ��", 0).show();
					return;
				}
			}
		});
		builder.setView(view);
		dialog = builder.show();
	}

	/**
	 * ��������Ի���
	 */
	private void showEnterDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		// �Զ��岼���ļ�
		View view = View.inflate(HomeActivity.this,
				R.layout.dialog_enter_password, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȡ������Ի���
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȡ������
				String password = et_setup_pwd.getText().toString().trim();
				String savePassword = sp.getString("password", "");// ȡ�����ܺ��
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(HomeActivity.this, "����Ϊ��", 1).show();
					return;
				}
				if (MD5Utils.md5PassWord(password).equals(savePassword)) {
					// ����������ȷ,�Ի���ȥ��,������ҳ��
					dialog.dismiss();
					Log.i(TAG, "�Ի���ȥ��,������ҳ��");
					Intent intent = new Intent(HomeActivity.this,
							LostFindActivity.class);
					startActivity(intent);

				} else {
					// ����������ȷ����
					Toast.makeText(HomeActivity.this, "�������", 1).show();
					return;
				}
			}
		});
		builder.setView(view);
		// dialog = builder.create();
		// dialog.setView(view,0,0,0,0);
		dialog = builder.show();

	}

	/**
	 * �ж��Ƿ���������
	 * 
	 * @return
	 */
	private boolean isSetupPwd() {
		String password = sp.getString("password", null);
		// if(TextUtils.isEmpty(password)){
		// return false;
		// }else{
		// return true;
		// }
		return !TextUtils.isEmpty(password);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(HomeActivity.this,
					R.layout.list_item_home, null);
			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);

			tv_item.setText(names[position]);
			iv_item.setImageResource(ids[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

}