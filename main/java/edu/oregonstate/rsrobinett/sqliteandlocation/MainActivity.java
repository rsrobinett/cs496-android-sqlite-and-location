package edu.oregonstate.rsrobinett.sqliteandlocation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;
import static edu.oregonstate.rsrobinett.sqliteandlocation.Constants.*;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private TextView mLatText;
    private GoogleApiClient mGoogleApiClient;
    private TextView mLonText;
    private Location mLastLocation;
    private TextView mPermissionText;
    private LocationRequest mLocationRequest;
    private LocationListener mLocationListener;
    private EditText mTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApiClient();
        createLocationRequest();
        createLocationListener();

        final Button button_save = (Button) findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //updateLocation();
                Intent intent = new Intent(MainActivity.this, SQLiteActivity.class);
                intent.putExtra("longitude", Double.parseDouble(mLatText.getText().toString()));
                intent.putExtra("latitude",Double.parseDouble(mLonText.getText().toString()));
                intent.putExtra("text",mTextInput.getText().toString());
                intent.putExtra("save",true);
                startActivity(intent);

                //new AsyncUpdateLocationAndSave().execute();
            }
        });

        final Button button_assignment = (Button) findViewById(R.id.button_assignment);
        button_assignment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AssignmentActivity.class);
                startActivity(intent);
            }
        });

        final Button button_view_list = (Button) findViewById(R.id.button_view_list);
        button_view_list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SQLiteActivity.class);
                intent.putExtra("save",false);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        mTextInput.setText(null);
        super.onRestart();
    }


        @Override
    protected void onStart() {
        getViewElements();
        mGoogleApiClient.connect();
        super.onStart();

    }

    private void getViewElements() {
        mLatText = (TextView) findViewById(R.id.current_latitude);
        mLonText = (TextView) findViewById(R.id.current_longitude);
        mPermissionText = (TextView) findViewById(R.id.permission_message);
        mTextInput = (EditText) findViewById(R.id.edit_message);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void getApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .build();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_RESULT: {
                     if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != PERMISSION_GRANTED) {
                            setDefaultLocation();
                            mPermissionText.setText("Permission Not granted");
                            return;
                        }
                    }
                         mPermissionText.setText("Permission granted");
                    updateLocation();
                } else {
                         setDefaultLocation();
                         mPermissionText.setText("Permission Not granted");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void setDefaultLocation() {
        mLatText.setText(Double.toString(DEFAULT_LATITUDE));
        mLonText.setText(Double.toString(DEFAULT_LONGITUDE));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, LOCATION_PERMISSION_LIST, LOCATION_PERMISSION_RESULT);
            return;
        }
        mPermissionText.setText("Permission granted");
        updateLocation();
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, LOCATION_PERMISSION_LIST, LOCATION_PERMISSION_RESULT);
            return;
        }
        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLonText.setText(String.valueOf(mLastLocation.getLongitude()));
        } else {
            setDefaultLocation();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,mLocationListener);
    }

    private void createLocationRequest(){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
    }

    private void createLocationListener(){
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location == null) {
                    setDefaultLocation();
                }
                updateLocation();
            }
        };
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class AsyncUpdateLocationAndSave extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            updateLocation();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent(MainActivity.this, SQLiteActivity.class);
            intent.putExtra("longitude", Double.parseDouble(mLatText.getText().toString()));
            intent.putExtra("latitude",Double.parseDouble(mLonText.getText().toString()));
            intent.putExtra("text",mTextInput.getText().toString());
            intent.putExtra("save",true);
            startActivity(intent);
        }
    }
}

