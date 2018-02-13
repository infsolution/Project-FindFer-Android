package br.com.findfer.findfer.model;

/**
 * Created by infsolution on 05/01/18.
 */

public class ItIsMarket {
    private String name;
    private Double distance;
    private int itIsMarket;
    private long idMarket;
    public ItIsMarket(String name, int isMarket){
        this.name = name;
        this.itIsMarket = isMarket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getItIsMarket() {
        return itIsMarket;
    }

    public void setItIsMarket(int itIsMarket) {
        this.itIsMarket = itIsMarket;
    }

    public long getIdMarket() {
        return idMarket;
    }

    public void setIdMarket(long idMarket) {
        this.idMarket = idMarket;
    }

    @Override
    public String toString() {
        String isMarket="";
        if(this.itIsMarket == 0){
            isMarket = "Sua localização não está cadastrada como uma feira!\nVocê está a "+getDistance()+" km da feira "+getName()+"." +
                    "\nPara concluir esta ação é preciso estar na sua feira.";
        }
        return isMarket;
    }
}
