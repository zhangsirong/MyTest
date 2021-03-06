package com.zhang.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.zhang.mobliesafe.R;

public class LostFindActivity extends Activity{
	
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//判断是否设置向导,如果没做过,就设置向导,否则留在当前页面
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			//就在手机防盗页面
			setContentView(R.layout.activity_lost_find);
			
		}else{//还没有做过设置向导
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			finish();
		}
		}
	/**
	 * 重新进入手机防盗设置页面
	 */
	public void reEnterSetup(View view){
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		finish();
 	}
}
