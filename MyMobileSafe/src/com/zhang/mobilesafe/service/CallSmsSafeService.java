package com.zhang.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.zhang.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallSmsSafeService extends Service {
	private InnerSmsReceiver receiver;
	public static final String TAG = "CallSmsSafeService";
	private BlackNumberDao dao;
	private TelephonyManager tm;
	private MyListener listener;
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	private class InnerSmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "内部广播接收者.短信到来了");
			//检查发件人是否是黑名单号码,设置短信拦截全部拦截
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for(Object obj:objs){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				//得到发件人
				String sender = smsMessage.getOriginatingAddress();
				String result = dao.findMode(sender);
				if("2".equals(result)||"3".equals(result)){
					Log.i(TAG, "拦截短信");
					abortBroadcast();
				}
				
				//演示代码
				String body = smsMessage.getMessageBody();
				if(body.contains("发票")){
					Log.i(TAG, "拦截发票信息");
				}
			}
		}
	}
	@Override
	public void onCreate() {
		dao = new BlackNumberDao(this);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		receiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, filter);
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
		receiver=null;
	}
	private class MyListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://零响状态。
				String result = dao.findMode(incomingNumber);
				if("1".equals(result)||"3".equals(result)){
					Log.i(TAG,"挂断电话。。。。");
					//观察呼叫记录数据库内容的变化
					Uri uri =Uri.parse("context://call_log/calls");
					getContentResolver().registerContentObserver(uri, true, new CallLogObserver(incomingNumber, new Handler()));
					endCall();
					//删除呼叫记录
					//deleteCallLog(incomingNumber);
				}
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}
	private class CallLogObserver extends ContentObserver{
		private String incomingNumber;
		public CallLogObserver(String incomingNumber,Handler handler) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}
		@Override
		public void onChange(boolean selfChange) {
			Log.i(TAG, "数据库的内容发生变化了");
			getContentResolver().unregisterContentObserver(this);
			super.onChange(selfChange);
			deleteCallLog(incomingNumber);
		}
	}
	
	public void endCall() {
		//反射方法
		//IBinder iBinder = ServiceManager.getService(TELEPHONY_SERVICE);
		try {
			//加载servicemanager的字节码
			Class clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(ibinder).endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 利用内容提供者删除呼叫记录
	 */
	public void deleteCallLog(String incomingNumber){
		ContentResolver resolver = getContentResolver();
		Uri uri =Uri.parse("context://call_log/calls");
		resolver.delete(uri, "number=?", new String[]{incomingNumber});
	}
}
