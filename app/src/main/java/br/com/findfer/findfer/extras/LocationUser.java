package br.com.findfer.findfer.extras;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import br.com.findfer.findfer.model.Coordinates;

/**
 * Created by infsolution on 30/11/17.
 */

public class LocationUser implements   GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Location location;
    private GoogleApiClient fGoogleApiClient;
    private LocationRequest locationRequest;
    private Coordinates coordinates;
    private Context context;

   public LocationUser(Context context){
       this.context = context;
       callConnection();
   }

    private void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    public Coordinates location(){
        startLocationUpdate();
        return coordinates;
    }
    private synchronized void callConnection() {
        fGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        fGoogleApiClient.connect();
    }
    private void startLocationUpdate() {
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(fGoogleApiClient, locationRequest, LocationUser.this);

    }
    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(fGoogleApiClient,LocationUser.this);
    }



    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(fGoogleApiClient);
        startLocationUpdate();
        if(location != null){
            coordinates = new Coordinates(location.getLatitude(), location.getLongitude());
            stopLocationUpdate();
           }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(coordinates == null){
            coordinates = new Coordinates(location.getLatitude(), location.getLongitude());
        }
        stopLocationUpdate();
    }


}
