package edu.oregonstate.rsrobinett.sqliteandlocation;

/**
 * Created by rrobinett on 10/26/2017.
 */

public class Constants {

    private Constants() {
        // restrict instantiation
    }

    public static final int LOCATION_PERMISSION_RESULT = 1;
    public static final String[] LOCATION_PERMISSION_LIST = {
            //android.Manifest.permission.INTERNET,
            //android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};

    public static final double DEFAULT_LONGITUDE = 44.5;
    public static final double DEFAULT_LATITUDE = -123.2;
}
