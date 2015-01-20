package com.zhang.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

public class Setup4Activity extends Activity {
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		sp = getSharedPreferences("config",MODE_PRIVATE);
	}
	/**
	 * 下一步
	 * @param view
	 */
	public void next(View view){
		Editor editor = sp.edit();
		editor.putBoolean("config", false);
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		finish();
	}
	/**
	 * 上一步
	 * @param view
	 */
	public void pre(View view){
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
	}
}
