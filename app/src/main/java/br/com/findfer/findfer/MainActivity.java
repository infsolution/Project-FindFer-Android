package br.com.findfer.findfer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.findfer.findfer.adapter.TabsAdapter;
import br.com.findfer.findfer.dao.CoordinateDao;
import br.com.findfer.findfer.dao.RecordDao;
import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.model.Coordinates;
import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public static int typeAccount = 2;
    public static long market_place = 1;
    public static long idMarketer = 3;
    public static String relationship;
    public static Location location;
    private User user;
    private UserDao uDao;
    private CoordinateDao cDao;
    private ViewPager mViewPager;
    private String url;
    private RequestQueue requestQueue;
    private List<Poster> posters = new ArrayList<>();
    private GoogleApiClient fGoogleApiClient;
    private RecordDao rDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        url = "http://www.findfer.com.br/FindFer/control/LoadPosters.php";
        chooseStart();
        relationship = setRelationship();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("FindFer");
        user = getUser();
        user.setCoordinates(getCoordinates(user.getIdUser()));
        this.requestQueue = Volley.newRequestQueue(getApplicationContext());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Para de clicar neste botão", Snackbar.LENGTH_LONG)
                        .setAction("Não clique", null).show();*/
                if (typeAccount == 1) {
                    requestPromotion(view);
                } else {
                    newPost(view);
                }

            }
        });
        callConnection();
    }

    private String setRelationship(){
        if(typeAccount == 1){
            return "FEIRANTES";
        }
        return "CLIENTES";
    }
    private User getUser() {
        uDao = new UserDao(this);
        return uDao.getUser();
    }
    private Coordinates getCoordinates(long id){
        cDao = new CoordinateDao(this);
        return cDao.getCoordinateUser(id);
    }
    private long getMarketPlace(Coordinates coordinates){
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            return true;
        }
        if (id == R.id.action_new_user) {
            Intent addUser = new Intent(this, NewUser.class);
            startActivity(addUser);
            return true;
        }
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Configuração", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public List<Poster> loadPosters() {
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Poster post;
                    JSONArray jPosts = new JSONArray(response);
                    for (int i = 0; i < jPosts.length(); i++) {
                        JSONObject jPost = jPosts.getJSONObject(i);
                        post = new Poster(jPost.getString("title"));
                        post.setIdPoster(jPost.getLong("id_poster"));
                        post.setMarketer(jPost.getLong("id_user"));
                        post.setDescription(jPost.getString("description"));
                        post.setValue(jPost.getDouble("value"));
                        post.setMarketPlace(jPost.getLong("id_market_place"));
                        post.setUrlImage("http://www.findfer.com.br/FindFer" + jPost.getString("media_capa"));
                        post.setDate(jPost.getString("date_time"));
                        posters.add(post);
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Erro Catch: " + e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Erro Response: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("market_place", Long.toString(market_place));
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(2000,3,2));
        this.requestQueue.add(request);
        return posters;
    }


    public void newPost(View v) {
        Intent goNewPos = new Intent(this, NewPoster.class);
        startActivity(goNewPos);
    }

    public void requestPromotion(View v) {
        Intent req = new Intent(this, RequestPromotion.class);
        startActivity(req);
    }

    //LOCATION IMPLEMENTS
   private synchronized void callConnection() {
        fGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        fGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
           /* Location location = LocationServices.FusedLocationApi.getLastLocation(fGoogleApiClient);
            if(location != null){
                user.getCoordinates().setLatitude(location.getLatitude());
                user.getCoordinates().setLongitude(location.getLongitude());
                Toast.makeText(this, "Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude(), Toast.LENGTH_LONG).show();
                //Log.i("LOG","Latitude: "+location.getLatitude());
                //Log.i("LOG","Logitude: "+location.getLongitude());
            }*/
           //return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(fGoogleApiClient);
        if(location != null){
            user.getCoordinates().setLatitude(location.getLatitude());
            user.getCoordinates().setLongitude(location.getLongitude());
        }
    }

   @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void chooseStart() {
        if(verifyRecord()){
            actionRecord();
        }
    }

    private void actionRecord(){
        Intent record = new Intent(this, RecordOneActivity.class);
        startActivity(record);
    }
    private boolean verifyRecord(){
        rDao = new RecordDao(this);
        return rDao.getRecord() == null;
    }
}
