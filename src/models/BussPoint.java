package models;

/**
 * Created by Jéssica on 19/06/2016.
 */
public class BussPoint {
    private double[] latLong;
    private boolean cross;

    public BussPoint(double[] latLong, boolean cross) {
        this.latLong = latLong;
        this.cross = cross;
    }

    public double[] getLatLong() {
        return latLong;
    }

    public void setLatLong(double[] latLong) {
        this.latLong = latLong;
    }

    public boolean isCross() {
        return cross;
    }

    public void setCross(boolean cross) {
        this.cross = cross;
    }
}
