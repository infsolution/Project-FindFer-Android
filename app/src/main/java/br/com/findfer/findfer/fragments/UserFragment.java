package br.com.findfer.findfer.fragments;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.findfer.findfer.MainActivity;
import br.com.findfer.findfer.PosterActivity;
import br.com.findfer.findfer.ProfileActivity;
import br.com.findfer.findfer.R;
import br.com.findfer.findfer.adapter.PosterAdapter;
import br.com.findfer.findfer.adapter.UserAdapter;
import br.com.findfer.findfer.dao.UserDao;
import br.com.findfer.findfer.extras.UtilTCM;
import br.com.findfer.findfer.interfaces.RecyclerViewOnClickListenerHack;
import br.com.findfer.findfer.model.Account;
import br.com.findfer.findfer.model.Coordinates;
import br.com.findfer.findfer.model.Poster;
import br.com.findfer.findfer.model.User;
import br.com.findfer.findfer.network.NetworkConnection;
import br.com.findfer.findfer.network.Transaction;

/**
 * Created by infsolution on 03/09/17.
 */

public class UserFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction{
    private RecyclerView pRecycle;
    private ProgressBar pbLoad;
    private View view;
    private String url;
    private List<User> userList;
    private UserAdapter adapter;
    private User user;
    private UserDao uDao;
    private LinearLayoutManager llM;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = "http://www.findfer.com.br/FindFer/control/LoadRelationship.php";
        userList = new ArrayList<>();
        user = getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        pbLoad = (ProgressBar)view.findViewById(R.id.pb_load_user);
        pRecycle = (RecyclerView)view.findViewById(R.id.rv_list_user);
        pRecycle.setHasFixedSize(true);
        pRecycle.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*llM = (LinearLayoutManager)pRecycle.getLayoutManager();
                adapter = (UserAdapter)pRecycle.getAdapter();
                if(userList.size() == llM.findLastCompletelyVisibleItemPosition()+1){
                    callVolleyRequest();
                }*/
            }

        });
        LinearLayoutManager llM = new LinearLayoutManager(getActivity());
        llM.setOrientation(LinearLayoutManager.VERTICAL);
        pRecycle.setLayoutManager(llM);
        callVolleyRequest();
        adapter = new UserAdapter(getActivity(),userList);
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
    public void callVolleyRequest(){
        NetworkConnection.getInstance(getActivity()).execute(this, url);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putParcelableArrayList("user",(ArrayList<User>) userList);
    }

    @Override
    public void onClickListner(View view, int position) {
        //Log.i("LOG","OnClick User");
        Intent user = new Intent(getActivity(), ProfileActivity.class);
        user.putExtra("name_user",userList.get(position).getName());
        user.putExtra("name_market","Market");
        user.putExtra("media_profile",userList.get(position).getImage());
        user.putExtra("date_time",userList.get(position).getDateRegister());
        user.putExtra("status_request_relationship",false);
        user.putExtra("type_account", userList.get(position).getTypeAccount());
        startActivity(user);
        }

    @Override
    public Map<String, String> doBefore() {
        //Log.i("MYLOG","abriu o doBefore User");
        pbLoad.setVisibility(View.VISIBLE);
        if(UtilTCM.verifyConnection(getActivity())){
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id_user", Long.toString(user.getCodUser()));
            parameters.put("type_account",Long.toString(user.getTypeAccount()));
            return parameters;
        }else{
            pbLoad.setVisibility(View.GONE);
            //Toast.makeText(getActivity(), "Você não está conectado à internet!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void doAfter(String response) {
        //Log.i("MYLOG","abriu o doAfter");
        pbLoad.setVisibility(View.GONE);
            userList = createUsers(response);
            UserAdapter adapter = new UserAdapter(getActivity(),userList);
            adapter.setRecyclerViewOnClickListenerHack(this);
            pRecycle.setAdapter(adapter);
        }

    private List<User> createUsers(String response){
        final List<User> users = new ArrayList<>();
        try {
            User user;
            JSONArray jUsers = new JSONArray(response);
            for (int i = 0; i < jUsers.length(); i++) {
                JSONObject jUser = jUsers.getJSONObject(i);
                user = new User(jUser.getString("name"));
                user.setIdUser(jUser.getLong("id_user"));
                user.setImage("http://www.findfer.com.br/FindFer" + jUser.getString("media"));
                user.setTypeAccount(jUser.getInt("id_conta"));
                user.setDescription(getDescription(user.getTypeAccount()));
                users.add(user);
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Desculpe! Houve um erro ao carregar sua lista de amigos", Toast.LENGTH_LONG).show();
        }
        return users;
    }

    private String getDescription(long typeAccount){
        if(typeAccount == 1){
            return "CLIENTE";
        }
        return "FEIRANTE";
    }
}

