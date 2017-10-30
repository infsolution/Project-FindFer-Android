package br.com.findfer.findfer.network;

/**
 * Created by infsolution on 29/09/17.
 */

import br.com.findfer.findfer.model.WrapObjToNetwork;

import org.json.JSONArray;


public interface Transaction {
    public WrapObjToNetwork toBefore();
    public void doAfeter(JSONArray jsonArray);
}