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
import br.com.findfer.findfer.model.ItIsMarket;
import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.model.Record;
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.NetworkConnection;
import br.com.findfer.findfer.network.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Transaction{

    public static String relationship;
    public static User user;
    public static Coordinates coordinates;
    private UserDao uDao;
    private ViewPager mViewPager;
    private ProgressBar load;
    private String url;
    private String [] titulos = {"ANUNCIOS", "FEIRANTES"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        user = getUser();
        chooseStart();
        relationship = setRelationship();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("FindFer");
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), this, titulos));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        load = (ProgressBar)findViewById(R.id.pb_load_main);
        url = "http://www.findfer.com.br/FindFer/control/ItisinMarket.php";
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
       /* if(id == R.id.action_profile){
            Intent profile = new Intent(this, ProfileEditActivity.class);
            startActivity(profile);
            Toast.makeText(this, "Aquarde, funcionalidade em desenvolvimento.", Toast.LENGTH_SHORT).show();
        }*/
         if (id == R.id.action_new_user) {
           // if(user.getTypeAccount() == 1) {
                //Intent addUser = new Intent(this, NewMarketer.class);
                //startActivity(addUser);
                callVolleyRequest();
                return true;
           /* }else{
                Intent updateUser = new Intent(this, UpdateMarketer.class);
                startActivity(updateUser);
                return true;
            }*/
        }
        /*if(id == R.id.action_interaction){
                Intent reqProm = new Intent(this,RequestPromotion.class);
                startActivity(reqProm);
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

        }*/
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
            Intent addUser = new Intent(this, NewMarketer.class);
            //addUser.putExtra("id_market", user.getIdMarket());
            startActivity(addUser);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

        }catch (JSONException e){

        }
        return isMarket;
    }



}
