package com.zhang.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup2Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
	}
	
	/**
	 * 下一步
	 * @param view
	 */
	@Override
	public void showNext() {
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		
	}
	/**
	 * 上一步
	 * @param view
	 */
	@Override
	public void showPre() {
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
		
	}
}
