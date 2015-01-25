package com.zhang.myapple;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity { 
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        GameView view = new GameView(this);
	        setContentView(view);
	        
	}
	
}
