package com.zhang.mobilesafe.utils;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

/**
 * ���ŵĹ�����
 * 
 * @author hp
 *
 */
public class SmsUtils {
	/**
	 * ���ݶ��ŵĻص��ӿ�
	 * 
	 * @author hp
	 *
	 */
	public interface BackUpCallback {
		/**
		 * ��ʼ���ݵ�ʱ���������ֵ
		 * 
		 * @param max
		 */
		public void beforBackup(int max);

		/**
		 * ���ݹ��������ӽ���
		 * 
		 * @param progress
		 */
		public void onSmsBackup(int progress);

	}

	/**
	 * �����û��Ķ���
	 * 
	 * @param context������
	 * @param BackUpCallback
	 *            ���ݶ��ŵĽӿ�
	 * @throws Exception
	 */
	public static void backupSms(Context context, BackUpCallback backUpCallback)
			throws Exception {
		ContentResolver resolver = context.getContentResolver();
		File file = new File(Environment.getExternalStorageDirectory(),
				"backup.xml");
		System.out.println(file.toString());
		FileOutputStream fos = new FileOutputStream(file);
		// ���û��Ķ���һ��һ��������,����һ����ʽд���ļ���
		XmlSerializer serializer = Xml.newSerializer();// ��ȡxml�ļ���������
		serializer.setOutput(fos, "utf-8");
		serializer.startDocument("utf-8", true);// ������xml�ļ�
		serializer.startTag(null, "smss");
		Uri uri = Uri.parse("content://sms/");

		Cursor cursor = resolver.query(uri, new String[] { "body", "address",
				"type", "date" }, null, null, null);
		// ��ʼ���ݶ��ŵĽ��������ֵ
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
			// ���ݹ��������ӽ���
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
	 * ��ԭ�û��Ķ���
	 * 
	 * @param context������
	 * @param flag�Ƿ�����ԭ���Ķ���
	 */
	public static void restoreSms(Context context, boolean flag) {
		Uri uri = Uri.parse("content://sms/");
		if (flag) {
			context.getContentResolver().delete(uri, null, null);
		}

		// 1.��ȡsd����xml�ļ�
		// Xml.newPullParser();

		// 2��ȡmax

		// 3.��ȡÿһ��������Ϣ,body,date,type,address

		// 4�Ѷ��Ų��뵽ϵͳӦ����

		ContentValues values = new ContentValues();
		values.put("body", "��������");
		values.put("date", "1434567890123");
		values.put("type", "1");
		values.put("address", "333333");
		context.getContentResolver().insert(uri, values);
	}
}
