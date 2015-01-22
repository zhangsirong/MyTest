package com.zhang.mobilesafe;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.zhang.mobilesafe.db.dao.BlackNumberDao;
import com.zhang.mobilesafe.db.domain.BlackNumberInfo;
import com.zhang.mobliesafe.R;

public class CallSmsSafeActivity extends Activity {
	public static final String TAG = "CallSmsSafeActivity";
	private ListView lv_callsms_safe;
	private BlackNumberDao dao;
	private List<BlackNumberInfo> infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		dao = new BlackNumberDao(this);
		infos = dao.findAll();
		lv_callsms_safe.setAdapter(new CallSmsAdapter());
	}

	private class CallSmsAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Log.i(TAG,"厨房有历史的view对象，复用历史缓存的view对象：");
			View view = View.inflate(getApplicationContext(), R.layout.list_item_callsms, null);			
			TextView tv_black_number = (TextView) view.findViewById(R.id.tv_black_number);
			TextView tv_black_mode = (TextView) view.findViewById(R.id.tv_black_mode);
			tv_black_number.setText(infos.get(position).getNumber());
			
			String mode = infos.get(position).getMode();
			if("1".equals(mode)){
				tv_black_mode.setText("电话拦截");
			}else if("2".equals(mode)){
				tv_black_mode.setText("短信拦截");
			}else{
				tv_black_mode.setText("全部拦截");
			}
			return view;
		}
	}
}
