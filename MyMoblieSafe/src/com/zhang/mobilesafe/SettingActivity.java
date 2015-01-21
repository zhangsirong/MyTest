package com.zhang.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhang.mobilesafe.ui.SettingItemView;
import com.zhang.mobliesafe.R;

public class SettingActivity extends Activity {
	private SettingItemView siv_update;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
	super.onCreate(savedInstanceState);
	sp = getSharedPreferences("config", MODE_PRIVATE);
	setContentView(R.layout.activity_setting);
	siv_update = (SettingItemView) findViewById(R.id.siv_update);
	
	boolean update = sp.getBoolean("update", false);
	if(update){
		//�Զ������Ѿ�����
		siv_update.setChecked(true);
		siv_update.setDesc("�Զ������Ѿ�����");
	}else{
		//�Զ������Ѿ��ر�
		siv_update.setChecked(false);
		siv_update.setDesc("�Զ������Ѿ��ر�");
	}
	
	siv_update.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Editor editor = sp.edit();
			//�ж��Ƿ�ѡ��
			//�Ѿ����Զ�����
			if(siv_update.isChecked()){
				siv_update.setChecked(false);
				siv_update.setDesc("�Զ������Ѿ��ر�");
				editor.putBoolean("update",false );
			}else{
				//û�д��Զ�����
				siv_update.setChecked(true);
				siv_update.setDesc("�Զ������Ѿ�����");
				editor.putBoolean("update",true);
			}
			editor.commit();
			
		}
	});
	}
}