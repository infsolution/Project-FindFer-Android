package br.com.findfer.findfer.model;

/**
 * Created by infsolution on 22/09/17.
 */

public class Account {
    private long idAccount;
    private int typeAccount;
    private Double value;
    private int numberClients;
    private int numberPosters;
    public Account(int typeAccount){
        this.typeAccount = typeAccount;
    }
    public long getIdAccount() {
        return idAccount;
    }
    public void setIdAccount(long idAccount) {
        this.idAccount = idAccount;
    }
    public int getTypeAccount() {
        return typeAccount;
    }
    public void setTypeAccount(int typeAccount) {
        this.typeAccount = typeAccount;
    }
    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public int getNumberClients() {
        return numberClients;
    }
    public void setNumberClients(int numberClients) {
        this.numberClients = numberClients;
    }
    public int getNumberPosters() {
        return numberPosters;
    }
    public void setNumberPosters(int numberPosters) {
        this.numberPosters = numberPosters;
    }
}
