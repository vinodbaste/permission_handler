package com.library.permission_handler;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void requestPhone(View view) {
        PermissionsHandler.requestPermission(this, Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Phone granted.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestCameraAndStorage(View view) {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        PermissionsHandler.requestPermission(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Camera+Storage granted.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(Context context, ArrayList<String> deniedPermissions) {
                Log.e("deniedPermissions", deniedPermissions.toString());
                Toast.makeText(MainActivity.this, "Location denied.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onPermissionNeverAskAgain(Context context, ArrayList<String> blockedList) {
                Log.e("onBlockedPermissions", blockedList.toString());
                return super.onPermissionNeverAskAgain(context, blockedList);
            }

            @Override
            public void onPermissionDeniedOnce(Context context, ArrayList<String> justBlockedList, ArrayList<String> deniedPermissions) {
                super.onPermissionDeniedOnce(context, justBlockedList, deniedPermissions);

                Log.e("onJustPermissions1", justBlockedList.toString());
                Log.e("onJustPermissions2", deniedPermissions.toString());
            }
        });
    }

    public void requestLocation(View view) {
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
    }

    public void openSettings(View view) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

}