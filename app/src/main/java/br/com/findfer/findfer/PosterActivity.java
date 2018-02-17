package br.com.findfer.findfer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.extras.UtilTCM;
import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.NetworkConnection;
import br.com.findfer.findfer.network.Transaction;

public class PosterActivity extends AppCompatActivity implements Transaction{
    private User user;
    private Bundle intent;
    private ProgressBar pbLoad;
    private TextView tvTitle, tvValue, tvMarket, tvDescription, tvPosterDate, tvNameUser;
    private ImageButton imbtProfile, imbtReqPromotion;
    private SimpleDraweeView imgPoster, imgProfile;
    private RatingBar rtbQualifcation;
    private Button btSale, btLocation;
    private String mediaCapa, url, nameUser, nameMarket, mediaUser, qualification;
    private long idMarketer, idMarket, idPoster;
    private double ageValue;
    private EditText egValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_poster);
        intent = getIntent().getExtras();
        user = getUser();
        pbLoad = (ProgressBar)findViewById(R.id.pb_load_poster);
        //pbLoad.setVisibility(View.VISIBLE);
        tvTitle = (TextView)findViewById(R.id.tv_title_poster);
        tvValue = (TextView)findViewById(R.id.tv_value);
        tvMarket = (TextView)findViewById(R.id.tv_market_name);
        tvDescription = (TextView)findViewById(R.id.tv_poster_description);
        tvPosterDate = (TextView)findViewById(R.id.tv_date_poster);
        tvNameUser = (TextView)findViewById(R.id.tv_name_user);
        imbtReqPromotion = (ImageButton)findViewById(R.id.imbt_request_promotion);
        imgPoster = (SimpleDraweeView)findViewById(R.id.img_poster);
        imgProfile = (SimpleDraweeView)findViewById(R.id.img_profile_poster);
        btLocation = (Button)findViewById(R.id.bt_location_market);
        btSale = (Button)findViewById(R.id.bt_sale);
        btSale.setActivated(false);
        mediaCapa = intent.getString("media_capa");
        idMarketer = intent.getLong("id_marketer");
        idMarket = intent.getLong("id_market_place");
        idPoster = intent.getLong("id_poster");
        mediaUser = intent.getString("media");
        nameUser = intent.getString("name_user");
        nameMarket = intent.getString("name_market");
        ageValue = 0.0;
        url = "http://www.findfer.com.br/FindFer/control/ObservableRequestPromotion.php";
        loadData();

    }
    private User getUser(){
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
    private void loadData() {
        tvTitle.setText(intent.getString("title"));
        tvDescription.setText(intent.getString("description"));
        tvValue.setText("R$ "+intent.getString("value"));
        tvPosterDate.setText(formatDate(intent.getString("date_time")));
        tvMarket.setText(nameMarket);
        tvNameUser.setText(nameUser);
        loadImage(mediaUser,imgProfile);
        loadImage(mediaCapa, imgPoster);
    }
    private String formatDate(String date){
        return date.substring(8,10)+"/"+date.substring(5,7)+"/"+date.substring(0,4)+" "+date.substring(12,19);
    }

    public void requestPromotionDirect(View view){
        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        LayoutInflater inf = LayoutInflater.from(this);
        final View layoutView = inf.inflate(R.layout.dialod_value_promotion,null);
        egValue = (EditText)layoutView.findViewById(R.id.et_dialog_value_promotion);
        buider.setView(layoutView);
        buider.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                if(egValue.getText().toString().equals("")){
                    Toast.makeText(PosterActivity.this, R.string.inform_value, Toast.LENGTH_SHORT).show();
                }else{
                ageValue = Double.valueOf(egValue.getText().toString());
                callVolleyRequest();
                }
            }
        }).setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Toast.makeText(PosterActivity.this, R.string.event_cancel, Toast.LENGTH_SHORT).show();
            }
        });
        buider.show();
    }
    public void callVolleyRequest(){
        NetworkConnection.getInstance(this).execute(this, url);
    }
    public void goProfileMarketer(View view){
        Intent goProfile = new Intent(this, ProfileActivity.class);
        goProfile.putExtra("id_marketer",idMarketer);
        goProfile.putExtra("media_profile",mediaUser);
        goProfile.putExtra("name_user",nameUser);
        goProfile.putExtra("name_market",nameMarket);
        goProfile.putExtra("date_time",formatDate(intent.getString("date_time")));
        goProfile.putExtra("status_request_relationship",true);
        startActivity(goProfile);

    }
    private void loadImage(String media, SimpleDraweeView image){
        Uri uri = Uri.parse(media);
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(image.getController())
                .build();
        image.setController(dc);
    }

    public void goLocationMarket(View view){
        Intent goProile = new Intent(this, Maps.class);
        goProile.putExtra("lati_market",-5.0882245);
        goProile.putExtra("longi_market",-42.8035754);
        goProile.putExtra("lati_user",Double.toString(user.getActualLatitude()));
        goProile.putExtra("longi_user",Double.toString(user.getActualLongitude()));
        startActivity(goProile);
    }

    public void toBuy(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PosterActivity.this);
        builder.setTitle(R.string.dial_title_function_development);
        builder.setMessage(R.string.dial_message_function_in_development);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public Map<String, String> doBefore() {
        pbLoad.setVisibility(View.VISIBLE);
        imbtReqPromotion.setVisibility(View.GONE);
        //Log.i("LOG","Abriu o doBefore posterActivity");
        if(UtilTCM.verifyConnection(this)){
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id_user",Long.toString(user.getCodUser()));
            parameters.put("id_marketer",Long.toString(idMarketer));
            parameters.put("product",tvTitle.getText().toString());
            parameters.put("age_value",Double.toString(ageValue));
           // Log.i("LOG","valor do ageValue: "+ageValue);
            return parameters;
        }else{
            Toast.makeText(this, "Você não está conectado à internet!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    @Override
    public void doAfter(String response) {
        pbLoad.setVisibility(View.GONE);
        imbtReqPromotion.setVisibility(View.VISIBLE);
        int result = 0;
        if(response != null){
            try{
                JSONArray res = new JSONArray(response);
                JSONObject obj = res.getJSONObject(0);
                result = obj.getInt("resultado");
                if(result > 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PosterActivity.this);
                    builder.setTitle(R.string.dial_title_request_promotion);
                    builder.setMessage(R.string.dial_message_request_promotion_successful);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            imbtReqPromotion.setClickable(false);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(this, "Houve um erro em sua solicitação! Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                //Log.i("ERRORLOG",e.toString());
                Toast.makeText(this, "Houve um erro em sua solicitação! Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
