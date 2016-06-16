import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by jordan on 15/06/16.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println();

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
                    for (int index = 0; i < jsonArray.length()-1; index++) {

                        JSONObject geometry = jsonArray.getJSONObject(index).getJSONObject("geometry");
                        JSONArray latLng = geometry.getJSONArray("coordinates");

                        System.out.println("https://www.openstreetmap.org/#map=18/"+latLng.get(1)+"/"+latLng.get(0));
                    }

                } catch (Exception err) {
                    System.out.println("Erro ao abrir pontos de parada, \"" + args[i] + "\" abortado por causa de erro(s)\n");
                    err.printStackTrace();
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
}
