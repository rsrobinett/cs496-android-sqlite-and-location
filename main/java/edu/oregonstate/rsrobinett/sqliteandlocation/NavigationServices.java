package edu.oregonstate.rsrobinett.sqliteandlocation;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import static com.google.android.gms.location.LocationServices.*;
import static edu.oregonstate.rsrobinett.sqliteandlocation.Constants.LOCATION_PERMISSION_LIST;
import static edu.oregonstate.rsrobinett.sqliteandlocation.Constants.LOCATION_PERMISSION_RESULT;

public class NavigationServices extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationListener mLocationListener;
    private TextView mLonText;
    private TextView mLatText;
    private static PermissionServices PermissionServices;
    private Location mLastLocation;

    public NavigationServices(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getOutPut();
        getApiClient();
        createLocationRequest();
        createLocationListener();
    }


    public void start(){
        mLatText.setText("start is called");
        mLonText.setText("start is called");
        //mGoogleApiClient.connect();
    }

    private void getOutPut() {
        mLatText = (TextView) findViewById(R.id.current_latitude);
        mLonText = (TextView) findViewById(R.id.current_longitude);
    }

    public void updateLocation() {

        if (!PermissionServices.IsPermissionGranted(LOCATION_PERMISSION_LIST)) {
            PermissionServices.RequestPermissions(LOCATION_PERMISSION_LIST,LOCATION_PERMISSION_RESULT);
            return;
        }
        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLonText.setText(String.valueOf(mLastLocation.getLongitude()));
        } else {
            FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
        }
    }


    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //possibly set text that says it's connected
        PermissionServices = new PermissionServices();

        if(!PermissionServices.IsPermissionGranted(LOCATION_PERMISSION_LIST)){
            //possibly write a note that permission is not granted
            PermissionServices.RequestPermissions(LOCATION_PERMISSION_LIST,LOCATION_PERMISSION_RESULT);
            return;
        }
        updateLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart(){
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void getApiClient(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .build();
        }
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
                if (location != null) {
                    mLonText.setText(String.valueOf(location.getLongitude()));
                    mLatText.setText(String.valueOf(location.getLatitude()));
                } else {
                    mLonText.setText("No Location Available");
                    mLatText.setText("No Location Available");
                }
            }
        };
    }
}
