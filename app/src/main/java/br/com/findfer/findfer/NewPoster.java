package br.com.findfer.findfer;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.network.StringRequet;

public class NewPoster extends AppCompatActivity {
    private Poster poster;
    private int optCoupon = 0;
    private ImageButton imgBtt;
    private EditText etTitle, etDescri, etValue,etValueCoupon;
    private TextView tvNameImage;
    //private final RadioGroup rgWanser;
    private RadioButton rbYes;
    private RadioButton rbNot;
    private String url="http://www.findfer.com.br/FindFer/control/ObservablePoster.php";
    private RequestQueue requestQueue;

    private File fileImagePoster;
    private String encod, imageName;
    private Bitmap bitmap;
    private Uri uriImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poster);
        etTitle = (EditText)findViewById(R.id.et_titulo_new_poster);
        etDescri =(EditText)findViewById(R.id.et_new_description);
        etValue =(EditText)findViewById(R.id.et_new_value);
        etValueCoupon = (EditText)findViewById(R.id.et_coupon_value);
        tvNameImage = (TextView)findViewById(R.id.tv_set_image);
        imgBtt = (ImageButton)findViewById(R.id.ib_camera);
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


    }

    public void creatPoster(View view) {
        //new EncodeImage().execute();
        makeRequest();
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
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK){
            tvNameImage.setText(imageName);
            Bundle bundle = data.getExtras();
            bitmap =(Bitmap) bundle.get("data");
            //bitmap = BitmapFactory.decodeFile(uriImage.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte [] array = stream.toByteArray();
            encod = Base64.encodeToString(array, 0);
        }
    }

    private String setImageName() {
        return etTitle.getText().toString()+getValidity()+".jpg";
    }

    private class EncodeImage extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            /*bitmap = BitmapFactory.decodeFile(uriImage.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);//NULL POINT
            byte [] array = stream.toByteArray();
            encod = Base64.encodeToString(array, 0);*/
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest();
        }

    }
    private void makeRequest(){
        requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(NewPoster.this, "Parabéns!\nAnúncio criado com sucesso..."+response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewPoster.this, "Desculpe, houve um erro ao criar seus anúncio, tente novamnete!\n "+ error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("encod_string", encod);
                parameters.put("market_place",Long.toString(MainActivity.market_place));
                parameters.put("marketer",Long.toString(MainActivity.idMarketer));
                parameters.put("image_name",imageName);
                parameters.put("title",etTitle.getText().toString());
                parameters.put("description",etDescri.getText().toString());
                parameters.put("value",etValue.getText().toString());
                parameters.put("add_coupon",Integer.toString(optCoupon));
                parameters.put("desconto",etValueCoupon.getText().toString());
                parameters.put("validity",getValidity());
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(2000,3,2));
        this.requestQueue.add(request);
    }
    private String getValidity(){
        Date datefor = new Date();
        SimpleDateFormat dt = new SimpleDateFormat ("yyyy-MM-dd");
        return dt.format(datefor);
    }
}
