package com.artwl.update.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.artwl.update.UpdateChecker;

public class MainActivity extends ActionBarActivity {

    protected static final String APP_UPDATE_SERVER_URL = "http://APP_UPDATE_SERVER_URL";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btnDialog).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateChecker.checkForDialog(MainActivity.this, APP_UPDATE_SERVER_URL, false);
			}
		});

		findViewById(R.id.btnDialogAutoInstall).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateChecker.checkForDialog(MainActivity.this, APP_UPDATE_SERVER_URL, true);
			}
		});

		findViewById(R.id.btnNotify).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateChecker.checkForNotification(MainActivity.this,APP_UPDATE_SERVER_URL, false);
			}
		});

		findViewById(R.id.btnNotifyAutoInstall).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateChecker.checkForNotification(MainActivity.this,APP_UPDATE_SERVER_URL, true);
			}
		});

	}

}
