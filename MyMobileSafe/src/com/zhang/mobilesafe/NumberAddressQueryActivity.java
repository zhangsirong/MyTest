package com.zhang.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.zhang.mobliesafe.R;

public class NumberAddressQueryActivity extends Activity {
	private EditText ed_phone;
	private TextView result;
	private  final static String TAG = "NumberAddressQueryActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address_query);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		result = (TextView) findViewById(R.id.result);
	}
	/**
	 * ��ѯ���������
	 * @param view
	 */
	public void numberAddressQuery(View view){
		String phone = ed_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "����Ϊ��", 0).show();
		}else{
			String address = NumberAddressQueryUtils.queryNumber(phone);
			result.setText(address);
			//ȥ���ݿ��ѯ���������
			//дһ��������ȥ��ѯ���ݿ�
			Log.i(TAG,"��Ҫ��ѯ�ĺ���=="+phone);
		}
	}
}
