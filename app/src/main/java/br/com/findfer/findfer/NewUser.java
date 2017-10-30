package br.com.findfer.findfer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.findfer.findfer.model.Coordinates;

public class NewUser extends AppCompatActivity {
    private RadioGroup rgTypeAccount;
    private RadioButton rbClient;
    private RadioButton rbMarketer;
    private Spinner comboMaket;
    private EditText etName, etNameUser, etPassword, etFone, etEmail;
    private TextView tvImgProfile;
    private ImageButton ibtNewPhoto;
    private Button btNewUser;
    private Coordinates coordinates;
    private int typeAccount;
    private RequestQueue requestQueue;
    private String url;
    private File fileImageUser;
    private String encod, imageName;
    private Uri uriImage;
    private Bitmap bitmap;
    private String market;
    private List<String> namesMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        url = "http://www.findfer.com.br/FindFer/control/CreateUser.php";
        comboMaket = (Spinner)findViewById(R.id.combo_market);
        etName = (EditText)findViewById(R.id.et_new_user);
        etNameUser = (EditText)findViewById(R.id.et_name_user);
        etPassword  = (EditText)findViewById(R.id.et_password);
        etFone =(EditText)findViewById(R.id.et_new_fone);
        etEmail = (EditText)findViewById(R.id.et_new_email);
        tvImgProfile = (TextView)findViewById(R.id.tv_img_profile);
        ibtNewPhoto = (ImageButton)findViewById(R.id.ibt_photo_user);
        btNewUser = (Button)findViewById(R.id.bt_new_user);
        rgTypeAccount = (RadioGroup)findViewById(R.id.rg_typeAccount);
        coordinates = new Coordinates(MainActivity.location.getLatitude(),MainActivity.location.getLongitude());
        rgTypeAccount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.rb_client:
                        typeAccount = 1;
                        break;
                    case R.id.rb_marketer:
                        typeAccount = 2;
                        break;
                }
            }
        });
        List<String> listMarket = marketRequest();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listMarket);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        comboMaket.setAdapter(adapter);
    }

    public void newUser(View v){
        makeRequest();

    }
    public void newImageUser(View v){
        Intent openCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getFileUri();
        openCam.putExtra(MediaStore.EXTRA_OUTPUT, fileImageUser);
        startActivityForResult(openCam,0);
    }

    public void getFileUri(){
        imageName = setImageName();
        fileImageUser = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator + imageName);
        uriImage = Uri.fromFile(fileImageUser);
    }

    private String setImageName() {
        return etNameUser.getText().toString()+getDate()+".jpg";
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            tvImgProfile.setText(imageName);
            Bundle bundle = data.getExtras();
            bitmap =(Bitmap) bundle.get("data");
            //bitmap = BitmapFactory.decodeFile(uriImage.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte [] array = stream.toByteArray();
            encod = Base64.encodeToString(array, 0);
        }
    }

    private void makeRequest(){
        requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(NewUser.this, "Parabéns!\nVocê Já está cadastrado..."+response, Toast.LENGTH_LONG).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewUser.this, "Desculpe, houve um erro ao realizar o cadastro, tente novamnete!\n "+ error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("type_user",Integer.toString(typeAccount));
                parameters.put("latitude",Double.toString(coordinates.getLatitude()));
                parameters.put("longitude",Double.toString(coordinates.getLongitude()));
                parameters.put("name",etName.getText().toString());
                parameters.put("user_name",etNameUser.getText().toString());
                parameters.put("password",etPassword.getText().toString());
                parameters.put("email",etEmail.getText().toString());
                parameters.put("fone",etFone.getText().toString());
                parameters.put("register_date",getDate());
                parameters.put("encod_string",encod);
                parameters.put("image_name",imageName);
                return parameters;
            }
        };

        this.requestQueue.add(request);
    }
    private String getDate(){
        Date datefor = new Date();
        SimpleDateFormat dt = new SimpleDateFormat ("yyyy-MM-dd");
        return dt.format(datefor);
    }

    private boolean isMarketLocation(Location user, Location market){
        return false;
    }
    private List<String> marketRequest(){
        String urlMarket = "";
        final List<String> mkt = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, urlMarket, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(NewUser.this, "Parabéns!\nVocê Já está cadastrado..."+response, Toast.LENGTH_LONG).show();
                try {
                    JSONArray jMkts = new JSONArray(response);
                    for(int i = 0; i<jMkts.length();i++){
                        JSONObject jMkt = jMkts.getJSONObject(i);
                        mkt.add(jMkt.getString("name"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(NewUser.this, "Desculpe, houve um erro ao realizar o cadastro, tente novamnete!\n "+ error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();

                return parameters;
            }
        };

        this.requestQueue.add(request);
        return mkt;

    }


}
