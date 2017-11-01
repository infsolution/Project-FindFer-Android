package br.com.findfer.findfer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import br.com.findfer.findfer.PosterActivity;
import br.com.findfer.findfer.ProfileActivity;
import br.com.findfer.findfer.extras.UtilTCM;
import br.com.findfer.findfer.interfaces.RecyclerViewOnClickListenerHack;
import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.adapter.PosterAdapter;
import br.com.findfer.findfer.MainActivity;
import br.com.findfer.findfer.R;
import br.com.findfer.findfer.model.WrapObjToNetwork;
import br.com.findfer.findfer.network.NetworkConncetion;
import br.com.findfer.findfer.network.Transaction;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by infsolution on 25/09/17.
 */

public class PosterFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction {
    private RecyclerView pRecycle;
    private List<Poster> posters;
    private ProgressBar pBLoad;
    private boolean bLoad;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_poster, container, false);
        posters = new ArrayList<>();
        pBLoad = (ProgressBar)view.findViewById(R.id.pb_load);
        pRecycle = (RecyclerView) view.findViewById(R.id.rv_list);
        pRecycle.setHasFixedSize(true);
        //pBLoad.setVisibility(View.VISIBLE);
        pRecycle.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView,dx,dy);
                LinearLayoutManager llM = (LinearLayoutManager)pRecycle.getLayoutManager();
                PosterAdapter pAdapter = (PosterAdapter)pRecycle.getAdapter();
                if(posters.size() == llM.findLastCompletelyVisibleItemPosition() + 1){
                    List<Poster> aux = ((MainActivity)getActivity()).loadPosters();
                    //pBLoad.setVisibility(View.GONE);
                    for (int i = 0; i < aux.size();i++){
                        pAdapter.addListItem(aux.get(i), posters.size());
                    }
                }
            }
        });

        LinearLayoutManager llM = new LinearLayoutManager(getActivity()); //TODO Descomentar
        llM.setOrientation(LinearLayoutManager.VERTICAL);
        pRecycle.setLayoutManager(llM);
        posters = ((MainActivity)getActivity()).loadPosters();
        if(posters.size()>0){
            //pBLoad.setVisibility(View.GONE);
        }
        PosterAdapter adapter = new PosterAdapter(getActivity(),posters);
        adapter.setRecyclerViewOnClickListenerHack(this);
        pRecycle.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /*  @Override
        public void onActivityCreated(Bundle savedInstanceState){
             super.onActivityCreated(savedInstanceState);
             if(savedInstanceState != null){
                 posters = savedInstanceState.getParcelableArrayList("poster");
                PosterAdapter adapter = new PosterAdapter(getActivity(), posters);
                 pRecycle.setAdapter(adapter);
                 if(posters == null || posters.size() == 0){
                     callVolleyRequest();
                 }
             }else{
                 callVolleyRequest();
             }

         }*/
    public void callVolleyRequest(){
        NetworkConncetion.getInstance(getActivity()).execute(this,PosterFragment.class.getName());
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
        poster.putExtra("value", posters.get(position).getValue());
        poster.putExtra("media_capa",posters.get(position).getUrlImage());
        poster.putExtra("date_time", posters.get(position).getDate());
        startActivity(poster);

    }

    @Override
    public void onStop() {
        super.onStop();
        NetworkConncetion.getInstance(getActivity()).getRequestQueue().cancelAll(PosterFragment.class.getName());
    }

    //Network
    @Override
    public WrapObjToNetwork toBefore() {
        pBLoad.setVisibility(View.VISIBLE);
        if(UtilTCM.verifyConnection(getActivity())){
            Poster poster = new Poster("");
            poster.setMarketPlace(1);
            return new WrapObjToNetwork(poster,"marketPlace", true);
        }
        return null;

    }

    @Override
    public void doAfeter(JSONArray jsonArray) {
        pBLoad.setVisibility(View.GONE);
        if(jsonArray != null){

            PosterAdapter pAdapter = (PosterAdapter)pRecycle.getAdapter();
            Gson gson = new Gson();
            int aux, position;
            try{
                for(int i = 0, tamI = jsonArray.length(); i < tamI; i++){
                    Poster poster = gson.fromJson(jsonArray.getJSONObject(i).toString(), Poster.class);
                    pAdapter.addListItem(poster,i);
                }

            }catch (JSONException e){
                Toast.makeText(getActivity(), "Erro: "+ e.toString(), Toast.LENGTH_SHORT).show();
            }


        }else{
            Toast.makeText(getActivity(), "Falha ao carregar os dados.", Toast.LENGTH_SHORT).show();
        }
    }
}

