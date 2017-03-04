package com.techpro.chat.ticklechat.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LocationController
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected static final String TAG = LocationController.class.getSimpleName();
    public static final int LOCATION_REQUEST_CODE = 1000;
    LocationManager mLocationManager;

    private Context context;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Callbacks callbacks;
    private Timer timer;

    private static final int UPDATE_INTERVAL_IN_MILLISECONDS = 5 * 1000;
    private static final int EXPIRATION_DURATION_MILLISECONDS = 5 * 60 * 1000;
    private static LocationController instance = null;

    public static LocationController getInstance(Context context) {
        if (instance == null) {
            instance = new LocationController(context);
        }
        return instance;
    }

    private LocationController(Context context) {
        this.context = context;
        mGoogleApiClient = new GoogleApiClient.Builder(this.context).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocationRequest = LocationRequest.create()
                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setNumUpdates(1)
                .setExpirationDuration(EXPIRATION_DURATION_MILLISECONDS);
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                .PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission
                .ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, new MyLocationListener());
        }
    }

    public boolean isLocationEnabled() {
        boolean gpsEnabled, networkEnabled;
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gpsEnabled || networkEnabled;
    }

    public void getLocation(Callbacks callbacks) {
        this.callbacks = callbacks;
        if (!isLocationEnabled()) {
            callbacks.onLocationDisabled();
        } else {
            callbacks.onLocationRequested();
            connect();
        }
    }

    public String getLocality(Location location) throws IOException {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addressList = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        if (addressList.size() > 0)
            return addressList.get(0).getLocality();
        return null;
    }

    private void connect() {
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        } else if (callbacks != null) {
            callbacks.onError();
        }
    }

    private void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (callbacks != null) {
            callbacks.onError();
        }
    }

    @Override
    public void onConnected(Bundle args) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null && callbacks != null) {
            sendLocationReceivedCallback(lastLocation);
            disconnect();
            return;
        }
        startLocationUpdates();
        timer = new Timer();
        timer.schedule(new TimerLocation(), 30000);
    }

    private void sendLocationReceivedCallback(Location location) {
        callbacks.onLocationReceived(location);
    }

    private void startLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            sendLocationReceivedCallback(location);
            timer.cancel();
            disconnect();
            stopLocationUpdates();
        }
    }

    public interface Callbacks {
        void onLocationRequested();

        void onLocationReceived(Location location);

        void onError();

        void onLocationDisabled();

        void onProviderEnabled();
    }

    private final class MyLocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location locFromGps) {
            // called when the listener is notified with a location update from the GPS
        }

        @Override
        public void onProviderDisabled(String provider) {
            // called when the GPS provider is turned off (user turning off the GPS on the phone)
        }

        @Override
        public void onProviderEnabled(String provider) {
            // called when the GPS provider is turned on (user turning on the GPS on the phone)
            if (callbacks != null)
                callbacks.onProviderEnabled();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // called when the status of the GPS provider changes
        }
    }

    @Override
    protected void finalize() throws Throwable {
        disconnect();
        super.finalize();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
    }

    private class TimerLocation extends TimerTask {

        @Override
        public void run() {
            disconnect();
            stopLocationUpdates();
            callbacks.onError();
        }
    }
}