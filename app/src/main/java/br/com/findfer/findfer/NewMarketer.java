package br.com.findfer.findfer;

        import android.Manifest;
        import android.content.DialogInterface;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.DefaultRetryPolicy;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.LocationListener;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Map;

        import br.com.findfer.findfer.dao.UserDao;
        import br.com.findfer.findfer.model.Coordinates;
        import br.com.findfer.findfer.model.Market;
        import br.com.findfer.findfer.model.User;

public class NewMarketer extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private User user;
    private Spinner comboMaket;
    private EditText etName, etconfirPassword, etPassword, etFone, etEmail;
    private Button btNewUser;
    private ProgressBar pbLoadMarket;
    private UserDao uDao;
    private GoogleApiClient fGoogleApiClient;
    private Location location;
    private LocationRequest locationRequest;
    private Coordinates coordinates;
    private String url;
    private Market market;
    private Double distance;
    private int itSInMarket;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_marketer);
        user = getUser();
        comboMaket = (Spinner) findViewById(R.id.combo_market);
        etEmail = (EditText) findViewById(R.id.et_new_email);
        etPassword = (EditText) findViewById(R.id.et_new_passwword);
        etconfirPassword = (EditText) findViewById(R.id.et_confirm_password);
        pbLoadMarket = (ProgressBar) findViewById(R.id.pb_load_market);
        btNewUser = (Button) findViewById(R.id.bt_new_user);
        this.requestQueue = Volley.newRequestQueue(getApplicationContext());
        url = "http://www.findfer.com.br/FindFer/control/CreateMarketer.php";
        //url = "http://192.168.42.132/findfer/control/CreateMarketer.php";
        callConnection();
    }

    private User getUser() {
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(fGoogleApiClient != null){
            stopLocationUpdate();
        }
    }

    protected boolean comparePasswords(String pass1, String pass2) {
        return pass1.equals(pass2);
    }

    private String getDate() {
        Date datefor = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        return dt.format(datefor);
    }

    public void updateUser(View view) {
        pbLoadMarket.setVisibility(View.VISIBLE);
        if (comparePasswords(etPassword.getText().toString(), etconfirPassword.getText().toString())) {
            updateRemote();
        } else {
            Toast.makeText(this, R.string.dif_pass, Toast.LENGTH_SHORT).show();
        }
    }

    protected long newMarketer(String email, String password, long idMarket) {
        user.setEmail(email);
        user.setPassword(password);
        user.setIdMarket(idMarket);
        user.setTypeAccount(2);
        uDao = new UserDao(this);
        return uDao.updateUser(user);
    }

    private void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate() {
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(fGoogleApiClient, locationRequest, NewMarketer.this);
    }
    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(fGoogleApiClient,NewMarketer.this);
    }
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
        location = LocationServices.FusedLocationApi.getLastLocation(fGoogleApiClient);
        startLocationUpdate();
    }

    private void updateRemote(){
        if(coordinates == null) {
            startLocationUpdate();
        }
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbLoadMarket.setVisibility(View.GONE);
                try {
                    JSONArray jUpdates = new JSONArray(response);
                    for (int i = 0; i < jUpdates.length(); i++) {
                        JSONObject jUpdate = jUpdates.getJSONObject(i);
                        market = new Market(jUpdate.getString("name"));
                        market.setIdMarket(jUpdate.getLong("id_market"));
                        distance = jUpdate.getDouble("distance");
                        itSInMarket = jUpdate.getInt("itsinmarket");
                        if(itSInMarket == 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewMarketer.this);
                            builder.setTitle(R.string.dial_title_alert_no_market);
                            builder.setMessage(R.string.dial_message_no_is_market);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                        if(newMarketer(etPassword.getText().toString(), etconfirPassword.getText().toString(),market.getIdMarket())>0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewMarketer.this);
                            builder.setTitle(R.string.dial_title_new_record);
                            builder.setMessage(R.string.dial_message_ok_record);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    stopLocationUpdate();
                                    finish();
                                    return;
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            }
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(NewMarketer.this, "Houve um erro em sua solicitação!\nCodigo do erro: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewMarketer.this, "Erro Response: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("email",etEmail.getText().toString());
                parameters.put("latitude",Double.toString(coordinates.getLatitude()));
                parameters.put("longitude",Double.toString(coordinates.getLongitude()));
                parameters.put("password",etconfirPassword.getText().toString());
                parameters.put("id_user",Long.toString(user.getCodUser()));
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(2000,3,2));
        this.requestQueue.add(request);

    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {
        Log.i("LOG","Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude());
        if(coordinates == null){
            coordinates = new Coordinates(location.getLatitude(), location.getLongitude());
        }
        stopLocationUpdate();
    }
}