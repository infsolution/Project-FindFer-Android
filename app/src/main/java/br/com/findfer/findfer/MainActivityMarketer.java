package br.com.findfer.findfer;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.findfer.findfer.adapter.TabsAdapter;
import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.extras.UtilTCM;
import br.com.findfer.findfer.model.Coordinates;
import br.com.findfer.findfer.model.ItIsMarket;
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.NetworkConnection;
import br.com.findfer.findfer.network.Transaction;

public class MainActivityMarketer extends AppCompatActivity implements Transaction{

    public static User user;
    public static Coordinates coordinates;
    private ViewPager mViewPager;
    private ProgressBar load;
    private String url;
    private String [] titulos ={"ANUNCIOS", "CLIENTES"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        user = getUser();
        chooseStart();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("FindFer");
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), this,titulos));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        load = (ProgressBar)findViewById(R.id.pb_load_main);
        url = "http://www.findfer.com.br/FindFer/control/ItisinMarket.php";
     }
    private User getUser(){
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_marketer, menu);
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
        if(id == R.id.action_mange){
            Intent manage = new Intent(this, PosterManger.class);
            startActivity(manage);
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
                callVolleyRequest();
        }
        if(id == R.id.action_help){
            Intent help = new Intent(this, HelpActivity.class);
            startActivity(help);
            return true;
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

    public void callVolleyRequest(){
        NetworkConnection.getInstance(this).execute(this, url);
    }
    @Override
    public Map<String, String> doBefore() {
        load.setVisibility(View.VISIBLE);
        if(UtilTCM.verifyConnection(this)){
            Map<String, String> parameters = new HashMap<>();
            parameters.put("latitude",Double.toString(user.getActualLatitude()));
            parameters.put("longitude",Double.toString(user.getActualLongitude()));
            parameters.put("id_user",Long.toString(user.getCodUser()));
            return parameters;
        }else{
            Toast.makeText(this, "Sem conexão!", Toast.LENGTH_SHORT).show();
            load.setVisibility(View.GONE);
        }
        return null;
    }

    @Override
    public void doAfter(String response) {
        load.setVisibility(View.GONE);
        ItIsMarket mark = getLocation(response);
        if(mark.getItIsMarket() == 1){
        Intent newPost = new Intent(this,NewPoster.class);
        newPost.putExtra("id_market",mark.getIdMarket());
        startActivity(newPost);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityMarketer.this);
            builder.setTitle("Desculpe!");
            builder.setMessage(mark.toString());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public ItIsMarket getLocation(String response){
        ItIsMarket isMarket = null;
        try{
            JSONArray mark = new JSONArray(response);
            JSONObject mkt = mark.getJSONObject(0);
            isMarket = new ItIsMarket(mkt.getString("name"),mkt.getInt("itsinmarket"));
            isMarket.setDistance(mkt.getDouble("distance"));
            isMarket.setIdMarket(mkt.getLong("id_market"));

        }catch (JSONException e){

        }
        return isMarket;
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
