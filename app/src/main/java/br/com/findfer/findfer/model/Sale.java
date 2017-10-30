package br.com.findfer.findfer.model;

import java.util.List;

/**
 * Created by infsolution on 22/09/17.
 */

public class Sale {
    private long idSale;
    private List<Poster> posters;
    private String dateSale;
    private Double value;
    public Sale(){

    }

    public long getIdSale() {
        return idSale;
    }

    public void setIdSale(long idSale) {
        this.idSale = idSale;
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }

    public String getDateSale() {
        return dateSale;
    }

    public void setDateSale(String dateSale) {
        this.dateSale = dateSale;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
