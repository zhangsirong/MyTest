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
	 * 点击事件,进入号码归属地查询的页面
	 */
	public void numberQuery(View view){
		Intent intent = new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intent);
	}
}
