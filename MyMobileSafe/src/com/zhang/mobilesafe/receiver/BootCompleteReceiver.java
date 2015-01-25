package com.zhang.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {
	private SharedPreferences sp;
	private TelephonyManager tm;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		 tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		 boolean protecting = sp.getBoolean("protecting", false);
		 if(protecting){
			 // ��ȡ֮ǰ�����SIM����Ϣ
			 String savesim = sp.getString("sim", "")+"auf";
		
			 //��ȡ��ǰ�����SIM����Ϣ
			 String realsim = tm.getSimSerialNumber();
		
			 //�Ƚ��Ƿ�һ��
			 if(savesim.equals(realsim)){
			//SIMû�б��,ͬһ��
			 }else{
				 //SIM�����,��һ����Ϣ����ȫ����
				 System.out.println("SIM���Ѿ����");
				 Toast.makeText(context,"SIM���Ѿ����", 1).show();
				 SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, "sim changing....", null, null);
			}
		}
	}
}
