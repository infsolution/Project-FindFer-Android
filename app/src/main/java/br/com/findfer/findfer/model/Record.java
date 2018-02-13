package br.com.findfer.findfer.model;

/**
 * Created by infsolution on 28/10/17.
 */

public class Record {
    private long idRecord;
    private String name;
    private String fone;
    private String code;
    public Record(String fone){
        this.fone = fone;
    }

    public long getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(long idRecord) {
        this.idRecord = idRecord;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}