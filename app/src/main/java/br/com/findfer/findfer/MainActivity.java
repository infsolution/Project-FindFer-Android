package br.com.findfer.findfer;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.findfer.findfer.adapter.TabsAdapter;
import br.com.findfer.findfer.dao.CoordinateDao;
import br.com.findfer.findfer.dao.RecordDao;
import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.extras.UtilTCM;
import br.com.findfer.findfer.fragments.PosterFragment;
import br.com.findfer.findfer.interfaces.FragmentComunication;
import br.com.findfer.findfer.model.Account;
import br.com.findfer.findfer.model.Coordinates;
import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.model.Record;
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity /*implements   GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener*/ {

    public static String relationship;
    public static User user;
    public static Coordinates coordinates;
    private UserDao uDao;
    private ViewPager mViewPager;
    private Location location;
    private GoogleApiClient fGoogleApiClient;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        user = getUser();
        chooseStart();
        relationship = setRelationship();
        //callConnection();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("FindFer");
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
     }
    private String setRelationship(){
        if(user != null && user.getTypeAccount()<= 1){
            return "FEIRANTES";
        }
        return "CLIENTES";
    }
    private User getUser(){
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(user != null && user.getTypeAccount() <= 1) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_main_marketer, menu);
        }
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);

        MenuItem menuItem = menu.findItem(R.id.action_searchable_activity);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_profile){
            Intent profile = new Intent(this, ProfileEditActivity.class);
            startActivity(profile);
        }
         if (id == R.id.action_new_user) {
            if(user.getTypeAccount() == 1) {
                Intent addUser = new Intent(this, NewMarketer.class);
                startActivity(addUser);
                return true;
            }else{
                Intent updateUser = new Intent(this, UpdateMarketer.class);
                startActivity(updateUser);
                return true;
            }
        }
        if(id == R.id.action_interaction){
            if(user.getTypeAccount() == 1){
                //Intent reqProm = new Intent(this,RequestPromotion.class);
                //startActivity(reqProm);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Funcionalidade em desenvolvimento");
                builder.setMessage("Para pedir uma promoção abra um anúncio com o produto que vocẽ deseja e click no icone pedir promoção.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                Intent newPost = new Intent(this,NewPoster.class);
                startActivity(newPost);
            }
        }

        return super.onOptionsItemSelected(item);
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
   /* private synchronized void callConnection() {
        fGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        fGoogleApiClient.connect();
    }
    private void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private void startLocationUpdate() {
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(fGoogleApiClient, locationRequest, MainActivity.this);
    }
    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(fGoogleApiClient,MainActivity.this);
    }



    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(fGoogleApiClient);
        startLocationUpdate();
        if(location != null){
            //coordinates = new Coordinates(location.getLatitude(), location.getLongitude());
            //sendCoordinate(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()), posterFragment);
            UserDao userDao = new UserDao(this);
            user.setActualLatitude(location.getLatitude());
            user.setActualLongitude(location.getLongitude());
            userDao.updateCoordinate(user);
            Toast.makeText(this, "Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Location Nullo ", Toast.LENGTH_SHORT).show();
        }
        stopLocationUpdate();
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
    }*/



}
