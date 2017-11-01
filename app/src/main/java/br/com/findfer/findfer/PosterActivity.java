package br.com.findfer.findfer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class PosterActivity extends AppCompatActivity {
    private Bundle intent;
    private TextView tvTitle, tvValue, tvMarket, tvDescription, tvPosterDate;
    private ImageButton imbtReqRelation, imbtProfile, imbtReqPromotion;
    private ImageView imgPoster;
    private RatingBar rtbQualifcation;
    private Button btSale, btLocation;
    private String mediaCapa;
    private long idMarketer, idMarket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);
        intent = getIntent().getExtras();
        tvTitle = (TextView)findViewById(R.id.tv_title_poster);
        tvValue = (TextView)findViewById(R.id.tv_value);
        tvMarket = (TextView)findViewById(R.id.tv_market_name);
        tvDescription = (TextView)findViewById(R.id.tv_poster_description);
        tvPosterDate = (TextView)findViewById(R.id.tv_date_poster);
        imbtProfile = (ImageButton)findViewById(R.id.imb_profile_poster);
        imbtReqPromotion = (ImageButton)findViewById(R.id.imbt_request_promotion);
        imbtReqRelation = (ImageButton)findViewById(R.id.imbt_request_relatioship);
        imgPoster = (ImageView)findViewById(R.id.img_poster);
        btLocation = (Button)findViewById(R.id.bt_location_market);
        btSale = (Button)findViewById(R.id.bt_sale);
        mediaCapa = intent.getString("media_capa");
        idMarketer = intent.getLong("id_marketer");
        idMarket = intent.getLong("id_market_place");
        loadData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
    private void loadData() {
        tvTitle.setText(intent.getString("title"));
        tvDescription.setText(intent.getString("description"));
        tvValue.setText(intent.getString("value"));
        tvPosterDate.setText(formatDate(intent.getString("date_time")));
    }
    private String formatDate(String date){

        return "";
    }

}
