package br.com.findfer.findfer;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class FeedbackActivity extends AppCompatActivity implements Transaction{
    private EditText textFeedback;
    private Button btFeed;
    private String url;
    private User user;
    private ProgressBar pbLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        textFeedback = (EditText)findViewById(R.id.et_mensage_feedback);
        btFeed = (Button)findViewById(R.id.bt_send_feedback);
        pbLoad = (ProgressBar)findViewById(R.id.pb_load_feedback);
        url = "http://www.findfer.com.br/FindFer/control/FeedbackUser.php";
        user = getUser();
    }

    private User getUser(){
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }
    public void callVolleyRequest(){
        NetworkConnection.getInstance(this).execute(this, url);
    }
    public void goFeedback(View view){
        callVolleyRequest();
    }

    @Override
    public Map<String, String> doBefore() {
        pbLoad.setVisibility(View.VISIBLE);
        btFeed.setVisibility(View.GONE);
        if(UtilTCM.verifyConnection(this)){
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id_user",Long.toString(user.getCodUser()));
            parameters.put("mensage",textFeedback.getText().toString());
            return parameters;
        }else{
            Toast.makeText(this, "Você não está conectado à internet!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void doAfter(String response) {
        pbLoad.setVisibility(View.GONE);
        btFeed.setVisibility(View.VISIBLE);
        //Log.i("MYLOG","Valor do respopnse no edit: "+response);
        try {
            JSONArray jResponse = new JSONArray(response);
            JSONObject jREs = jResponse.getJSONObject(0);
            int res = jREs.getInt("result");
            if(res == 1){
                AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
                builder.setMessage("Muito obrigado por sua atenção, mensagem recebida.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
                builder.setMessage("Opa! Houve um erro durante o envio de sua mensagem, por favor tente novamente.");
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
