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
			Log.i(TAG, "�ڲ��㲥������.���ŵ�����");
			//��鷢�����Ƿ��Ǻ���������,���ö�������ȫ������
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for(Object obj:objs){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				//�õ�������
				String sender = smsMessage.getOriginatingAddress();
				String result = dao.findMode(sender);
				if("2".equals(result)||"3".equals(result)){
					Log.i(TAG, "���ض���");
					abortBroadcast();
				}
				
				//��ʾ����
				String body = smsMessage.getMessageBody();
				if(body.contains("��Ʊ")){
					Log.i(TAG, "���ط�Ʊ��Ϣ");
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
			case TelephonyManager.CALL_STATE_RINGING://����״̬��
				String result = dao.findMode(incomingNumber);
				if("1".equals(result)||"3".equals(result)){
					Log.i(TAG,"�Ҷϵ绰��������");
					//�۲���м�¼���ݿ����ݵı仯
					Uri uri =Uri.parse("context://call_log/calls");
					getContentResolver().registerContentObserver(uri, true, new CallLogObserver(incomingNumber, new Handler()));
					endCall();
					//ɾ�����м�¼
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
			Log.i(TAG, "���ݿ�����ݷ����仯��");
			getContentResolver().unregisterContentObserver(this);
			super.onChange(selfChange);
			deleteCallLog(incomingNumber);
		}
	}
	
	public void endCall() {
		//���䷽��
		//IBinder iBinder = ServiceManager.getService(TELEPHONY_SERVICE);
		try {
			//����servicemanager���ֽ���
			Class clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(ibinder).endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ���������ṩ��ɾ�����м�¼
	 */
	public void deleteCallLog(String incomingNumber){
		ContentResolver resolver = getContentResolver();
		Uri uri =Uri.parse("context://call_log/calls");
		resolver.delete(uri, "number=?", new String[]{incomingNumber});
	}
}
