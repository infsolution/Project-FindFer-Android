package br.com.findfer.findfer;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.NetworkConnection;
import br.com.findfer.findfer.network.Transaction;

public class PosterEditActivity extends AppCompatActivity implements Transaction{
    private User user;
    private Bundle intent;
    private SimpleDraweeView imgPoster;
    private TextView title, value, description;
    private String url, newtitle, newValue, newDescription, deleted;

    private long idPoster, idMarket;
    private ProgressBar pbLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_poster_edit);
        intent = getIntent().getExtras();
        user = getUser();
        imgPoster = (SimpleDraweeView)findViewById(R.id.img_poster_edit);
        title = (TextView)findViewById(R.id.tv_edit_title_poster);
        value = (TextView)findViewById(R.id.tv_edit_value);
        description = (TextView)findViewById(R.id.tv_edit_poster_description);
        pbLoad = (ProgressBar)findViewById(R.id.pb_load_edit);
        url = "http://www.findfer.com.br/FindFer/control/UpdatePoster.php";
        idPoster = intent.getLong("id_poster");
        idMarket = intent.getLong("id_market_place");
        newtitle = "";
        newDescription = "";
        newValue = "";
        deleted = "";
        loadData();
    }
    private void loadData() {
        title.setText(intent.getString("title"));
        description.setText(intent.getString("description"));
        value.setText("R$ "+intent.getString("value"));
        loadImage(intent.getString("media_capa"), imgPoster);
    }
    public void callVolleyRequest(){
        NetworkConnection.getInstance(this).execute(this, url);
    }

    private User getUser(){
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }
    private void loadImage(String media, SimpleDraweeView image){
        Uri uri = Uri.parse(media);
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(image.getController())
                .build();
        image.setController(dc);
    }

    public void editTitle(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PosterEditActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.edit_poster_title, null);
        builder.setTitle(R.string.dial_title_edit_poster_title);
        //builder.setMessage(R.string.dial_message_request_promotion_successful);
        builder.setView(layoutView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final EditText title = (EditText) layoutView.findViewById(R.id.et_edit_new_title);
                newtitle = title.getText().toString();
                callVolleyRequest();
            }
        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(PosterEditActivity.this, R.string.event_cancel, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void editValue(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PosterEditActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.edit_poster_value, null);
        builder.setTitle(R.string.dial_title_edit_poster_value);
       // builder.setMessage(R.string.dial_message_request_promotion_successful);
        builder.setView(layoutView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final EditText value  = (EditText) layoutView.findViewById(R.id.et_edit_new_value);
                newValue = value.getText().toString();
                callVolleyRequest();
            }
        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(PosterEditActivity.this, R.string.event_cancel, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void editDescription(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PosterEditActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.edit_poster_description, null);
        builder.setTitle(R.string.dial_title_edit_poster_description);
        // builder.setMessage(R.string.dial_message_request_promotion_successful);
        builder.setView(layoutView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final EditText description  = (EditText) layoutView.findViewById(R.id.et_edit_new_description);
                newDescription = description.getText().toString();
                callVolleyRequest();
            }
        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(PosterEditActivity.this, R.string.event_cancel, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void deletePoster(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(PosterEditActivity.this);
        builder.setTitle(R.string.dial_title_delete_poster);
        // builder.setMessage(R.string.dial_message_request_promotion_successful);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleted = "1";
                callVolleyRequest();
            }
        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(PosterEditActivity.this, R.string.event_cancel, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public Map<String, String> doBefore() {
        pbLoad.setVisibility(View.VISIBLE);
        if(UtilTCM.verifyConnection(this)){
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id_poster",Long.toString(idPoster));
            parameters.put("id_market",Long.toString(idMarket));
            parameters.put("title",newtitle);
            parameters.put("description",newDescription);
            parameters.put("value",newValue);
            parameters.put("deleted", deleted);
            return parameters;
        }else{
            Toast.makeText(this, "Você não está conectado à internet!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    @Override
    public void doAfter(String response) {
        pbLoad.setVisibility(View.GONE);
        //Log.i("MYLOG","Valor do respopnse no edit: "+response);
        try {
            JSONArray jResponse = new JSONArray(response);
            JSONObject jREs = jResponse.getJSONObject(0);
            int res = jREs.getInt("result");
            if(res==1){
                AlertDialog.Builder builder = new AlertDialog.Builder(PosterEditActivity.this);
                builder.setMessage("Alteração realizada com sucesso! Em alguns instantes estará disponível.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(PosterEditActivity.this);
                builder.setMessage("Houve um erro ao editar o anúncio!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }catch (JSONException e){
            Toast.makeText(this, "Desculpe! Houve um erro, tente novamente em alguns minutos.", Toast.LENGTH_LONG).show();
        }
    }

}
