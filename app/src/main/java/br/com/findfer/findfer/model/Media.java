package br.com.findfer.findfer.model;

/**
 * Created by infsolution on 22/09/17.
 */

public class Media {
    public static final String PATHMEDIA ="";
    private long idMedia;
    private String nameMedia;
    public Media(String nameMedia){
        this.nameMedia = nameMedia;
    }

    public static String getPATHMEDIA() {
        return PATHMEDIA;
    }

    public long getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(long idMedia) {
        this.idMedia = idMedia;
    }

    public String getNameMedia() {
        return nameMedia;
    }

    public void setNameMedia(String nameMedia) {
        this.nameMedia = nameMedia;
    }
}
