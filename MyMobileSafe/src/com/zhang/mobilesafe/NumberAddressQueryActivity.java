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
	 * 查询号码归属地
	 * @param view
	 */
	public void numberAddressQuery(View view){
		String phone = ed_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "号码为空", 0).show();
		}else{
			String address = NumberAddressQueryUtils.queryNumber(phone);
			result.setText(address);
			//去数据库查询号码归属地
			//写一个工具类去查询数据库
			Log.i(TAG,"你要查询的号码=="+phone);
		}
	}
}
