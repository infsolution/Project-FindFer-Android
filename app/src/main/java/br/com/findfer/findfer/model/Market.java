package br.com.findfer.findfer.model;

import java.util.List;

/**
 * Created by infsolution on 22/09/17.
 */

public class Market {
    private long idMarket;
    private List<MarketStall> marketStalls;
    private Perimeter perimeter;
    private String nameMarket;
    private String description;
    public Market(String nameMarket){
        this.nameMarket = nameMarket;
    }

    public long getIdMarket() {
        return idMarket;
    }

    public void setIdMarket(long idMarket) {
        this.idMarket = idMarket;
    }

    public List<MarketStall> getMarketStalls() {
        return marketStalls;
    }

    public void setMarketStalls(List<MarketStall> marketStalls) {
        this.marketStalls = marketStalls;
    }

    public Perimeter getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(Perimeter perimeter) {
        this.perimeter = perimeter;
    }

    public String getNameMarket() {
        return nameMarket;
    }

    public void setNameMarket(String nameMarket) {
        this.nameMarket = nameMarket;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
