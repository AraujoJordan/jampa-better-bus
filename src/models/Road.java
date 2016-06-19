package models;

import java.util.LinkedList;

/**
 * Created by Jéssica on 19/06/2016.
 */
public class Road {

    private String name;
    private LinkedList<BussPoint> bussPoints;

    public Road(String name, LinkedList<BussPoint> bussPoints) {
        this.name = name;
        this.bussPoints = bussPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<BussPoint> getBussPoints() {
        return bussPoints;
    }

    public void setBussPoints(LinkedList<BussPoint> bussPoints) {
        this.bussPoints = bussPoints;
    }

}
