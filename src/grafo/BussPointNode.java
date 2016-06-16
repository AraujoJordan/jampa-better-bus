package grafo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by jordan on 16/06/16.
 */
public class BussPointNode implements Node {

    private double[] latLng;
    private LinkedList<Node> neighbors;

    private final String OSRMServer = "http://router.project-osrm.org/viaroute?loc=";
    private final String secondParam = "&loc=";

    private HashMap<Node, Integer> costCache = new HashMap<>();

    public BussPointNode(double[] latLng) {
        this.latLng = latLng;
    }

    public void setNeighbors(LinkedList<Node> neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public double getDistanceTo(Node nd) {

        BussPointNode bussPointNode = (BussPointNode) nd;
        double nodeLatLng[] = bussPointNode.getLatLng();
        JSONObject jsonObject = null;

        final int R = 6371;
        final double dLat = deg2rad(nodeLatLng[0] - latLng[0]);
        final double dLon = deg2rad(nodeLatLng[1] - latLng[1]);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(latLng[0])) * Math.cos(deg2rad(nodeLatLng[0])) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;

//        try {
//            jsonObject = readJsonFromUrl(OSRMServer+
//                    latLng[0]+","+latLng[1]+
//                    secondParam+
//                    nodeLatLng[0]+","+nodeLatLng[1]
//            );
//            JSONObject routeSummary = jsonObject.getJSONObject("route_summary");
//            return routeSummary.getInt("total_distance");
//        } catch (IOException e) {
//            System.err.println("Cant get distance from OSRM, get direct distance. Error: "+e.getMessage());
//
//            //        DISTANCIA USANDO LINHA RETA ENTRE OS DOIS PONTOS
//            final int R = 6371;
//            final double dLat = deg2rad(nodeLatLng[0] - latLng[0]);
//            final double dLon = deg2rad(nodeLatLng[1] - latLng[1]);
//            final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                    Math.cos(deg2rad(latLng[0])) * Math.cos(deg2rad(nodeLatLng[0])) *
//                            Math.sin(dLon / 2) * Math.sin(dLon / 2);
//            final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//            return R * c;
//
//        }
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    @Override
    public String toString() {
        return "[" + latLng[0] + " : " + latLng[1] + "]";
    }

    @Override
    public double getCoastTo(Node nd) {
        if (costCache.containsKey(nd)) {
            System.err.println("Pegando o tempo pelo OSRM server (CACHE)");
            return costCache.get(nd);
        }

        BussPointNode bussPointNode = (BussPointNode) nd;
        double nodeLatLng[] = bussPointNode.getLatLng();
        JSONObject jsonObject = null;

//      DISTANCIA USANDO LINHA RETA ENTRE OS DOIS PONTOS
//        final int R = 6371;
//        final double dLat = deg2rad(nodeLatLng[0] - latLng[0]);
//        final double dLon = deg2rad(nodeLatLng[1] - latLng[1]);
//        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                Math.cos(deg2rad(latLng[0])) * Math.cos(deg2rad(nodeLatLng[0])) *
//                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
//        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        return R * c * 125; //1km vai demorar 7min

        try {
            jsonObject = readJsonFromUrl(OSRMServer +
                    latLng[0] + "," + latLng[1] +
                    secondParam +
                    nodeLatLng[0] + "," + nodeLatLng[1]
            );
            JSONObject routeSummary = jsonObject.getJSONObject("route_summary");
            costCache.put(nd,routeSummary.getInt("total_time"));
            System.err.println("Pegando o tempo pelo OSRM server (ONLINE)");
            return routeSummary.getInt("total_time"); //TEMPO DO PERCUSSO
        } catch (IOException e) {
            System.err.println("Pegando o tempo pela linha reta dos pontos");

            //  DISTANCIA USANDO LINHA RETA ENTRE OS DOIS PONTOS
            final int R = 6371;
            final double dLat = deg2rad(nodeLatLng[0] - latLng[0]);
            final double dLon = deg2rad(nodeLatLng[1] - latLng[1]);
            final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(deg2rad(latLng[0])) * Math.cos(deg2rad(nodeLatLng[0])) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);
            final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return R * c * 125; //1km vai demorar 7min
        }
    }

    @Override
    public LinkedList<Node> getNeighbors() {
        return this.neighbors;
    }

    public double[] getLatLng() {
        return latLng;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
