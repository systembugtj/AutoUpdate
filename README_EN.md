android-update-apk
===================
[![Maven](https://img.shields.io/badge/maven-v0.0.1-brightgreen.svg)](https://bintray.com/artwl/maven/android-update-apk/0.0.1/view)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

Android app update checker, download and install apk(auto or manual). Support API level 8+


#### 1. import library ####

You can import this library in two ways:

- first: Gradle
```
dependencies {
    //jCenter
    compile 'com.artwl:android-update-apk:0.0.1'
}
```
- second: download android-update-apk library and import

#### 2. import library ####

You can use this library in two ways , example code:

- Dialog


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

- Notification

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

The server response JSON data format is:

`{"url":"http://192.168.205.33:8080/Hello/medtime_v3.0.1_Other_20150116.apk","versionCode":2,"updateMessage":"update info"}`

#### 3. add permission ####

- Add INTERNET permission

    `<uses-permission android:name="android.permission.INTERNET" />`

- Add WRITE_EXTERNAL_STORAGE permission(optional)

    if you add this permission, apk will save in /data/packagename/cache, Otherwise save in /data/data/packagename/cache

    `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`

#### 4. Register DownloadService in AndroidManifest.xml ####

`<service android:name="com.artwl.update.DownloadService" android:exported="true" />`

#### 5. sample project screens ####
![screenshot](https://raw.github.com/artwl/android-update-apk/master/screenshots/sample.png)
![screenshot](https://raw.github.com/artwl/android-update-apk/master/screenshots/dialog.png)
![screenshot](https://raw.github.com/artwl/android-update-apk/master/screenshots/notification.png)


#### 6. reference opensource projects ####

1. [android-styled-dialogs](https://github.com/inmite/android-styled-dialogs "https://github.com/inmite/android-styled-dialogs")

2. [UpdateChecker](https://github.com/rampo/UpdateChecker "https://github.com/rampo/UpdateChecker")
