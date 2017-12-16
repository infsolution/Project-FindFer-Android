package br.com.findfer.findfer;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.model.Coordinates;
import br.com.findfer.findfer.model.User;

public class InitialActivity extends AppCompatActivity implements   GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{
    private User user;
    private Location location;
    private GoogleApiClient fGoogleApiClient;
    private LocationRequest locationRequest;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        user = getUser();
        callConnection();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        },5000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(fGoogleApiClient != null){
            stopLocationUpdate();
        }
    }

    private User getUser(){
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }
    public void startApp(){
        if(user == null){
            chooseStart();
        }else{
        Intent goMain = new Intent(this, MainActivity.class);
        startActivity(goMain);
        }
        finish();
    }
    private void chooseStart(){
        if(user == null){
            actionRecord();
        }
    }
    private void actionRecord(){
        Intent record = new Intent(this, RecordOneActivity.class);
        startActivity(record);
    }

    private synchronized void callConnection() {
        Log.i("LOG","CallConnection start InitActivity");
        fGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        fGoogleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        Location locationConn = LocationServices.FusedLocationApi.getLastLocation(fGoogleApiClient);
        if(locationConn != null){
            Log.i("LOG","Localização atualizada no sqlite com onConnected");
            UserDao userDao = new UserDao(this);
            user.setActualLatitude(locationConn.getLatitude());
            user.setActualLongitude(locationConn.getLongitude());
            userDao.updateCoordinate(user);
        }else{
            startLocationUpdate();
        }
    }

    private void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate() {
        Log.i("LOG","InitLocatio in StartLocation start");
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(fGoogleApiClient, locationRequest, InitialActivity.this);
    }
    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(fGoogleApiClient,InitialActivity.this);
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
        Log.i("LOG","Localização atualizada no sqlite com OnChanged");
        UserDao userDao = new UserDao(this);
        user.setActualLatitude(location.getLatitude());
        user.setActualLongitude(location.getLongitude());
        userDao.updateCoordinate(user);
        }else{
            Log.i("LOG","Update da localização indisponivel");
        }
    }
}

