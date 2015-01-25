package com.zhang.myapple;

import com.example.myapple.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	private Button startButton;
	private Button exitButton;
	private Button helpButton;
	private ToggleButton musicButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		startButton = (Button) findViewById(R.id.start);
		exitButton = (Button) findViewById(R.id.exit);
		helpButton = (Button) findViewById(R.id.help);
		musicButton = (ToggleButton) findViewById(R.id.music);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						GameActivity.class);
				startActivity(intent);
			}
		});

		exitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				simple(v);
			}
		});

		helpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						HelpActivity.class);
				startActivity(intent);
			}
		});
		musicButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Intent Serviceintent = new Intent("com.zhang.myapple.MUSIC");
				if (isChecked) {
					startService(Serviceintent);
				} else {
					stopService(Serviceintent);
				}
			}
		});

	}

	private void simple(View source) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setMessage("是否退出游戏?");
		setPositiveButton(builder);
		setNegativeButon(builder).create().show();
	}

	private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
		return builder.setPositiveButton("是", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		});
	}

	private AlertDialog.Builder setNegativeButon(AlertDialog.Builder builder) {
		return builder.setNegativeButton("否", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
	}
}