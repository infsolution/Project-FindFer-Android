package br.com.findfer.findfer.model;

/**
 * Created by infsolution on 22/09/17.
 */

public class MarketStall {
    private long idMarketStall;
    private User marketer;
    private Coordinates coodinates;
    private Media media;
    public MarketStall(User marketer){
        this.marketer = marketer;
    }

    public long getIdMarketStall() {
        return idMarketStall;
    }

    public void setIdMarketStall(long idMarketStall) {
        this.idMarketStall = idMarketStall;
    }

    public User getMarketer() {
        return marketer;
    }

    public void setMarketer(User marketer) {
        this.marketer = marketer;
    }

    public Coordinates getCoodinates() {
        return coodinates;
    }

    public void setCoodinates(Coordinates coodinates) {
        this.coodinates = coodinates;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
