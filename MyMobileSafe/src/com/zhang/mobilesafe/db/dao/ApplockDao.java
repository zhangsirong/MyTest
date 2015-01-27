package com.zhang.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhang.mobilesafe.db.ApplockDBOpenHelper;

/**
 * 程序锁的dao
 * @author hp
 *
 */
public class ApplockDao {
	private ApplockDBOpenHelper helper;
	private Context context;
	/**
	 * 构造方法
	 * @param context 上下文
	 */
	public ApplockDao(Context context){
		helper = new ApplockDBOpenHelper(context);	
		this.context = context;
	}
	/**
	 * 添加要锁定应用程序的包名
	 * @param packname
	 */
	public void add(String packname){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		db.insert("applock", null, values);
		db.close();
		Intent intent = new Intent();
		intent.setAction("com.zhang.mobilesafe.applockchange");
		context.sendBroadcast(intent);
		//context.getContentResolver().notifyChange(uri, observer);
		
	}
	
	/**
	 * 删除一个要锁定应用程序的包名
	 * @param packname
	 */
	public void delete(String packname){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("applock","packname=?",new String[]{packname});
		db.close();
		Intent intent = new Intent();
		intent.setAction("com.zhang.mobilesafe.applockchange");
		context.sendBroadcast(intent);
	}
	
	/**
	 * 查询一个程序锁包名是否存在
	 * @param packname
	 */
	public boolean find(String packname){
		boolean result = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("applock",null,"packname=?",new String[]{packname},null,null,null);
		if(cursor.moveToNext()){
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * 查询全部的包名
	 * @return
	 */
	public List<String> findAll() {
		List<String> protectPacknames = new ArrayList<String>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("applock",new String[]{"packname"},null,null,null,null,null,null);
		if(cursor.moveToNext()){
			protectPacknames.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return protectPacknames;
	}
}
