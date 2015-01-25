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
			 // 读取之前保存的SIM卡信息
			 String savesim = sp.getString("sim", "")+"auf";
		
			 //读取当前保存的SIM卡信息
			 String realsim = tm.getSimSerialNumber();
		
			 //比较是否一样
			 if(savesim.equals(realsim)){
			//SIM没有变更,同一人
			 }else{
				 //SIM卡变更,发一个信息给安全号码
				 System.out.println("SIM卡已经变更");
				 Toast.makeText(context,"SIM卡已经变更", 1).show();
				 SmsManager.getDefault().sendTextMessage(sp.getString("safenumber", ""), null, "sim changing....", null, null);
			}
		}
	}
}
