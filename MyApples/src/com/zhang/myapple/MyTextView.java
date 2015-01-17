package com.zhang.myapple;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class MyTextView {
	private Counter mCounter = new Counter();
	private int mSeconds = 60;
	private Paint mPaint = new Paint();
	private int scoreNum = 0;
	private int cutBombNum = 3;

	public int getmSeconds() {
		return mSeconds;
	}

	public MyTextView() {
		mPaint.setColor(Color.YELLOW);
		mPaint.setTextSize(50);
		mPaint.setStyle(Style.FILL);
	}

	public void check() {
		if (mCounter.isTimeUp()) {
			mSeconds--;
		}
	}

	public void drawTime(Canvas canvas) {
		if (mSeconds > 59) {
			canvas.drawText("时间还有:" + mSeconds / 60 + "分"
					+ (mSeconds - 60 * (mSeconds / 60)) + "秒", 0, 80, mPaint);
		} else if (mSeconds > 0) {
			canvas.drawText("时间还有:" + mSeconds + "秒", 0, 80, mPaint);
		} else {
			canvas.drawText("时间还有:0秒", 0, 80, mPaint);
		}
	}

	public void setScoreNum(int num) {
		this.scoreNum = num;
	}

	public void drawScore(Canvas canvas) {
		canvas.drawText("分数:" + scoreNum, 0, 140, mPaint);
	}

	public void setCutBombNum(int cutBombNum) {
		this.cutBombNum = cutBombNum;
	}

	public void drawLife(Canvas canvas) {
		canvas.drawText("生命:" + cutBombNum, 0, 200, mPaint);
	}
}
