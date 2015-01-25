package com.zhang.myapple;

import com.example.myapple.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;



public class MusicServer extends Service {
	private MediaPlayer mediaPlayer;

	@Override
	public void onCreate() {
		super.onCreate();
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(this, R.raw.apple);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mediaPlayer.stop();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}

