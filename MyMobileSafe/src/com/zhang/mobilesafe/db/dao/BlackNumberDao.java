package com.zhang.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhang.mobilesafe.db.BlackNumberDBOpenHelper;
import com.zhang.mobilesafe.db.domain.BlackNumberInfo;

public class BlackNumberDao {
	private BlackNumberDBOpenHelper helper;

	/**
	 * ���췽��
	 * 
	 * @param context������
	 */
	public  BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);

	}

	/**
	 * ��ѯ�����������Ƿ����
	 * 
	 * @param number
	 * @return
	 */
	public boolean find(String number) {
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber where number=?",
				new String[] { number });
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * ��ѯȫ������������
	 * @return
	 */
	public List<BlackNumberInfo> findAll(){
		List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc", null);
		while(cursor.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			info.setMode(mode);
			info.setNumber(number);
			result.add(info);
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * ��Ӻ���������
	 * @param number����������
	 * @param mode����ģʽ1���غ��� 2���ض��� 3����ȫ��
	 */
	public void add(String number,String mode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		db.insert("blacknumber", null, values);
		db.close();
	}
	/**
	 * �޸ĺ��������������ģʽ
	 * @param number����������
	 * @param newmode����ģʽ 1���غ��� 2���ض��� 3����ȫ��
	 */
	public void update(String number,String newmode){
		
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		db.update("blacknumber", values, "number=?", new String[]{number});
		db.close();
	}
	/**
	 * ɾ������������
	 * @param number����������
	 */
	public void delete(String number){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacknumber", "number=?", new String[] {number});
		db.close();
	}
}
