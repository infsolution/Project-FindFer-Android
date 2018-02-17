package br.com.findfer.findfer.model;

/**
 * Created by infsolution on 10/11/17.
 */

public class User {
    private long idUser;
    private String name;
    private String fone;
    private String password;
    private String dateRegister;
    private String email;
    private int typeAccount;
    private String image;
    private long idMarket;
    private long idCoordinate;
    private long codUser;
    private Double ActualLatitude;
    private Double ActualLongitude;
    private String description;
    public User(String name){
        this.name = name;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(String dateRegister) {
        this.dateRegister = dateRegister;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(int typeAccount) {
        this.typeAccount = typeAccount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getIdMarket() {
        return idMarket;
    }

    public void setIdMarket(long idMarket) {
        this.idMarket = idMarket;
    }

    public long getIdCoordinate() {
        return idCoordinate;
    }

    public void setIdCoordinate(long idCoordinate) {
        this.idCoordinate = idCoordinate;
    }

    public long getCodUser() {
        return codUser;
    }

    public void setCodUser(long codUser) {
        this.codUser = codUser;
    }

    public Double getActualLatitude() {
        return ActualLatitude;
    }

    public void setActualLatitude(Double actualLatitude) {
        ActualLatitude = actualLatitude;
    }

    public Double getActualLongitude() {
        return ActualLongitude;
    }

    public void setActualLongitude(Double actualLongitude) {
        ActualLongitude = actualLongitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getName();
    }
}
