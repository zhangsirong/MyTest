package com.zhang.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.zhang.mobliesafe.R;

public class NumberAddressQueryActivity extends Activity {
	private EditText ed_phone;
	private TextView result;
	/**
	 * ϵͳ�ṩ���񶯷���
	 */
	private Vibrator vibrator;
	private  final static String TAG = "NumberAddressQueryActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address_query);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		result = (TextView) findViewById(R.id.result);
		ed_phone.addTextChangedListener(new TextWatcher() {
			/**
			 * ���ı������仯��ʱ��ص�
			 */
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()>=3){
					//��ѯ���ݿ�,������ʾ���
					String address = NumberAddressQueryUtils.queryNumber(s.toString());
					result.setText(address); 
				}
			}
			/**
			 * ���ı������仯֮ǰ��ʱ��ص�
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * ��ѯ���������
	 * @param view
	 */
	public void numberAddressQuery(View view){
		String phone = ed_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "����Ϊ��", 0).show();
			 Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			 ed_phone.startAnimation(shake);
			 
			 //���绰����Ϊ�յ�ʱ�򣬾�ȥ���ֻ������û�
//			 vibrator.vibrate(2000);
			 long[] pattern = {200,200,300,300,1000,2000};
//			 //-1���ظ� 0ѭ���� 1��
			 vibrator.vibrate(pattern, -1);
			 
			return;
		}else{
			//ȥ���ݿ��ѯ���������
			//дһ��������ȥ��ѯ���ݿ�
			String address = NumberAddressQueryUtils.queryNumber(phone);
			result.setText(address);
			Log.i(TAG,"��Ҫ��ѯ�ĺ���=="+phone);
		}
	}
}
