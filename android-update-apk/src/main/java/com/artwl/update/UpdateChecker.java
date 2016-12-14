package com.artwl.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.artwl.update.entity.UpdateDescription;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateChecker extends Fragment {

    private static final String NOTICE_TYPE_KEY = "type";
    private static final String APP_UPDATE_SERVER_URL = "app_update_server_url";
    private static final String APK_IS_AUTO_INSTALL = "apk_is_auto_install";
    private static final int NOTICE_NOTIFICATION = 2;
    private static final int NOTICE_DIALOG = 1;
    private static final String TAG = "UpdateChecker";
    private static final String HTTP_VERB = "httpverb";

    private FragmentActivity mContext;
    private Thread mThread;
    private int mTypeOfNotice;
    private boolean mIsAutoInstall;
    private String mHttpVerb;

    public static void checkForDialog(FragmentActivity fragmentActivity,
                                      String checkUpdateServerUrl,
                                      boolean isAutoInstall) {
        checkForDialog(fragmentActivity, checkUpdateServerUrl, isAutoInstall, "GET");
    }

    /**
     * Show a Dialog if an update is available for download. Callable in a
     * FragmentActivity. Number of checks after the dialog will be shown:
     * default, 5
     *
     * @param fragmentActivity Required.
     */
    public static void checkForDialog(FragmentActivity fragmentActivity,
                                      String checkUpdateServerUrl,
                                      boolean isAutoInstall, String httpVerb) {
        checkForAutoUpdate(fragmentActivity, checkUpdateServerUrl, isAutoInstall, httpVerb, NOTICE_DIALOG);
    }


    /**
     * Show a Notification if an update is available for download. Callable in a
     * FragmentActivity Specify the number of checks after the notification will
     * be shown.
     *
     * @param fragmentActivity Required.
     */
    public static void checkForNotification(FragmentActivity fragmentActivity,
                                            String checkUpdateServerUrl,
                                            boolean isAutoInstall) {
        checkForNotification(fragmentActivity, checkUpdateServerUrl, isAutoInstall, "GET");
    }

    /**
     * Show a Notification if an update is available for download. Callable in a
     * FragmentActivity Specify the number of checks after the notification will
     * be shown.
     *
     * @param fragmentActivity Required.
     */
    public static void checkForNotification(FragmentActivity fragmentActivity,
                                            String checkUpdateServerUrl,
                                            boolean isAutoInstall, String httpVerb) {
        checkForAutoUpdate(fragmentActivity, checkUpdateServerUrl, isAutoInstall, httpVerb, NOTICE_NOTIFICATION);
    }

    public static void checkForAutoUpdate(FragmentActivity fragmentActivity,
                                          String checkUpdateServerUrl,
                                          boolean isAutoInstall,
                                          String httpVerb,
                                          int typeOfNotice) {
        FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putInt(NOTICE_TYPE_KEY, typeOfNotice);
        args.putString(APP_UPDATE_SERVER_URL, checkUpdateServerUrl);
        args.putBoolean(APK_IS_AUTO_INSTALL, isAutoInstall);
        args.putString(HTTP_VERB, httpVerb);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }


    /**
     * This class is a Fragment. Check for the method you have chosen.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (FragmentActivity) activity;
        Bundle args = getArguments();
        mTypeOfNotice = args.getInt(NOTICE_TYPE_KEY);
        mIsAutoInstall = args.getBoolean(APK_IS_AUTO_INSTALL);
        mHttpVerb = args.getString(HTTP_VERB);
        String url = args.getString(APP_UPDATE_SERVER_URL);

        if (Strings.isNullOrEmpty(mHttpVerb)) {
            mHttpVerb = "POST";
        }
        checkForUpdates(url);
    }

    /**
     * Heart of the library. Check if an update is available for download
     * parsing the desktop Play Store page of the app
     */
    private void checkForUpdates(final String url) {
        mThread = new Thread() {
            @Override
            public void run() {
                //if (isNetworkAvailable(mContext)) {

                String json = sendPost(url);
                if (json != null) {
                    parseJson(json);
                } else {
                    Log.e(TAG, "can't get app update json");
                }
                //}
            }

        };
        mThread.start();
    }

    private String sendPost(String url) {

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException ex) {
            return "";
        }
    }


    private void parseJson(String json) {
        mThread.interrupt();
        Looper.prepare();
        UpdateDescription description = new Gson().fromJson(json, UpdateDescription.class);
        try {
            int versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;

            if (description.versionCode > versionCode) {
                description.updateMessage += String.format(" [%d --> %d]", versionCode, description.versionCode);

                if (mTypeOfNotice == NOTICE_NOTIFICATION) {
                    showNotification(description.updateMessage, description.url, mIsAutoInstall);
                } else if (mTypeOfNotice == NOTICE_DIALOG) {
                    showDialog(description.updateMessage, description.url, mIsAutoInstall);
                }
            } else {
                Log.i(TAG, mContext.getString(R.string.app_no_new_update));
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            Log.e(TAG, "parse json error", ignored);
        }
    }

    /**
     * Show dialog
     */
    private void showDialog(String content, String apkUrl, boolean isAutoInstall) {
        UpdateDialog d = new UpdateDialog();
        Bundle args = new Bundle();
        args.putString(Constants.APK_UPDATE_CONTENT, content);
        args.putString(Constants.APK_DOWNLOAD_URL, apkUrl);
        args.putBoolean(Constants.APK_IS_AUTO_INSTALL, isAutoInstall);
        d.setArguments(args);

        // http://blog.csdn.net/chenshufei2/article/details/48747149
        // Don't use default d.show(mContext.getSupportFragmentManager(), null);
        FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
        ft.add(d, this.getClass().getSimpleName());
        ft.commitAllowingStateLoss();//注意这里使用commitAllowingStateLoss()
    }

    /**
     * Show Notification
     */
    private void showNotification(String content, String apkUrl, boolean isAutoInstall) {
        android.app.Notification noti;
        Intent myIntent = new Intent(mContext, DownloadService.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
        myIntent.putExtra(Constants.APK_IS_AUTO_INSTALL, isAutoInstall);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int smallIcon = mContext.getApplicationInfo().icon;
        noti = new NotificationCompat.Builder(mContext).setTicker(getString(R.string.newUpdateAvailable))
                .setContentTitle(getString(R.string.newUpdateAvailable)).setContentText(content).setSmallIcon(smallIcon)
                .setContentIntent(pendingIntent).build();

        noti.flags = android.app.Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
    }


    /**
     * Check if a network available
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null) {
                connected = ni.isConnected();
            }
        }
        return connected;
    }


}
