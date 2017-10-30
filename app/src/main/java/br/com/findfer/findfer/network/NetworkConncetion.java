package br.com.findfer.findfer.network;

/**
 * Created by infsolution on 29/09/17.
 */

import android.app.VoiceInteractor;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.findfer.findfer.model.WrapObjToNetwork;

public class NetworkConncetion {
    private static NetworkConncetion instance;
    private Context context;
    private RequestQueue requestQueue;

    private NetworkConncetion(Context context){
        this.context = context;
        this.requestQueue = getRequestQueue();

    }
    public static NetworkConncetion getInstance(Context context){
        if(instance == null){
            instance = new NetworkConncetion(context.getApplicationContext());
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if(this.requestQueue == null){
            this.requestQueue = Volley.newRequestQueue(this.context);
        }
        return this.requestQueue;
    }
    public <T> void addRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
    public void execute(final Transaction transaction, String tag){
        WrapObjToNetwork obj = transaction.toBefore();
        Gson gson = new Gson();
        if(obj == null){
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("market_place", gson.toJson(obj));
        CustomRequest request = new CustomRequest(Request.Method.POST, "http://www.findfer.com.br/FindFer/control/LoadPosters.php", params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                transaction.doAfeter(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                transaction.doAfeter(null);
            }
        });
        request.setTag(tag);
        addRequestQueue(request);
        }
    }
