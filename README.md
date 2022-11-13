# permission_handler
On most operating systems, permissions aren't just granted to apps at install time. Rather, developers have to ask the user for permissions while the app is running. 

This plugin provides a API to request permissions and check their status. You can also open the device's app settings so users can grant a permission.
and you can show a rationale for requesting a permission.

[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=26)
[![GitHub tag](https://img.shields.io/github/tag/vinodbaste/image-compressor?include_prereleases=&sort=semver&color=blue)](https://github.com/vinodbaste/image-compressor/releases/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue)](#license)
[![News - Android Weekly](https://img.shields.io/badge/News-Android_Weekly-d36f21)](https://androidweekly.net/issues/issue-326)
[![Story - Medium](https://img.shields.io/badge/Story-Medium-2ea44f)](https://medium.com/codex/image-compressor-13dbfd0445a3)
[![GitHub - VinodBaste](https://img.shields.io/badge/GitHub-VinodBaste-4664c6)](https://github.com/vinodbaste/permission_handler)

<img src = "https://github.com/vinodbaste/app_update/raw/main/screenshots/android.png" width = 250 height = 500 />

# How to implement
To get a Git project into your build:
## Gradle
` Step 1:` Add it in your **root build.gradle**  at the end of repositories:
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

`Step 2:` Add the dependency in your **project build.gradle**
```java
dependencies {
    implementation 'com.github.vinodbaste:permission_handler:1.0.0'
}
```

# Usage:
------

First declare your permissions in the manifest file.
Example:

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

## Single permission
Pass the single permission you want to request.
```java
PermissionsHandler.requestPermission(this, Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
@Override
public void onPermissionGranted() {
        Toast.makeText(MainActivity.this, "Phone granted.", Toast.LENGTH_SHORT).show();
        }
});
```

## Multiple permissions
pass multiple requests in a list
```java
String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        PermissionsHandler.requestPermission(this,permissions, null, new PermissionHandler() {
@Override
public void onPermissionGranted() {
        Toast.makeText(MainActivity.this, "Permissions granted.", Toast.LENGTH_SHORT).show();
        }
});
```

## override methods
**onPermissionGranted:**
Handle your logic on permission granted
```java
public void onPermissionGranted(){
        Toast.makeText(MainActivity.this,"Permissions granted.",Toast.LENGTH_SHORT).show();
        }
```

**onPermissionDenied:**
Handle your logic on permission denied
```java
 @Override
public void onPermissionDenied(Context context, ArrayList<String> deniedPermissions) {
        Toast.makeText(MainActivity.this, "Location denied.", Toast.LENGTH_SHORT).show();
        }
```

**onPermissionDeniedOnce:**
Handle your logic on permission denied once
```java
@Override
public void onPermissionDeniedOnce(Context context, ArrayList<String> justBlockedList, ArrayList<String> deniedPermissions) {
        super.onPermissionDeniedOnce(context, justBlockedList, deniedPermissions);

        Log.e("onJustPermissions1", justBlockedList.toString());
        Log.e("onJustPermissions2", deniedPermissions.toString());
        }
```

**onPermissionNeverAskAgain:**
Handle your logic on permission never ask again
```java
 @Override
public boolean onPermissionNeverAskAgain(Context context, ArrayList<String> blockedList) {
        return super.onPermissionNeverAskAgain(context, blockedList);
        }
```

## Customized permissions request
you can customize the permission request by customising the options.
```java
String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        String rationale = "Please provide location permission so that you can ...";
        PermissionsHandler.Options options = new PermissionsHandler.Options()
        .setRationaleDialogTitle("Info")
        .setSettingsDialogTitle("Warning");

        PermissionsHandler.requestPermission(this, permissions, rationale, options, new PermissionHandler() {
@Override
public void onPermissionGranted() {
        Toast.makeText(MainActivity.this, "Location granted.", Toast.LENGTH_SHORT).show();
        }

@Override
public void onPermissionDenied(Context context, ArrayList<String> deniedPermissions) {
        Toast.makeText(MainActivity.this, "Location denied.", Toast.LENGTH_SHORT).show();
        }
        });
```

**If you find this library useful, please consider starring this repository from the top of this page.**
[![](https://i.imgur.com/oSLuE0e.png)](#)

# Support my work
<a href="https://www.buymeacoffee.com/bastevinod" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" height="41" width="174"></a>

# License
```
Copyright [2022] [Vinod Baste]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```