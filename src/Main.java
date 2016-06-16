import grafo.BussPointNode;
import grafo.Node;
import heuristic.AStar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by jordan on 15/06/16.
 */
public class Main {

    private static LinkedList<BussPointNode> allBussPoints;

    public static void main(String[] args) {
        allBussPoints = new LinkedList<>();

        if (args == null) {
            System.err.println("Parâmetro vazio! Coloque o endereço completo do arquivo JSON com os pontos de onibus");
            System.exit(1);
        }

        try {
            int i = 0;
            while (true) {
                String line, text = "";
                BufferedReader reader = new BufferedReader(new FileReader(args[i]));
                while ((line = reader.readLine()) != null)
                    text += line + "\n";
                try {

                    JSONObject jsonRoot = new JSONObject(text);
                    JSONArray jsonArray = jsonRoot.getJSONArray("features");
                    for (int index = 0; i < jsonArray.length() - 1; index++) {

                        JSONObject geometry = jsonArray.getJSONObject(index).getJSONObject("geometry");
                        JSONArray latLng = geometry.getJSONArray("coordinates");

                        double[] latLngDouble = new double[2];
                        latLngDouble[0] = (double) latLng.get(1);
                        latLngDouble[1] = (double) latLng.get(0);

                        allBussPoints.add(new BussPointNode(latLngDouble));
//                        System.out.println("https://www.openstreetmap.org/#map=18/" + latLng.get(1) + "/" + latLng.get(0));
                    }


                } catch (Exception limitOfArray) {
                    for(BussPointNode bussPointNode : allBussPoints) {
                        bussPointNode.setNeighbors(getNeighbors(bussPointNode));
                    }

                    findPaths();

                }
                i++;
            }
        } catch (ArrayIndexOutOfBoundsException eof) {
            System.out.println("Leu todos os parâmetros.");
        } catch (FileNotFoundException error) {
            System.err.println("Arquivo não encontrado");
        } catch (IOException error) {
            System.err.println("Arquivo não é de texto");
        }
    }

    private static void findPaths() {

        AStar astar = new AStar();

        // INTEGRAÇAO - UFPB CAMPUS 5
        astar.findRoute();
    }

    private static LinkedList<Node> getNeighbors(BussPointNode bussPoint) {
        LinkedList<Node> neighborsNodes = new LinkedList<>();

        for(int bussDistance = 50; neighborsNodes.size() < 5; bussDistance+=50) {
            for (BussPointNode bussPointFromList : allBussPoints) {
                if(distanceInMetters(bussPoint, bussPointFromList) <= bussDistance)
                    neighborsNodes.add(bussPointFromList);
            }
        }
        return neighborsNodes;
    }

    public static double distanceInMetters(BussPointNode p1, BussPointNode p2) {
        final int R = 6371;
        final double dLat = deg2rad(p1.getLatLng()[0] - p2.getLatLng()[0]);
        final double dLon = deg2rad(p1.getLatLng()[1] - p2.getLatLng()[1]);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(p2.getLatLng()[0])) * Math.cos(deg2rad(p1.getLatLng()[0])) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }
}
