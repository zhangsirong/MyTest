package com.zhang.myapple;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Fruit {
	private int fruit;//水果总类
	private Point mPos = new Point();
	private Paint mPaint = new Paint();
	private int alpha = 0x00ffffff;//透明度初始值
	private int length = 2000;//线条长度
	private int ang = 360;//旋转角度
	private int stop1X, stop1Y, stop2X, stop2Y, stop3X, stop3Y;
	private Point mSize = new Point();
	private Point mV = new Point();
	private Point mA = new Point(0, 1);
	private Bitmap mImageUp;
	private boolean mIsUp = true;


	public void setImages(Bitmap up) {
		mImageUp = up;
		mSize.x = mImageUp.getWidth();
		mSize.y = mImageUp.getHeight();
	}
	public Bitmap getImages() {
		return mImageUp;
	}

	public void setPos(int x, int y) {
		mPos.x = x;
		mPos.y = y;
	}

	public Point getPos() {
		Point pos = new Point(mPos.x, mPos.y);
		return pos;
	}

	public void move() {
		mPos.x += mV.x;
		mPos.y += mV.y;
		mV.x += mA.x;
		mV.y += mA.y;
	}

	public void setA(int x, int y) {
		mA.x = x;
		mA.y = y;
	}

	public void setV(int x, int y) {
		mV.x = x;
		mV.y = y;
	}

	public void setfruit(int fruit) {
		this.fruit = fruit;
	}

	public int getfruit() {
		return fruit;
	}

	//画3条白线
	public void draw3Lines(Canvas canvas) {
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeWidth(50);

		stop1X = (int) (length * Math.cos(ang));
		stop1Y = (int) (length * Math.sin(ang));
		stop2X = (int) (length * Math.cos(ang + 30));
		stop2Y = (int) (length * Math.sin(ang + 30));
		stop3X = (int) (length * Math.cos(ang - 60));
		stop3Y = (int) (length * Math.sin(ang - 60));
		ang = ang - 6;
		canvas.drawLine(mPos.x, mPos.y, stop1X, stop1Y, mPaint);
		canvas.drawLine(mPos.x, mPos.y, stop2X, stop2Y, mPaint);
		canvas.drawLine(mPos.x, mPos.y, stop3X, stop3Y, mPaint);
		if (ang <= 0) {
			ang = 360;
		}

	}
	//屏幕变白
	public void ScreenTurnWhite(Canvas canvas) {
		canvas.drawColor(alpha);
		if (alpha != 0xFEFFFFFF)
			alpha = alpha + 0x01000000;
	}
	
	public boolean isMove(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			Rect r = new Rect(mPos.x, mPos.y, mPos.x + mSize.x, mPos.y
					+ mSize.y);
			if (r.contains(x, y)) {
				mIsUp = !mIsUp;
				return true;
			}
		}
		return false;
	}

	public void draw(Canvas canvas) {
		Bitmap image = mImageUp;
		canvas.drawBitmap(image, mPos.x, mPos.y, null);
	}
}
