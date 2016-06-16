package heuristic;
import grafo.Node;

public class AStarNode {
    private int father_id;
    private Node node;
    private double F;
    private double coast;
    private double distance;

    public AStarNode(Node node, int father, double coast, double distance) {
        this.father_id = father;
        this.node = node;
        this.F = coast + distance;
        this.coast = coast;
        this.distance = distance;
    }

    public int getFatherID() {
        return father_id;
    }

    public void setFatherID(int id) {
        this.father_id = id;
    }

    public double getF() {
        return F;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public double getCoast() {
        return coast;
    }

    public void setCoast(double coast) {
        this.coast = coast;
        this.F = this.coast + this.distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
        this.F = this.coast + this.distance;
    }
}
