Android应用自动更新库 (android-update-apk)
===================

[![](https://jitpack.io/v/systembugtj/autoupdate.svg)](https://jitpack.io/#systembugtj/autoupdate)

v1.6.5

Fix SDK >= 24 file:// not allowed

该library项目实现了软件版本检查，apk文件下载，软件安装（Android app update checker,download and install apk）支持API 8+

#### 1.导入包 ####

有两种方式：

- 方式一：Gradle

```
dependencies {
    //jCenter
    compile 'com.artwl:android-update-apk:0.0.1'
}
```

- 方式二：下载并导入 android-update-apk 库

#### 2.调用 ####

提供2种调用方式, 在你的项目中添加以下代码即可

- 使用Dialog


	```
    	private static final String APP_UPDATE_SERVER_URL = "http://updatecheck";
        private static final boolean APK_IS_AUTO_INSTALL = true;

    	...

    	updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateChecker.checkForDialog(getActivity(), APP_UPDATE_SERVER_URL, APK_IS_AUTO_INSTALL);
            }
        });

    	...

	```

- 使用Notification

	```
    	private static final String APP_UPDATE_SERVER_URL = "http://updatecheck";
        private static final boolean APK_IS_AUTO_INSTALL = false;

    	...

    	updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateChecker.checkForNotification(getActivity(), APP_UPDATE_SERVER_URL, APK_IS_AUTO_INSTALL);
            }
        });

    	...

	```

服务端返回的JSON数据示例格式为：

`{"url":"http://192.168.205.33:8080/Hello/medtime_v3.0.1_Other_20150116.apk","versionCode":2,"updateMessage":"版本更新信息"}`

#### 3.添加权限 ####

- 添加访问网络的权限

	`<uses-permission android:name="android.permission.INTERNET" />`

- 添加写SDCard权限（可选，非必须）

	如果添加这个权限 apk下载在sdcard中的Android/data/包名/cache目录下 否则下载到 内存中的 /data/data/包名/cache中

	`<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`

#### 4.在AndroidManifest.xml中注册 DownloadService ####

`<service android:name="com.artwl.update.DownloadService" android:exported="true" />`

#### 5.示例效果图 ####
![screenshot](https://raw.github.com/artwl/android-update-apk/master/screenshots/sample.png)
![screenshot](https://raw.github.com/artwl/android-update-apk/master/screenshots/dialog.png)
![screenshot](https://raw.github.com/artwl/android-update-apk/master/screenshots/notification.png)


#### 6.使用与参考的开源项目 ####

1. [android-styled-dialogs](https://github.com/inmite/android-styled-dialogs "https://github.com/inmite/android-styled-dialogs") 使用该项目，可以在api 8+上显示 holo 风格的对话框，其它选择
，当然你也可以使用其它的开源项目比如：[ActionBarSherlock](https://github.com/JakeWharton/ActionBarSherlock "https://github.com/JakeWharton/ActionBarSherlock") 和 [HoloEverywhere](https://github.com/Prototik/HoloEverywhere "https://github.com/Prototik/HoloEverywhere")


2. [UpdateChecker](https://github.com/rampo/UpdateChecker "https://github.com/rampo/UpdateChecker") 该项目检查的是google play上的应用，如果有更新打开google Play,不提供下载apk的功能

