package br.com.findfer.findfer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

import br.com.findfer.findfer.network.NetworkConnection;
import br.com.findfer.findfer.network.Transaction;

public class RequestPromotion extends AppCompatActivity implements Transaction{
    private EditText etProduct, ageValue;
    private Button btRequest;
    private String url;
    private Bundle intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_promotion);
        intent = getIntent().getExtras();
        etProduct = (EditText)findViewById(R.id.et_prodct_promotion);
        btRequest = (Button)findViewById(R.id.bt_request_promotion);
        ageValue = (EditText)findViewById(R.id.et_value_promotion);
        url = "http://www.findfer.com.br/FindFer/control/ObservableRequestPromotion.php";
    }

    public void requestPromotion(View view){
        if(etProduct.getText().equals("") || ageValue.getText().equals("")){
            callVolleyRequest();
        }else{
            Toast.makeText(this, "Os campos Produto e Valor devem ser prenchidos.", Toast.LENGTH_SHORT).show();
        }
    }
    public void callVolleyRequest(){
        NetworkConnection.getInstance(this).execute(this, url);
    }

    @Override
    public Map<String, String> doBefore() {

        return null;
    }

    @Override
    public void doAfter(String response) {

    }
}
