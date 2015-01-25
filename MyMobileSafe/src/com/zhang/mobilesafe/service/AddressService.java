package com.zhang.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhang.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.zhang.mobliesafe.R;

public class AddressService extends Service {

	/**
	 * 窗体管理者
	 */
	private WindowManager wm;
	private View view;

	/**
	 * 电话服务
	 */
	private TelephonyManager tm;
	private MyListenerPhone listenerPhone;
	private OutCallReceiver receiver;
	private SharedPreferences sp;

	protected static final String TAG = "AddressService";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// 服务里面的内部类
	// 广播接收者的生命周期和服务一样
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 这就是我们拿到的播出去的电话号码
			String phone = getResultData();
			// 查询数据库
			String address = NumberAddressQueryUtils.queryNumber(phone);

			// Toast.makeText(context, address, 1).show();
			myToast(address);
		}

	}

	private class MyListenerPhone extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state：状态，incomingNumber：来电号码
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 来电铃声响起
				// 查询数据库的操作
				String address = NumberAddressQueryUtils
						.queryNumber(incomingNumber);

				// Toast.makeText(getApplicationContext(), address, 1).show();
				myToast(address);

				break;

			case TelephonyManager.CALL_STATE_IDLE:// 电话的空闲状态：挂电话、来电拒绝
				// 把这个View移除
				if (view != null) {
					wm.removeView(view);
				}

				break;

			default:
				break;
			}
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		// 监听来电
		listenerPhone = new MyListenerPhone();
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);

		// 用代码去注册广播接收者
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);

		// 实例化窗体
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 取消监听来电
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone = null;

		// 用代码取消注册广播接收者
		unregisterReceiver(receiver);
		receiver = null;

	}

	private WindowManager.LayoutParams params;
	long[] mHits = new long[2];

	/**
	 * 自定义土司
	 * 
	 * @param address
	 */
	public void myToast(String address) {
		view = View.inflate(this, R.layout.address_show, null);
		TextView textview = (TextView) view.findViewById(R.id.tv_address);
		// 双击居中
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// 双击居中
					params.x = wm.getDefaultDisplay().getWidth() / 2- view.getWidth() / 2;
					//params.y = wm.getDefaultDisplay().getHeight() / 2- view.getHeight() / 2;
					wm.updateViewLayout(view, params);
					// 记录控件距离屏幕左上角的坐标
					Editor editor = sp.edit();
					editor.putInt("lastx", params.x);
					editor.putInt("lasty", params.y);
					editor.commit();
				}
			}
		});

		// 给View对象设置一个触摸的监听器
		view.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 定义手指初始化位置

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 手指按下屏幕
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					Log.i(TAG, "开始位置" + startX + ":" + startY);
					break;

				case MotionEvent.ACTION_MOVE:// 手指移动

					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					Log.i(TAG, "新的位置" + newX + ":" + newY);
					int dx = newX - startX;
					int dy = newY - startY;
					Log.i(TAG, "偏移量" + dx + ":" + dy);
					Log.i(TAG, "更新ImageView在窗体的位置" + dx + ":" + dy);
					params.x += dx;
					params.y += dy;
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.x > wm.getDefaultDisplay().getWidth()
							- view.getWidth()) {
						params.x = wm.getDefaultDisplay().getWidth()
								- view.getWidth();
					}
					if (params.y > wm.getDefaultDisplay().getHeight()
							- view.getHeight()) {
						params.y = wm.getDefaultDisplay().getHeight()
								- view.getHeight();
					}

					wm.updateViewLayout(view, params);
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_UP:// 手指离开一瞬间
					// 记录控件距离屏幕左上角的坐标
					Editor editor = sp.edit();
					editor.putInt("lastx", params.x);
					editor.putInt("lasty", params.y);
					editor.commit();

					break;
				}
				return true;// 事件处理完毕了
			}
		});

		// "半透明","活力橙","卫士蓝","金属灰","苹果绿"
		int[] ids = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		sp = getSharedPreferences("config", MODE_PRIVATE);
		view.setBackgroundResource(ids[sp.getInt("which", 0)]);
		textview.setText(address);
		// 窗体的参数就设置好了
		params = new WindowManager.LayoutParams();

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		// 与屏幕左上角对齐
		params.gravity = Gravity.TOP + Gravity.LEFT;
		// 距离左边和上边各100像素
		params.x = sp.getInt("lastx", 0);
		params.y = sp.getInt("lastx", 0);

		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		// 具有电话优先级的一种窗体类型,要添加权限
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(view, params);

	}
}
