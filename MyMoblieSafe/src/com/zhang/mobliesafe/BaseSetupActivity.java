package com.zhang.mobliesafe;

import com.zhang.mobliesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	//1.����һ������ʶ����
		private GestureDetector detector;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		
		//2.ʵ��������ʶ����
		detector = new GestureDetector(this, new SimpleOnGestureListener(){
			/**
			 * ��ָ�����滬��ʵ�ֻص�	
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				//������x��������������
				if(Math.abs(velocityX)<200){
					Toast.makeText(getApplicationContext(), "������̫����", 0).show();

				}
				
				//����б���������
				if(Math.abs(e2.getRawY()-e1.getRawY())>200){
					Toast.makeText(getApplicationContext(), "������������", 0).show();
				}
				
				if((e2.getRawX()-e1.getRawX())>200){
					//�����һ���,��ʾ��һ��ҳ��
					System.out.println("�����һ���,��ʾ��һ��ҳ��");
					showPre();
					return true;
				}
				if((e1.getRawX()-e2.getRawX())>200){
					//���ҵ��󻬶�,��ʾ��һ��ҳ��
					System.out.println("���ҵ��󻬶�,��ʾ��һ��ҳ��");
					showNext();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
		}
		
	public abstract void showNext();
	public abstract void showPre();
	/**
	 * ��һ��
	 * @param view
	 */
	public void next(View view){
		showNext();
	}
	/**
	 * ��һ��
	 * @param view
	 */
	public void pre(View view){
		showPre();
	}
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
