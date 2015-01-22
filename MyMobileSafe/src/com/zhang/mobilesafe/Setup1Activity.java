package com.zhang.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

import com.zhang.mobliesafe.R;

public class Setup1Activity extends BaseSetupActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		
	}
	

	@Override
	public void showNext() {
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		finish();
		//要求在finish()或者startActivity(intent)后面
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		
	}
}
