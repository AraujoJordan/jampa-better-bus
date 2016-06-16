package grafo;

import java.util.LinkedList;
/**
 *
 * @author Jorismar
 */
public interface Node {
    public String toString();
    public double getDistanceTo(Node nd);
    public double getCoastTo(Node nd);
    public LinkedList<Node> getNeighbors();
}
