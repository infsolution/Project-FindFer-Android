package br.com.findfer.findfer.model;

import java.util.List;

/**
 * Created by infsolution on 22/09/17.
 */

public class Map {
    private long idMap;
    private List<Market> markets;
    private String marketPlace;
    public Map(String marketPlace){
        this.marketPlace = marketPlace;
    }

    public long getIdMap() {
        return idMap;
    }

    public void setIdMap(long idMap) {
        this.idMap = idMap;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }

    public String getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(String marketPlace) {
        this.marketPlace = marketPlace;
    }
}
