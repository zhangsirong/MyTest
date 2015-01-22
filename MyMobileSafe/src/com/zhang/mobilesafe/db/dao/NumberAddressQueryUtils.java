package com.zhang.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {
	private static String path = "data/data/com.zhang.mobliesafe/files/address.db";
	
	/**
	 * 
	 * @param number
	 * @return
	 */
	public static String queryNumber(String number){
		String address = number;
		//把address.db拷贝到我们的data/data/com.zhang.mobilesafe/files/address.db
		SQLiteDatabase database= SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		
		String sql = "select location from data2 where id=(select outkey from data1 where id=?)";
		Cursor cursor = database.rawQuery(sql, new String[]{number.substring(0,7)});
		while(cursor.moveToNext()){
			String location = cursor.getString(0);
			address = location;
		}
		cursor.close();
		return address;
	}
}
