package com.library.permission_handler;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *  <pre>
 * The class for handling permission callbacks.
 * Created on 12/11/2022 on 3:42 PM
 *  <pre>
 * @author Vinod Baste
 */
@SuppressWarnings("WeakerAccess")
public abstract class PermissionHandler {

    /**
     * This method will be called if all of the requested permissions are granted.
     */
    public abstract void onPermissionGranted();

    /**
     * This method will be called if some of the requested permissions have been denied.
     *
     * @param context           The application context.
     * @param deniedPermissions The list of permissions which have been denied.
     */
    public void onPermissionDenied(Context context, ArrayList<String> deniedPermissions) {
        if (PermissionsHandler.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Denied:");
            for (String permission : deniedPermissions) {
                builder.append(" ");
                builder.append(permission);
            }
            PermissionsHandler.log(builder.toString());
        }
        Toast.makeText(context, "Permission Denied.", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method will be called if some permissions have previously been set not to ask again.
     *
     * @param context     the application context.
     * @param blockedList the list of permissions which have been set not to ask again.
     * @return The overrider of this method should return true if no further action is needed,
     * and should return false if the default action is to be taken, i.e. send user to settings.
     * <br><br>
     * Note: If the option {@link PermissionsHandler.Options#sendDontAskAgainToSettings(boolean)} has been
     * set to false, the user won't be sent to settings by default.
     */
    @SuppressWarnings("UnusedParameters")
    public boolean onPermissionNeverAskAgain(Context context, ArrayList<String> blockedList) {
        if (PermissionsHandler.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Set not to ask again:");
            for (String permission : blockedList) {
                builder.append(" ");
                builder.append(permission);
            }
            PermissionsHandler.log(builder.toString());
        }
        return true;
    }

    /**
     * This method will be called if some permissions have just been set not to ask again.
     *
     * @param context           The application context.
     * @param justBlockedList   The list of permissions which have just been set not to ask again.
     * @param deniedPermissions The list of currently unavailable permissions.
     */
    public void onPermissionDeniedOnce(Context context, ArrayList<String> justBlockedList,
                                       ArrayList<String> deniedPermissions) {
        if (PermissionsHandler.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Just set not to ask again:");
            for (String permission : justBlockedList) {
                builder.append(" ");
                builder.append(permission);
            }
            PermissionsHandler.log(builder.toString());
        }
        onPermissionDenied(context, deniedPermissions);
    }

}
