package br.com.findfer.findfer.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by infsolution on 02/10/17.
 */

public class CustomJsonArrayRequest extends Request<JSONArray> {
    private Response.Listener<JSONArray> response;
    private Map<String, String> params;

    public CustomJsonArrayRequest(int method, String url, Map<String, String> params, Response.Listener<JSONArray> jsonArrayListener, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    public Map<String, String> getHeaders(String key, String value) throws AuthFailureError{
        HashMap<String, String> header = new HashMap<>();
        header.put(key, value);

        return(header);
    }
    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String js = new String(response.data, HttpHeaderParser.parseCharset(response.headers));//TODO Ver isso
            return(Response.success(new JSONArray(js), HttpHeaderParser.parseCacheHeaders(response)));
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void deliverResponse(JSONArray response) {
        this.response.onResponse(response);
    }
}
