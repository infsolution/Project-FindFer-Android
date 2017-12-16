package br.com.findfer.findfer.network;

import org.json.JSONArray;

import java.util.Map;

import br.com.findfer.findfer.model.Poster;

/**
 * Created by viniciusthiengo on 7/26/15.
 */
public interface Transaction {
    public Map<String, String> doBefore();

    public void doAfter(String response);
}
