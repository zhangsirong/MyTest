package com.zhang.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.zhang.mobilesafe.db.BlackNumberDBOpenHelper;
import com.zhang.mobilesafe.domain.BlackNumberInfo;

/**
 * �������ݿ�Ĳ�ѯҵ����
 * 
 * @author hp
 *
 */
public class AntivirsuDao {
	/**
	 * ��ѯһ��md5�Ƿ��ڲ������ݿ����
	 * 
	 * @param md5
	 * @return
	 */
	public static boolean isVirus(String md5) {
		boolean result = false;
		String path = "/data/data/com.zhang.mobliesafe/files/antivirus.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select * from datable where md5=?",
				new String[] {"e1e7344eef6a645fdb6fc66f42718d86"});
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
}