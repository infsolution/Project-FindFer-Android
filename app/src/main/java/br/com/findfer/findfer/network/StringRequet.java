package br.com.findfer.findfer.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.findfer.findfer.MainActivity;
import br.com.findfer.findfer.model.Poster;

/**
 * Created by infsolution on 12/10/17.
 */

public class StringRequet {
    private RequestQueue requestQueue;
    private String url, newResponse;
    private Context context;
    private Map<String, String> parameters = new HashMap<>();
    public StringRequet(Context context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(this.context.getApplicationContext());
    }

    public String getStringRequest(){
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                newResponse = response;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               newResponse = error.toString();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return parameters;
            }
        };

        this.requestQueue.add(request);
        return newResponse;
    }
}
