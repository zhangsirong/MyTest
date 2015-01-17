package com.zhang.myapple;

import java.util.ArrayList;
import java.util.Random;
import com.example.myapple.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GameView extends View {
	private MyTextView mText = new MyTextView();
	private ArrayList<Fruit> mSpiriteList = new ArrayList<Fruit>();//水果精灵数组
	private ArrayList<Fruit> mSpiriteListP1 = new ArrayList<Fruit>();//左边精灵数组
	private ArrayList<Fruit> mSpiriteListP2 = new ArrayList<Fruit>();//右边精灵数组
	private ArrayList<Fruit> mSpiriteListP3 = new ArrayList<Fruit>();//切开炸弹后爆炸画面
	private Bitmap mBomb, mApple, mAppleP1, mAppleP2, mOrange, mOrangeP1,
			mOrangeP2, mPapaya, mPapayaP1, mPapayaP2, mPeach, mPeachP1,
			mPeachP2;
	private Random mRan = new Random();
	private Counter mGenCounter = new Counter();
	private int noTime = 0;//时间还没到0
	private int ScoreNum = 0;//初始为0分
	private int cutBombNum = 3;// 3次生命

	public GameView(Context context) {
		super(context);

		mBomb = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
		mApple = BitmapFactory.decodeResource(getResources(), R.drawable.apple);
		mAppleP1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.applep1);
		mAppleP2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.applep2);
		mOrange = BitmapFactory.decodeResource(getResources(),
				R.drawable.orange);
		mOrangeP1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.orangep1);
		mOrangeP2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.orangep2);

		mPapaya = BitmapFactory.decodeResource(getResources(),
				R.drawable.papaya);
		mPapayaP1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.papayap1);
		mPapayaP2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.papayap2);

		mPeach = BitmapFactory.decodeResource(getResources(), R.drawable.peach);
		mPeachP1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.peachp1);
		mPeachP2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.peachp2);

		setBackgroundResource(R.drawable.background);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		mText.check();
		mText.drawTime(canvas);
		mText.drawScore(canvas);
		mText.drawLife(canvas);
		genSpirites();
		moveSpiriteList(mSpiriteList);
		drawSpiriteList(canvas, mSpiriteList);
		moveSpiriteList(mSpiriteListP1);
		drawSpiriteList(canvas, mSpiriteListP1);
		moveSpiriteList(mSpiriteListP2);
		drawSpiriteList(canvas, mSpiriteListP2);
		drawSpiriteList3(canvas, mSpiriteListP3);
		invalidate();
	}

	private void genSpirites() {
		int interval = mRan.nextInt(2000) + 1000;
		mGenCounter.setInterval(interval);
		if (mGenCounter.isTimeUp()) {
			if (mText.getmSeconds() > 0) {
				int h = 1280;
				int a = mRan.nextInt(200) - 100;
				int b = mRan.nextInt(200) - 100;
				int c = mRan.nextInt(200) - 100;
				int d = mRan.nextInt(200) - 100;
				int e = mRan.nextInt(200) - 100;
				Fruit spirite1 = new Fruit();
				spirite1.setPos(a, h);
				spirite1.setImages(mApple);
				spirite1.setV(6, -45);
				spirite1.setfruit(1);
				mSpiriteList.add(spirite1);

				Fruit spirite2 = new Fruit();
				spirite2.setPos(b, h);
				spirite2.setfruit(2);
				spirite2.setImages(mOrange);
				spirite2.setV(6, -50);
				mSpiriteList.add(spirite2);

				Fruit spirite3 = new Fruit();
				spirite3.setPos(c, h);
				spirite3.setImages(mPapaya);
				spirite3.setfruit(3);
				spirite3.setV(7, -35);
				mSpiriteList.add(spirite3);

				Fruit spirite4 = new Fruit();
				spirite4.setPos(d, h);
				spirite4.setfruit(4);
				spirite4.setImages(mPeach);
				spirite4.setV(8, -44);
				mSpiriteList.add(spirite4);

				int mBombRandom = mRan.nextInt(2);
				if (mBombRandom == 1) {
					Fruit spirite5 = new Fruit();
					spirite5.setPos(e, h);
					spirite5.setfruit(5);
					spirite5.setImages(mBomb);
					spirite5.setV(6, -45);
					mSpiriteList.add(spirite5);
				}

			} else {
				if (noTime == 0) {//时间是否为0
					showToast("时间到!!!");
					noTime++;
				}
				new Point(200, 400);
				Fruit p3 = new Fruit();
				mSpiriteListP3.add(p3);
			}
		}
	}

	private void moveSpiriteList(ArrayList<Fruit> list) {
		for (Fruit sp : list) {
			sp.move();
		}
	}

	private void drawSpiriteList(Canvas canvas, ArrayList<Fruit> list) {
		for (Fruit spirite : list) {
			spirite.draw(canvas);

		}
	}

	private void drawSpiriteList3(Canvas canvas, ArrayList<Fruit> list) {
		for (Fruit spirite : list) {
			if (mText.getmSeconds() > 0) {
				spirite.draw3Lines(canvas);
			}
			spirite.ScreenTurnWhite(canvas);
		}
	}

	private void hanglecut(MotionEvent event) {
		for (int i = 0; i < mSpiriteList.size(); i++) {
			Fruit spirite = mSpiriteList.get(i);
			if (spirite.isMove(event)) {
				Point pos = spirite.getPos();
				Fruit p1 = new Fruit();
				p1.setPos(pos.x, pos.y);
				p1.setV(-6, -3);

				Fruit p2 = new Fruit();
				p2.setPos(pos.x, pos.y);
				p2.setV(6, -3);

				switch (spirite.getfruit()) {
				case 1:
					p1.setImages(mAppleP1);
					mSpiriteListP1.add(p1);
					p2.setImages(mAppleP2);
					mSpiriteListP2.add(p2);
					mText.setScoreNum(++ScoreNum);
					break;
				case 2:
					p1.setImages(mOrangeP1);
					mSpiriteListP1.add(p1);
					p2.setImages(mOrangeP2);
					mSpiriteListP2.add(p2);
					mText.setScoreNum(++ScoreNum);
					break;
				case 3:
					p1.setImages(mPapayaP1);
					mSpiriteListP1.add(p1);
					p2.setImages(mPapayaP2);
					mSpiriteListP2.add(p2);
					mText.setScoreNum(++ScoreNum);
					break;
				case 4:
					p1.setImages(mPeachP1);
					mSpiriteListP1.add(p1);
					p2.setImages(mPeachP2);
					mSpiriteListP2.add(p2);
					mText.setScoreNum(++ScoreNum);
					break;
				case 5:
					cutBombNum -= 1;
					mText.setCutBombNum(cutBombNum);
					if (cutBombNum == 0) {//是否切了3次炸弹
						noTime++;
						Fruit p3 = new Fruit();
						p3.setPos(pos.x, pos.y);
						p3.setV(0, 0);
						p3.setA(0, 0);
						p3.setImages(mBomb);
						mSpiriteListP3.add(p3);
						showToast("游戏结束!!!");
					} else {
						showToast("生命-1!");
					}
					break;
				}

				mSpiriteList.remove(spirite);
				i--;
			}
		}
	}

	private void showToast(String msg) {
		Toast toast = new Toast(getContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		TextView textView = new TextView(getContext());
		textView.setText(msg);
		textView.setTextSize(50);
		textView.setTextColor(Color.RED);
		toast.setView(textView);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		hanglecut(event);
		return true;
	}
}
