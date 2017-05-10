package mota.dev.happytesting.managers;

import android.Manifest;
import android.app.Activity;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;

/**
 * Created by user on 10/05/2017.
 */

public class PermissionManager {
    private static PermissionManager instance;
    private HashMap<Integer,PermisionResult> callbacks;

    private PermissionManager() {

    }

    public static PermissionManager getInstance() {
        if (instance == null)
            instance = new PermissionManager();
        return instance;
    }

    public void requestPermission(Activity activity, String permission, int constant, PermisionResult callback)
    {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            callbacks.put(constant,callback);
            ActivityCompat.requestPermissions(activity, new String[]{permission}, constant);

        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if(callbacks.containsKey(requestCode))
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                callbacks.get(requestCode).onGranted();
            else
                callbacks.get(requestCode).onDenied();
        }
    }

    public interface PermisionResult {
        void onDenied();

        void onGranted();
    }


}
