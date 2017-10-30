package br.com.findfer.findfer.model;

/**
 * Created by infsolution on 29/09/17.
 */

public class WrapObjToNetwork {
    private Poster poster;
    private Contact contact;
    private String method;
    private boolean isNewer;
    private String term;


    public WrapObjToNetwork(Poster poster, String method, boolean isNewer) {
        this.poster = poster;
        this.method = method;
        this.isNewer = isNewer;
    }
    public WrapObjToNetwork(Poster poster, String method, String term) {
        this.poster=poster;
        this.method = method;
        this.term = term;
    }
    public WrapObjToNetwork(Poster poster, String method, Contact contact) {
        this.poster = poster;
        this.method = method;
        this.contact = contact;
    }


    public Poster getPoster() {
        return this.poster;
    }

    public void setCar(Poster poster) {
        this.poster = poster;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isNewer() {
        return isNewer;
    }

    public void setIsNewer(boolean isNewer) {
        this.isNewer = isNewer;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
