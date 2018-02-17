package br.com.findfer.findfer.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.findfer.findfer.NewMarketer;
import br.com.findfer.findfer.PosterActivity;
import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.extras.LocationUser;
import br.com.findfer.findfer.extras.UtilTCM;
import br.com.findfer.findfer.interfaces.FragmentComunication;
import br.com.findfer.findfer.interfaces.RecyclerViewOnClickListenerHack;
import br.com.findfer.findfer.model.Coordinates;
import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.adapter.PosterAdapter;
import br.com.findfer.findfer.MainActivity;
import br.com.findfer.findfer.R;
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.NetworkConnection;
import br.com.findfer.findfer.network.Transaction;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by infsolution on 25/09/17.
 */

public class PosterFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction{
    private RecyclerView pRecycle;
    private List<Poster> posters;
    private ProgressBar pBLoad;
    private View view;
    protected LinearLayoutManager llM;
    private String url;
    private PosterAdapter adapter;
    private User user;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = "http://www.findfer.com.br/FindFer/control/LoadPosters.php";
        posters = new ArrayList<>();
        user = getUser();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_poster, container, false);
        view.setTag("PosterFragment");
        pBLoad = (ProgressBar)view.findViewById(R.id.pb_load_frag_poster);
        pRecycle = (RecyclerView) view.findViewById(R.id.rv_list);
        pRecycle.setHasFixedSize(true);
        pRecycle.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView,dx,dy);
                //LinearLayoutManager llM = (LinearLayoutManager)pRecycle.getLayoutManager();
                //PosterAdapter pAdapter = (PosterAdapter)pRecycle.getAdapter();
                /*if(posters.size() == llM.findLastCompletelyVisibleItemPosition() + 1){
                    List<Poster> aux = ((MainActivity)getActivity()).loadPosters();
                    pBLoad.setVisibility(View.GONE);
                    for (int i = 0; i < aux.size();i++){
                        pAdapter.addListItem(aux.get(i), posters.size());
                    }
                }*/
            }
        });

        llM = new LinearLayoutManager(getActivity()); //TODO Descomentar
        llM.setOrientation(LinearLayoutManager.VERTICAL);
        pRecycle.setLayoutManager(llM);
        callVolleyRequest();
        adapter = new PosterAdapter(getActivity(),posters);
        adapter.setRecyclerViewOnClickListenerHack(this);
        pRecycle.setAdapter(adapter);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    private User getUser(){
        UserDao uDao = new UserDao(getActivity());
        return uDao.getUser();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("poster", (ArrayList<Poster>) posters);
    }
    @Override
    public void onClickListner(View view, int position) {
        Intent poster = new Intent(getActivity(), PosterActivity.class);
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

    @Override
    public void onStop() {
        super.onStop();
        }

    public void callVolleyRequest(){
        NetworkConnection.getInstance(getActivity()).execute(this, url);
    }

    @Override
    public Map<String, String> doBefore() {
        //Log.i("MYLOG","abriu o doBefore de Poster");
        pBLoad.setVisibility(View.VISIBLE);
            if(UtilTCM.verifyConnection(getActivity())){
                 Map<String, String> parameters = new HashMap<>();
                if(user ==  null){
                    parameters.put("latitude",Double.toString(0));
                    parameters.put("longitude",Double.toString(0));
                    parameters.put("query","");
                    parameters.put("id_user",Long.toString(0));
                }else{
                    parameters.put("latitude",Double.toString(user.getActualLatitude()));
                    parameters.put("longitude",Double.toString(user.getActualLongitude()));
                    parameters.put("query","");
                    parameters.put("id_user",Long.toString(user.getCodUser()));
                }
            return parameters;
        }else{
                pBLoad.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Você não está conectado à internet!", Toast.LENGTH_SHORT).show();
            }
        return null;
    }
    @Override
    public void doAfter(String response) {
        //Log.i("MYLOG","abriu o doAfter poster");
        //Log.i("MYLOG","Valor do response de poster: "+response);
        pBLoad.setVisibility(View.GONE);
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
                posters.add(post);
            }
            adapter = new PosterAdapter(getActivity(),posters);
            adapter.setRecyclerViewOnClickListenerHack(this);
            pRecycle.setAdapter(adapter);
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Desculpe! houve um erro ao carregar os anúncios, por favor, tente novamente em alguns minutos.", Toast.LENGTH_LONG).show();
        }
    }
}

