package com.zhang.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

/**
 * 短信的工具类
 * 
 * @author hp
 *
 */
public class SmsUtils {
	/**
	 * 备份短信的回调接口
	 * 
	 * @author hp
	 *
	 */
	public interface BackUpCallback {
		/**
		 * 开始备份的时候设置最大值
		 * 
		 * @param max
		 */
		public void beforBackup(int max);

		/**
		 * 备份过程中增加进度
		 * 
		 * @param progress
		 */
		public void onSmsBackup(int progress);

	}

	/**
	 * 备份用户的短信
	 * 
	 * @param context上下文
	 * @param BackUpCallback
	 *            备份短信的接口
	 * @throws Exception
	 */
	public static void backupSms(Context context, BackUpCallback backUpCallback)
			throws Exception {
		ContentResolver resolver = context.getContentResolver();
		File file = new File(Environment.getExternalStorageDirectory(),
				"backup.xml");
		System.out.println(file.toString());
		FileOutputStream fos = new FileOutputStream(file);
		// 把用户的短信一条一条读出来,按照一定格式写到文件里
		XmlSerializer serializer = Xml.newSerializer();// 获取xml文件的生成器
		serializer.setOutput(fos, "utf-8");
		serializer.startDocument("utf-8", true);// 独立的xml文件
		serializer.startTag(null, "smss");
		Uri uri = Uri.parse("content://sms/");

		Cursor cursor = resolver.query(uri, new String[] { "body", "address",
				"type", "date" }, null, null, null);
		// 开始备份短信的进度条最大值
		int max = cursor.getCount();

		serializer.attribute(null, "max", max + "");
		int process = 0;
		// pd.setMax(max);
		backUpCallback.beforBackup(max);
		while (cursor.moveToNext()) {
			Thread.sleep(50);
			String body = cursor.getString(0);
			String address = cursor.getString(1);
			String type = cursor.getString(2);
			String date = cursor.getString(3);

			serializer.startTag(null, "sms");

			serializer.startTag(null, "body");
			serializer.text(body);
			serializer.endTag(null, "body");

			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");

			serializer.startTag(null, "type");
			serializer.text(type);
			serializer.endTag(null, "type");

			serializer.startTag(null, "date");
			serializer.text(date);
			serializer.endTag(null, "date");

			serializer.endTag(null, "sms");
			// 备份过程中增加进度
			process++;
			// pd.setProgress(process);
			backUpCallback.onSmsBackup(process);

		}
		cursor.close();
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
	}

	/**
	 * 还原用户的短信
	 * 
	 * @param context上下文
	 * @param flag是否清理原来的短信
	 */
	public static void restoreSms(Context context, boolean flag) {
		Uri uri = Uri.parse("content://sms/");
		if (flag) {
			context.getContentResolver().delete(uri, null, null);
		}

		// 1.读取sd卡的xml文件
		// Xml.newPullParser();

		// 2读取max

		// 3.读取每一条短信信息,body,date,type,address

		// 4把短信插入到系统应用中

		
		ContentValues values = new ContentValues();
//		System.out.println("时间在"+System.currentTimeMillis());
//		values.put("body", "短信内容");
//		values.put("date", "1424345577787");
//		values.put("type", "1");
//		values.put("address", "333333");
//		context.getContentResolver().insert(uri, values);
//		
		try {
			File path = new File(Environment.getExternalStorageDirectory(), "backup.xml");
			FileInputStream fis = new FileInputStream(path);
			
			// 获得pull解析器对象
			XmlPullParser parser = Xml.newPullParser();
			// 指定解析的文件和编码格式
			parser.setInput(fis, "utf-8");
			
			int eventType = parser.getEventType(); 		// 获得事件类型
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();	// 获得当前节点的名称
				
				switch (eventType) {
				case XmlPullParser.START_TAG: 		// 当前等于开始节点  <smss>
					if("smss".equals(tagName)) {	// <persons>
						int max = Integer.valueOf(parser.getAttributeValue(null, "max"));
					} else if("body".equals(tagName)) { // <body id="1">
						values.put("body", parser.nextText());
					} else if("address".equals(tagName)) { // <address>
						values.put("address", parser.nextText());
					} else if("type".equals(tagName)) { // <type>
						values.put("type", parser.nextText());
					}
					else if("date".equals(tagName)) { // <date>
						values.put("date", parser.nextText());
						context.getContentResolver().insert(uri, values);
					}
					break;
				default:
					break;
				}
				eventType = parser.next();		// 获得下一个事件类型
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
