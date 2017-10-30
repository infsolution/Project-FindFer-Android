package br.com.findfer.findfer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.findfer.findfer.dao.RecordDao;
import br.com.findfer.findfer.model.Record;

public class RecordTwoActivity extends AppCompatActivity {
    private Bundle intent;
    private String nameUser, code;
    private EditText fone;
    private Button newRecord;
    private Record record;
    private RecordDao rDao;
    private RequestQueue requestQueue;
    private  String url;
    private ProgressBar pbLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_two);
        this.requestQueue = Volley.newRequestQueue(getApplicationContext());
        intent = getIntent().getExtras();
        nameUser = intent.getString("name");
        pbLoad = (ProgressBar)findViewById(R.id.pb_load_record);
        fone = (EditText)findViewById(R.id.et_fone);
        newRecord = (Button)findViewById(R.id.bt_finish);
        url ="http://www.findfer.com.br/FindFer/control/NewRecord.php";
    }

    public void finishRecord(View view){
        if(getNumberFone()== null){
            Toast.makeText(this, R.string.request_fone, Toast.LENGTH_SHORT).show();
        }else{
            pbLoad.setVisibility(View.VISIBLE);
            createRecordRemote();
            pbLoad.setVisibility(View.GONE);
        }
    }
    private void creatRecordLocal(Record record){
        rDao = new RecordDao(this);
        long res = rDao.insertRecord(record);
        if(res > 0){
            Toast.makeText(this, R.string.answer_ok_new_record, Toast.LENGTH_SHORT).show();
        }
        if(res == -1){
            Toast.makeText(this, R.string.user_existent, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, R.string.answer_not_new_record, Toast.LENGTH_SHORT).show();
        }

    }
    public void createRecordRemote(){
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               try {
                    JSONArray jRecords = new JSONArray(response);
                    for (int i = 0; i < jRecords.length(); i++) {
                        JSONObject jRecord = jRecords.getJSONObject(i);
                        record = new Record(jRecord.getString("fone"));
                        record.setName("name");
                        record.setCode("code");
                        creatRecordLocal(record);
                    }
                } catch (JSONException e) {
                    Toast.makeText(RecordTwoActivity.this, "Erro Catch: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecordTwoActivity.this, "Erro Response: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("name",nameUser);
                parameters.put("fone",getNumberFone());
                return parameters;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(2000,3,2));
        this.requestQueue.add(request);

    }
    private String  getNumberFone(){
        if(!veriNumber()){
            return fone.getText().toString();
        }
        return null;
    }
    private boolean veriNumber(){
        return fone.getText().toString().equals("") || fone.getText().toString().equals(" ");
    }
}
