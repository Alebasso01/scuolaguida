package com.example.scuolaguida.models;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {
    public final static String[] CALENDAR_PERMISSIONS = {Manifest.permission.READ_CALENDAR};
    private final static String TAG = PermissionManager.class.getCanonicalName();

    private final Context context;

    public PermissionManager(Context context) {
        this.context = context;
    }

    public boolean askNeededPermissions(int requestCode, boolean performRequest, String[] permissions) {
        List<String> missingPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this.context, permission) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission " + permission + " is granted by the user.");
            }


            /*else if (shouldShowRequestPermissionRationale(...)){
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                showInContextUI(...);
            }*/
            else {
                missingPermissions.add(permission);
            }
        }

        if (missingPermissions.isEmpty()) {
            return false;
        }

        /*
        NOTE:
            After some `requestPermissions` calls, Android automatically answer no to the popup.
            To avoid that, the apps usually ask for the permission on one time (first run) and then open the settings activity.
            For instance: https://stackoverflow.com/questions/32822101/how-can-i-programmatically-open-the-permission-screen-for-a-specific-app-on-andr
            ```
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", this.activity.getPackageName(), null);
            intent.setData(uri);
            this.activity.startActivity(intent);
            ```
         */

        if (performRequest) {
            Log.d(TAG, "Request for missing permissions " + missingPermissions);

            // https://developer.android.com/reference/androidx/core/app/ActivityCompat#requestPermissions(android.app.Activity,java.lang.String[],int)
            ActivityCompat.requestPermissions((Activity) context, missingPermissions.toArray(new String[missingPermissions.size()]), requestCode);
        }

        return true;
    }

    public boolean askCalendarPermission(int requestcode, boolean performRequest){
        return askNeededPermissions(requestcode, performRequest, CALENDAR_PERMISSIONS);
    }
}
