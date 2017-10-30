package br.com.findfer.findfer.model;

/**
 * Created by infsolution on 22/09/17.
 */

public class Coordinates {
    private long idCoordinate;
    private long user;
    private Double longitude;
    private Double latitude;

    public Coordinates(Double latitude, Double longitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getIdCoordinate() {
        return idCoordinate;
    }

    public void setIdCoordinate(long idCoordinate) {
        this.idCoordinate = idCoordinate;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
