import grafo.BussPointNode;
import grafo.Node;
import heuristic.AStar;
import models.BussPoint;
import models.Neighborhood;
import models.Road;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by jordan on 15/06/16.
 */
public class Main {

    private static LinkedList<Neighborhood> allNeighborhoods;
    private static LinkedList<BussPointNode> allBussPoints;

    public static void main(String[] args) {
        allBussPoints = new LinkedList<>();

        if (args == null) {
            System.err.println("Parâmetro vazio! Coloque o endereço completo do arquivo JSON com os pontos de onibus");
            System.exit(1);
        }

        try {
            System.err.println("Obtendo pontos de onbibus do arquivo JSON...");
            String line, text = "";
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            while ((line = reader.readLine()) != null)
                text += line + "\n";
            try {
                JSONObject jsonRoot = new JSONObject(text);
                JSONArray districts = jsonRoot.getJSONArray("districts");

                allNeighborhoods = new LinkedList<>();
                for (int districtsIndex = 0; districtsIndex < districts.length(); districtsIndex++) {
                    JSONObject district = districts.getJSONObject(districtsIndex);
                    String districtName = district.getString("name");
                    JSONArray districtRoutes = district.getJSONArray("route");

                    LinkedList<Road> roads = new LinkedList<>();
                    for (int routesIndex = 0; routesIndex < districtRoutes.length(); routesIndex++) {
                        JSONObject road = districtRoutes.getJSONObject(routesIndex);
                        String name = road.getString("road");
                        JSONArray points = road.getJSONArray("points");

                        LinkedList<BussPoint> bussPoints = new LinkedList<>();
                        for (int pointIndex = 0; pointIndex < points.length(); pointIndex++) {
                            JSONObject point = points.getJSONObject(pointIndex);
                            boolean cross = point.getString("cross").equals("yes");
                            JSONArray coordinates = point.getJSONArray("coordinates");
                            double[] latLng = new double[2];
                            latLng[0] = (double) coordinates.get(0);
                            latLng[1] = (double) coordinates.get(1);

                            bussPoints.add(new BussPoint(latLng, cross));
                        }
                        roads.add(new Road(name, bussPoints));
                    }
                    allNeighborhoods.add(new Neighborhood(districtName, roads));
                }

            } catch (Exception errorToReadJson) {
                errorToReadJson.printStackTrace();
            }

            System.err.println("Arquivo lido com " + allNeighborhoods.size() + " distritos");
            int routes = 0;
            int bussPoints = 0;
            for (int i = 0; i < allNeighborhoods.size(); i++) {
                routes += allNeighborhoods.get(i).getRoads().size();
                for (int j = 0; j < allNeighborhoods.get(i).getRoads().size(); j++)
                    bussPoints += allNeighborhoods.get(i).getRoads().get(j).getBussPoints().size();
            }
            System.err.println("Rotas encontradas: " + routes);
            System.err.println("Pontos de onibus encontrados: " + bussPoints);

            // FAÇA A BUSCA AQUI

        } catch (ArrayIndexOutOfBoundsException eof) {
            System.err.println("Leu todos os parâmetros.");
        } catch (FileNotFoundException error) {
            System.err.println("Arquivo não encontrado");
        } catch (IOException error) {
            System.err.println("Arquivo não é de texto");
        }
    }
}
