package com.zhang.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class LostFindActivity extends Activity{
	
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//�ж��Ƿ�������,���û����,��������,�������ڵ�ǰҳ��
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			//�����ֻ�����ҳ��
			setContentView(R.layout.activity_lost_find);
			
		}else{//��û������������
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			finish();
		}
		}
	/**
	 * ���½����ֻ���������ҳ��
	 */
	public void reEnterSetup(View view){
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		finish();
 	}
}
