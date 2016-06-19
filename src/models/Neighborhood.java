package models;

import java.util.LinkedList;

/**
 * Created by Jéssica on 19/06/2016.
 */
public class Neighborhood {
    private String name;
    private LinkedList<Road> roads;

    public Neighborhood(String name, LinkedList<Road> roads) {
        this.name = name;
        this.roads = roads;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Road> getRoads() {
        return roads;
    }

    public void setRoads(LinkedList<Road> roads) {
        this.roads = roads;
    }
}
