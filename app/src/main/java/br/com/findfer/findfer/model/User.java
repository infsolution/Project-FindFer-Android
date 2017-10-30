package br.com.findfer.findfer.model;

import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by infsolution on 22/09/17.
 */

public class User {
    private long idUser;
    private String user;
    private String password;
    private String nameUser;
    private float qualification;
    private List<User>users;
    private Media media;
    private Account account;
    private long marketPlace;
    private Coordinates coordinates;
    public User(String nameUser){
        this.nameUser = nameUser;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public float getQualification() {
        return qualification;
    }

    public void setQualification(float qualification) {
        this.qualification = qualification;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public long getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(long marketPlace) {
        this.marketPlace = marketPlace;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
