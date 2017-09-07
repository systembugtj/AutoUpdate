package com.artwl.update.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.artwl.update.Constants;
import com.artwl.update.UpdateChecker;
import com.artwl.update.UpdateDialog;
import com.artwl.update.UpdateNotice;
import com.artwl.update.entity.UpdateDescription;

public class MainActivity extends AppCompatActivity implements UpdateNotice {

    protected static final String APP_UPDATE_SERVER_URL = "http://cdn.kandaping.com/hjb/update.json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        final UpdateNotice notice = this;

		findViewById(R.id.btnDialog).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateChecker.checkForDialog(MainActivity.this, APP_UPDATE_SERVER_URL, false, true);
			}
		});

		findViewById(R.id.btnDialogAutoInstall).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateChecker.checkForDialog(MainActivity.this, APP_UPDATE_SERVER_URL, true, true);
			}
		});

		findViewById(R.id.btnNotify).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateChecker.checkForNotification(MainActivity.this,APP_UPDATE_SERVER_URL, false, false);
			}
		});

		findViewById(R.id.btnNotifyAutoInstall).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateChecker.checkForNotification(MainActivity.this,APP_UPDATE_SERVER_URL, true, true);
			}
		});

		findViewById(R.id.btnCustomNotice).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateChecker.checkForCustomNotice(MainActivity.this,APP_UPDATE_SERVER_URL, notice);
			}
		});
	}

	@Override
	public void showCustomNotice(UpdateDescription description) {
        UpdateDialog d = new UpdateDialog();
        Bundle args = new Bundle();
        args.putString(Constants.APK_UPDATE_CONTENT, description.updateMessage);
        args.putString(Constants.APK_DOWNLOAD_URL, description.url);
        args.putBoolean(Constants.APK_IS_AUTO_INSTALL, true);
        args.putBoolean(Constants.APK_CHECK_EXTERNAL, true);
        d.setArguments(args);

        // http://blog.csdn.net/chenshufei2/article/details/48747149
        // Don't use default d.show(mContext.getSupportFragmentManager(), null);
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(d, this.getClass().getSimpleName());
        ft.commitAllowingStateLoss();//注意这里使用commitAllowingStateLoss()
	}
}
