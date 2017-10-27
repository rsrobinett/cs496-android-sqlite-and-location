package edu.oregonstate.rsrobinett.sqliteandlocation;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static edu.oregonstate.rsrobinett.sqliteandlocation.Constants.LOCATION_PERMISSION_RESULT;

public class PermissionServices extends AppCompatActivity {


    public void RequestPermissions(String[] permissionList) {
        ActivityCompat.requestPermissions(this, permissionList, LOCATION_PERMISSION_RESULT);
    }

    public int CheckPermissions(int permissionType){
        ActivityCompat.checkSelfPermission(this, )
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
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    for (int i = 0; i < permissions.length; i++) {
                        result_text += permissions[i] + ":";
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            result_text += "true ";
                        } else {
                            result_text += "false ";
                        }
                    }

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

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
