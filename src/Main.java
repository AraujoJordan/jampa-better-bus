import grafo.BussPointNode;
import grafo.Node;
import heuristic.AStar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Date;
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
        System.err.println("Obtendo pontos de onbibus do arquivo JSON...");
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

                        if(latLngDouble[0]==-1.7976931348623157E308 || latLngDouble[1]==-1.7976931348623157E308)
                            continue;

                        allBussPoints.add(new BussPointNode(latLngDouble));
//                        System.out.println("https://www.openstreetmap.org/#map=18/" + latLng.get(1) + "/" + latLng.get(0));
                    }

                } catch (Exception limitOfArray) {
                    System.err.println("Obtendo vizinhaça de cada ponto...");
//                    Date d1 = new Date();
//                    Date d2;
//                    for (final BussPointNode bussPointNode : allBussPoints) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                bussPointNode.setNeighbors(getNeighbors(bussPointNode));
//                            }
//                        }).start();
//                        bussPointNode.setNeighbors(getNeighbors(bussPointNode));
//                    }
//                    d2 = new Date();
//                    System.err.println("D2-D1: "+(d2.getTime()-d1.getTime()));
//                    d1 = new Date();
                    for (BussPointNode bussPointNode : allBussPoints)
                        bussPointNode.setNeighbors(getNeighbors(bussPointNode));
//                    d2 = new Date();
//                    System.err.println("D2-D1: "+(d2.getTime()-d1.getTime()));
                    findPaths();
                }
                i++;
            }

        } catch (ArrayIndexOutOfBoundsException eof) {
            System.err.println("Leu todos os parâmetros.");
        } catch (FileNotFoundException error) {
            System.err.println("Arquivo não encontrado");
        } catch (IOException error) {
            System.err.println("Arquivo não é de texto");
        }
    }

    private static void findPaths() {

        AStar astar = new AStar();

        BussPointNode integracao = getPointByLatLng(-7.113865, -34.889959);
        BussPointNode ufpb5 = getPointByLatLng(-7.160302, -34.819225);
        BussPointNode lagoa = getPointByLatLng(-7.124206, -34.879972);

        BussPointNode vizinho1 = getPointByLatLng(-7.132889, -34.880213);
        BussPointNode vizinho2 = getPointByLatLng(-7.134557, -34.878613);

        System.err.println("Buscando rotas com A* (Vai demorar mais de 15 min)...");

        // INTEGRAÇAO - UFPB CAMPUS 5
        LinkedList<Node> resultado = astar.findRoute(lagoa, ufpb5);

        System.err.println("Rotas e vizinhos recolhidas");
        for (Node node : resultado) {
            BussPointNode bussPointNode = (BussPointNode) node;
            System.err.println("https://www.openstreetmap.org/#map=18/" + bussPointNode.getLatLng()[0] + "/" + bussPointNode.getLatLng()[1]);
        }
    }

    private static BussPointNode getPointByLatLng(double lat, double longe) {
        for (BussPointNode bussPointNode : allBussPoints) {
            if (bussPointNode.getLatLng()[0] == lat && bussPointNode.getLatLng()[1] == longe)
                return bussPointNode;
        }
        return null;
    }

    private static LinkedList<Node> getNeighbors(BussPointNode bussPoint) {
        LinkedList<Node> neighborsNodes = new LinkedList<>();
//        System.err.println("\nPonto: [" + bussPoint.getLatLng()[0] + "," + bussPoint.getLatLng()[1] + "]: ");

        for (int bussDistance = 30; neighborsNodes.size() < 10; bussDistance += 40) {
            for (BussPointNode bussPointFromList : allBussPoints) {
                if (distanceInMetters(bussPoint, bussPointFromList) <= bussDistance) {
                    if (neighborsNodes.contains(bussPointFromList))
                        continue;
                    neighborsNodes.add(bussPointFromList);
//                    System.err.print("[" + bussPointFromList.getLatLng()[0] + "," + bussPointFromList.getLatLng()[1] + "]");
//                    System.out.print("["+bussPointFromList.getLatLng()[0]+","+bussPointFromList.getLatLng()[1]+"]");
                }
            }
        }
//        System.out.println("Foram encontrados de:" + bussPoint.getLatLng()[0] + " " + bussPoint.getLatLng()[1] + ", "+neighborsNodes.size()+" pontos!");
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
        return R * c * 1000;
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }
}
