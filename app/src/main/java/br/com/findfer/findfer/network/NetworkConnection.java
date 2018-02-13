package br.com.findfer.findfer.network;

import android.content.Context;
import android.util.Log;
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
import br.com.findfer.findfer.model.Poster;

/**
 * Created by viniciusthiengo on 7/26/15.
 */
public class NetworkConnection {
    private static NetworkConnection instance;
    private Context mContext;
    private RequestQueue mRequestQueue;


    private NetworkConnection(Context c) {
        mContext = c;
        mRequestQueue = getRequestQueue();
    }


    public static NetworkConnection getInstance(Context c) {
        if (instance == null) {
            instance = new NetworkConnection(c.getApplicationContext());
        }
        return (instance);
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return (mRequestQueue);
    }


    public <T> void addRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }


    public void execute(final Transaction transaction, String url ) {
        if (transaction.doBefore() == null) {
            return;
        }
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                transaction.doAfter(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                transaction.doAfter(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                  return transaction.doBefore();
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000,3,2));
        addRequestQueue(request);
    }

    }

