package com.zhang.mobilesafe;

import com.zhang.mobliesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class AtoolsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	/**
	 * ����¼�,�����������ز�ѯ��ҳ��
	 */
	public void numberQuery(View view){
		Intent intent = new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intent);
	}
}
