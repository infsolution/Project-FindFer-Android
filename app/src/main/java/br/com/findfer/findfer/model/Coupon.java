package br.com.findfer.findfer.model;

import java.util.Date;

/**
 * Created by infsolution on 22/09/17.
 */

public class Coupon {
    private long idCoupon;
    private String code;
    private Double value;
    private Date validity;
    public Coupon(Double value, Date validity){
        this.value = value;
        this.validity = validity;
    }

    public long getIdCoupon() {
        return idCoupon;
    }

    public void setIdCoupon(long idCoupon) {
        this.idCoupon = idCoupon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }
}
