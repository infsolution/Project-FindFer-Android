package br.com.findfer.findfer.network;

import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import br.com.findfer.findfer.model.Poster;

/**
 * Created by infsolution on 02/10/17.
 */

public class SimpleNetWork {
    private RequestQueue rq;
    private Map<String, String> params;
    private String url = "http://192.168.43.98/findfer/control/LoadPosters.php";


    public RequestQueue callByJsonArrayRequest(View view){
        params = new HashMap<>();
        params.put("market_place", "0");

        CustomJsonArrayRequest request = new CustomJsonArrayRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                       // Log.i("Script", "SUCCESS: "+response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        request.setTag("tag");
        rq.add(request);
        return rq;
    }
}
