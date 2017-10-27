package edu.oregonstate.rsrobinett.sqliteandlocation;

import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static edu.oregonstate.rsrobinett.sqliteandlocation.Constants.LOCATION_PERMISSION_RESULT;

public class PermissionServices extends AppCompatActivity {


    private final NavigationServices _navigationServices;
    private LauncherApps.Callback mCallback;

    public PermissionServices() {
        _navigationServices = new NavigationServices();
    }

    public void RequestPermissions(String[] permissionList, int requestCode) {
        ActivityCompat.requestPermissions(this, permissionList, requestCode);
    }

    public boolean IsPermissionGranted(String[] permissions){
        if(CheckPermissions(permissions)==PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    private int CheckPermissions(String[] permissions){
        for (int i = 0; i<permissions.length; i++)
             {
                 if(ContextCompat.checkSelfPermission(this, permissions[i])!= PERMISSION_GRANTED){
                     return PERMISSION_DENIED;
                 }
             }
             return PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        TextView permission_result_text_view = (TextView) findViewById(R.id.permission_message);

        String result_text = "";
        switch (requestCode) {
            case LOCATION_PERMISSION_RESULT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PERMISSION_GRANTED) {

                    for (int i = 0; i < permissions.length; i++) {
                        result_text += permissions[i] + ":";
                        if (grantResults[i] == PERMISSION_GRANTED) {
                            result_text += "true ";
                        } else {
                            result_text += "false ";
                        }
                    }
                    //_navigationServices.updateLocation();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    result_text = "permission not PERMISSION_GRANTED";
                }
                permission_result_text_view.setText(result_text);
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
