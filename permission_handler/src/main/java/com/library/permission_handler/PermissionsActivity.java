package com.library.permission_handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Vinod Baste on 12/11/2022 on 11:19 PM
 */

@SuppressWarnings("unchecked")
public class PermissionsActivity extends Activity {

    private static final int RC_SETTINGS = 6739;
    private static final int RC_PERMISSION = 6937;

    static final String EXTRA_PERMISSIONS = "permissions";
    static final String EXTRA_RATIONALE = "rationale";
    static final String EXTRA_OPTIONS = "options";

    static PermissionHandler permissionHandler;

    private ArrayList<String> allPermissions, deniedPermissions, noRationaleList;
    private PermissionsHandler.Options options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_PERMISSIONS)) {
            finish();
            return;
        }

        getWindow().setStatusBarColor(0);
        allPermissions = (ArrayList<String>) intent.getSerializableExtra(EXTRA_PERMISSIONS);
        options = (PermissionsHandler.Options) intent.getSerializableExtra(EXTRA_OPTIONS);
        if (options == null) {
            options = new PermissionsHandler.Options();
        }
        deniedPermissions = new ArrayList<>();
        noRationaleList = new ArrayList<>();

        boolean noRationale = true;
        for (String permission : allPermissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
                if (shouldShowRequestPermissionRationale(permission)) {
                    noRationale = false;
                } else {
                    noRationaleList.add(permission);
                }
            }
        }

        if (deniedPermissions.isEmpty()) {
            grant();
            return;
        }

        String rationale = intent.getStringExtra(EXTRA_RATIONALE);
        if (noRationale || TextUtils.isEmpty(rationale)) {
            PermissionsHandler.log("No rationale.");
            requestPermissions(toArray(deniedPermissions), RC_PERMISSION);
        } else {
            PermissionsHandler.log("Show rationale.");
            showRationale(rationale);
        }
    }

    private void showRationale(String rationale) {
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                requestPermissions(toArray(deniedPermissions), RC_PERMISSION);
            } else {
                deny();
            }
        };
        new AlertDialog.Builder(this).setTitle(options.rationaleDialogTitle)
                .setMessage(rationale)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .setOnCancelListener(dialog -> deny()).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length == 0) {
            deny();
        } else {
            deniedPermissions.clear();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (deniedPermissions.size() == 0) {
                PermissionsHandler.log("Just allowed.");
                grant();
            } else {
                ArrayList<String> blockedList = new ArrayList<>(); //set not to ask again.
                ArrayList<String> justBlockedList = new ArrayList<>(); //just set not to ask again.
                ArrayList<String> justDeniedList = new ArrayList<>();
                for (String permission : deniedPermissions) {
                    if (shouldShowRequestPermissionRationale(permission)) {
                        justDeniedList.add(permission);
                    } else {
                        blockedList.add(permission);
                        if (!noRationaleList.contains(permission)) {
                            justBlockedList.add(permission);
                        }
                    }
                }

                if (justBlockedList.size() > 0) { //checked don't ask again for at least one.
                    PermissionHandler permissionHandler = PermissionsActivity.permissionHandler;
                    finish();
                    if (permissionHandler != null) {
                        permissionHandler.onPermissionDeniedOnce(getApplicationContext(), justBlockedList,
                                deniedPermissions);
                    }

                } else if (justDeniedList.size() > 0) { //clicked deny for at least one.
                    deny();

                } else { //unavailable permissions were already set not to ask again.
                    if (permissionHandler != null &&
                            permissionHandler.onPermissionNeverAskAgain(getApplicationContext(), blockedList)) {
                        sendToSettings();

                    } else finish();
                }
            }
        }
    }

    private void sendToSettings() {
        if (!options.sendBlockedToSettings) {
            deny();
            return;
        }
        PermissionsHandler.log("Ask to go to settings.");
        new AlertDialog.Builder(this).setTitle(options.settingsDialogTitle)
                .setMessage(options.settingsDialogMessage)
                .setPositiveButton(options.settingsText, (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    startActivityForResult(intent, RC_SETTINGS);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> deny())
                .setOnCancelListener(dialog -> deny()).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SETTINGS && permissionHandler != null) {
            PermissionsHandler.requestPermission(this, toArray(allPermissions), null, options,
                    permissionHandler);
        }
        // super, because overridden method will make the handler null, and we don't want that.
        super.finish();
    }

    private String[] toArray(ArrayList<String> arrayList) {
        int size = arrayList.size();
        String[] array = new String[size];
        for (int i = 0; i < size; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    @Override
    public void finish() {
        permissionHandler = null;
        super.finish();
    }

    private void deny() {
        PermissionHandler permissionHandler = PermissionsActivity.permissionHandler;
        finish();
        if (permissionHandler != null) {
            permissionHandler.onPermissionDenied(getApplicationContext(), deniedPermissions);
        }
    }

    private void grant() {
        PermissionHandler permissionHandler = PermissionsActivity.permissionHandler;
        finish();
        if (permissionHandler != null) {
            permissionHandler.onPermissionGranted();
        }
    }

}
