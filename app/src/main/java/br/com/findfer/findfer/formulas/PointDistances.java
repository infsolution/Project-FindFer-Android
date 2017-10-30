package br.com.findfer.findfer.formulas;

import br.com.findfer.findfer.model.Coordinates;

/**
 * Created by infsolution on 20/10/17.
 */

public class PointDistances {

    public PointDistances(){

    }
    public Double calcHipotenusa(Coordinates a, Coordinates b){
      return Math.sqrt(Math.pow((b.getLatitude() - a.getLatitude()),2) + Math.pow(b.getLongitude() - a.getLongitude(),2));
    }
}
