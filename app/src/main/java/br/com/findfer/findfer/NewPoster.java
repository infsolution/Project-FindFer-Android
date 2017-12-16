package br.com.findfer.findfer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarException;

import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.extras.UtilTCM;
import br.com.findfer.findfer.model.Coordinates;
import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.NetworkConnection;
import br.com.findfer.findfer.network.Transaction;

public class NewPoster extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, Transaction{
    private Poster poster;
    private int optCoupon = 0;
    private ImageButton imgBtt;
    private UserDao uDao;
    private EditText etTitle, etDescri, etValue,etValueCoupon;
    private TextView tvNameImage;
    private RadioButton rbYes;
    private RadioButton rbNot;
    private String url;
    private User user;
    private File fileImagePoster;
    private String encod, imageName;
    private Bitmap bitmap;
    private Uri uriImage;
    private GoogleApiClient fGoogleApiClient;
    private Location location;
    private LocationRequest locationRequest;
    private Coordinates coordinates;
    private ProgressBar progressBar;
    private Button btCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poster);
        url="http://www.findfer.com.br/FindFer/control/ObservablePoster.php";
        user = getUser();
        etTitle = (EditText)findViewById(R.id.et_titulo_new_poster);
        etDescri =(EditText)findViewById(R.id.et_new_description);
        etValue =(EditText)findViewById(R.id.et_new_value);
        etValueCoupon = (EditText)findViewById(R.id.et_coupon_value);
        progressBar = (ProgressBar)findViewById(R.id.pb_load_new_poster);
        tvNameImage = (TextView)findViewById(R.id.tv_set_image);
        imgBtt = (ImageButton)findViewById(R.id.ib_camera);
        btCreate = (Button)findViewById(R.id.bt_new_post);
        rbYes = (RadioButton)findViewById(R.id.rb_yes);
        rbNot = (RadioButton)findViewById(R.id.rb_no);
        rbNot.setChecked(true);
        final RadioGroup rgWanser = (RadioGroup)findViewById(R.id.rg_new_coupon);
        rgWanser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.rb_yes:
                        optCoupon=1;
                        etValueCoupon.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_no:
                        optCoupon=0;
                        etValueCoupon.setVisibility(View.GONE);
                        break;
                }
            }
        });
        callConnection();
    }
    private User getUser(){
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }
    public void creatPoster(View view) {
        startLocationUpdate();
        callVolleyRequest();
            }
    public void callVolleyRequest(){
        NetworkConnection.getInstance(this).execute(this, url);
    }
    public void newImagePost(View view){
        Intent openCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getFileUri();
        openCam.putExtra(MediaStore.EXTRA_OUTPUT, fileImagePoster);
        startActivityForResult(openCam,0);
    }
    public void getFileUri(){
        imageName = setImageName();
        fileImagePoster = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator + imageName);
        uriImage = Uri.fromFile(fileImagePoster);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK){
            tvNameImage.setText(imageName);
            Bundle bundle = data.getExtras();
            bitmap =(Bitmap) bundle.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte [] array = stream.toByteArray();
            encod = Base64.encodeToString(array, 0);
            imgBtt.setImageBitmap(bitmap);
        }
    }
    private String setImageName() {
        String title = etTitle.getText().toString().toLowerCase().replace(" ","");
        Log.i("LOG",title);
        return title+user.getCodUser()+getValidity()+".jpg";
    }
    private String getValidity(){
        Date datefor = new Date();
        SimpleDateFormat dt = new SimpleDateFormat ("yyyy-MM-dd");
        return dt.format(datefor);
    }
    private void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private void startLocationUpdate() {
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(fGoogleApiClient, locationRequest, NewPoster.this);
    }
    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(fGoogleApiClient,NewPoster.this);
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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("FINDFERLOG","New Poster- Latitude onChangeed: "+location.getLatitude() +"\nLongitude onChangeed: "+location.getLongitude());
        if(coordinates == null){
            coordinates = new Coordinates(location.getLatitude(), location.getLongitude());
        }

    }
    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(fGoogleApiClient);
        startLocationUpdate();
        if(location != null){
            coordinates = new Coordinates(location.getLatitude(), location.getLongitude());
            Toast.makeText(this, "Latitude: "+coordinates.getLatitude()+" Longitude: "+coordinates.getLongitude(), Toast.LENGTH_SHORT).show();
        }
        stopLocationUpdate();
    }
    @Override
    public Map<String, String> doBefore() {
        progressBar.setVisibility(View.VISIBLE);
        btCreate.setVisibility(View.GONE);
        Log.i("LOG","doBefore NewPoster");
        if(UtilTCM.verifyConnection(this)){
            if(encod.equals("") || encod == null){
                return null;
            }else{
            Map<String, String> parameters = new HashMap<>();
            parameters.put("encod_string", encod);
            parameters.put("market_place",Long.toString(user.getIdMarket()));
            parameters.put("marketer",Long.toString(user.getCodUser()));
            parameters.put("image_name",imageName);
            parameters.put("title",etTitle.getText().toString());
            parameters.put("description",etDescri.getText().toString());
            parameters.put("value",etValue.getText().toString());
            parameters.put("add_coupon",Integer.toString(optCoupon));
            parameters.put("desconto",etValueCoupon.getText().toString());
            parameters.put("validity",getValidity());
            parameters.put("latitude",Double.toString(coordinates.getLatitude()));
            parameters.put("longitude",Double.toString(coordinates.getLongitude()));
            return parameters;
            }
        }else{
            Toast.makeText(this, "Sem conexão!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void doAfter(String response) {
        progressBar.setVisibility(View.GONE);
        btCreate.setVisibility(View.VISIBLE);
        int result = 0;
        if(response != null){
            try{
                JSONArray res = new JSONArray(response);
                JSONObject obj = res.getJSONObject(0);
                result = obj.getInt("result");
                if(result == -1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewPoster.this);
                    builder.setTitle(R.string.dial_title_alert_no_market);
                    builder.setMessage(R.string.dial_message_poster_no_is_market);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewPoster.this);
                    builder.setTitle(R.string.dial_title_congratuletion);
                    builder.setMessage(R.string.dial_message_poster_successfull);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }catch (JSONException e){
                Log.i("ERRORLOG",e.toString());
            }
        }
    }




}
