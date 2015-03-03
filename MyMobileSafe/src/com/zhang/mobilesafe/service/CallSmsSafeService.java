package com.zhang.mobilesafe.service;

import java.lang.reflect.Method;

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
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.zhang.mobilesafe.db.dao.BlackNumberDao;

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
			Log.i(TAG,"我是服务内部的广播接受者，我接受到了 短信到来的广播事件");
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for(Object obj : objs){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				//得到短信发件人
				String sender = smsMessage.getOriginatingAddress();
				String mode = dao.findMode(sender);
				if("2".equals(mode)||"3".equals(mode)){
					Log.i(TAG,"拦截到黑名单短信");
					abortBroadcast();
					//TODO:把这个拦截掉的短信记录下来。
				}
				
				String body = smsMessage.getMessageBody();
				//遍历查询数据库
				if(body.contains("fapiao")){
					Log.i(TAG,"拦截到卖发票的短信"); 
					abortBroadcast();
					//TODO:把这个拦截掉的短信记录下来。
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
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://响铃状态
				String mode = dao.findMode(incomingNumber);
				if("1".equals(mode)||"3".equals(mode)){
					Log.i(TAG,"这是黑名单号码，挂断电话。。。");
					//监视呼叫记录的生成，如果呼叫记录产生了。删除呼叫记录。
					Uri url = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(url, true, new CallLogObserver(new Handler(), incomingNumber));
					endCall();// 立刻把电话挂断了。  但是呼叫记录的生成 并不是一个同步的代码。 是一个异步代码
				}
				break;
			}
		}
	}
	private class CallLogObserver extends ContentObserver{
		private String incomingNumber;
		public CallLogObserver(Handler handler, String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}
		//数据变化会调用onchage方法
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			getContentResolver().unregisterContentObserver(this);
			deleteCallLog(incomingNumber);
		}
	}
	
	public void endCall() {
		//反射方法
		//IBinder iBinder = ServiceManager.getService(TELEPHONY_SERVICE);
		try {
			//加载servicemanager的字节码
			Class<?> clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
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
		Uri url = Uri.parse("content://call_log/calls");
		resolver.delete(url, "number=?", new String[]{incomingNumber});
	}
}
