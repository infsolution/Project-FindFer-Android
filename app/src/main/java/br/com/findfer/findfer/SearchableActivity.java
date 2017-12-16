package br.com.findfer.findfer;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.findfer.findfer.adapter.PosterAdapter;
import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.extras.UtilTCM;
import br.com.findfer.findfer.interfaces.RecyclerViewOnClickListenerHack;
import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.NetworkConnection;
import br.com.findfer.findfer.network.Transaction;

public class SearchableActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack, Transaction{
    private User user;
    private RecyclerView pRecyclerView;
    private PosterAdapter pAdapter;
    private List<Poster> posters;
    private ProgressBar pbLoad;
    private String url, query;
    private CoordinatorLayout clContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_searchable);
        user = getUser();
        clContainer = (CoordinatorLayout) findViewById(R.id.cl_container);
        url = "http://www.findfer.com.br/FindFer/control/LoadPosters.php";
        pbLoad = (ProgressBar) findViewById(R.id.pb_search);

        hendleSearch(getIntent());
        posters = new ArrayList<>();
        pRecyclerView = (RecyclerView) findViewById(R.id.rv_search);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        pRecyclerView.setLayoutManager(llm);
        callVolleyRequest();
        pAdapter = new PosterAdapter(this, posters);
        pAdapter.setRecyclerViewOnClickListenerHack(this);
        pRecyclerView.setAdapter(pAdapter);

    }
    private User getUser(){
        UserDao uDao = new UserDao(this);
        return uDao.getUser();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        hendleSearch(intent);
        callVolleyRequest();
    }

    public void hendleSearch(Intent intent){
        if(intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())){
            query = intent.getStringExtra(SearchManager.QUERY);
        }
    }



    public void callVolleyRequest(){
        NetworkConnection.getInstance(this).execute(this, url);
    }
    @Override
    public Map<String, String> doBefore() {
        pbLoad.setVisibility(View.VISIBLE);
        if(UtilTCM.verifyConnection(this)){
            Map<String, String> parameters = new HashMap<>();
            parameters.put("latitude",Double.toString(user.getActualLatitude()));
            parameters.put("longitude",Double.toString(user.getActualLongitude()));
            parameters.put("query",query);
            parameters.put("id_user",Long.toString(MainActivity.user.getCodUser()));
            return parameters;
        }else{
            Toast.makeText(this, "Você não está conectado à internet.", Toast.LENGTH_SHORT).show();
            finish();
        }
        return null;
    }

    @Override
    public void doAfter(String response) {
        Log.i("LOG","Response no doAfter:"+response);
        pbLoad.setVisibility(View.GONE);
        posters = loadPosters(response);
        Log.i("LOG","poster: "+posters.size());
        if( posters.size() > 0){
            pAdapter = new PosterAdapter(this,posters);
            pAdapter.setRecyclerViewOnClickListenerHack(this);
            pRecyclerView.setAdapter(pAdapter);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchableActivity.this);
            builder.setTitle(R.string.dial_searcg_resul);
            builder.setMessage(R.string.tv_result_no_result_search);
            builder.setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    return;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }/*else{
            TextView tv = new TextView( this );
            tv.setText( "Sua busca não encontrou nenhum resultado!" );
            tv.setTextColor( getResources().getColor( R.color.black ) );
            tv.setId( 1 );
            tv.setLayoutParams( new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT )  );
            tv.setGravity(Gravity.CENTER);
            clContainer.addView( tv );
        }
         if( clContainer.findViewById(1) != null && posters.size() == 0) {
            clContainer.removeView( clContainer.findViewById(1) );
        }*/

        pAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClickListner(View view, int position) {
        Intent poster = new Intent(this, PosterActivity.class);
        poster.putExtra("id_poster",posters.get(position).getIdPoster());
        poster.putExtra("id_marketer",posters.get(position).getMarketer());
        poster.putExtra("id_market_place", posters.get(position).getMarketPlace());
        poster.putExtra("title",posters.get(position).getTitle());
        poster.putExtra("description", posters.get(position).getDescription());
        poster.putExtra("value", posters.get(position).getStringValue());
        poster.putExtra("media_capa",posters.get(position).getUrlImage());
        poster.putExtra("date_time", posters.get(position).getDate());
        poster.putExtra("name_market",posters.get(position).getNameMarket());
        poster.putExtra("name_user",posters.get(position).getNameUser());
        poster.putExtra("media",posters.get(position).getUrlImageUser());
        poster.putExtra("flag_visible","");
        startActivity(poster);

    }
    private List<Poster> loadPosters(String response){
        List<Poster> posts = new ArrayList<>();
        try {
            Poster post;
            JSONArray jPosts = new JSONArray(response);
            for (int i = 0; i < jPosts.length(); i++) {
                JSONObject jPost = jPosts.getJSONObject(i);
                post = new Poster(jPost.getString("title"));
                post.setIdPoster(jPost.getLong("id_poster"));
                post.setMarketer(jPost.getLong("id_user"));
                post.setDescription(jPost.getString("description"));
                post.setValue(jPost.getDouble("value"));
                post.setMarketPlace(jPost.getLong("id_market_place"));
                post.setUrlImage("http://www.findfer.com.br/FindFer" + jPost.getString("media_capa"));
                post.setDate(jPost.getString("date_time"));
                JSONObject us = jPost.getJSONObject("0");
                post.setNameUser(us.getString("name_user"));
                post.setUrlImageUser("http://www.findfer.com.br/FindFer" + us.getString("media"));
                post.setNameMarket(us.getString("name_market"));
                posts.add(post);
            }

        } catch (JSONException e) {
            Toast.makeText(this, "Erro Catch: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return posts;
    }
}
