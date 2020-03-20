package di.uniba.mqald;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lucia
 */
public class Utils {

    public static void ganswer(String question) throws Exception {

        URL url = new URL("http://ganswer.gstore-pku.com/api/qald.jsp?query=" + URLEncoder.encode(question, "utf-8"));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.setConnectTimeout(1000 * 60);
        con.setReadTimeout(1000 * 60);

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        con.disconnect();
        //System.out.println(response.toString());
        JSONParser parser = new JSONParser();
        JSONObject r = (JSONObject) parser.parse(response.toString());
        JSONArray a = (JSONArray) r.get("questions");
        if (a != null) {
            for (int i = 0; i < a.size(); i++) {

            }
        }

    }

    /*
    public static List<String> getQuestionsIDWithModifiers(List<Question> questions) {
        List<String> modqID = new ArrayList<>();
        for (Question q : questions) {
            String query = q.getQuery();
            List<String> modifiers = new ArrayList<>();

            if (query.contains("ASK")) {
                modifiers.add("ASK");
            }
            if (query.contains("FILTER") || query.contains("filter")) {
                modifiers.add("FILTER");
            }
            if (query.contains("LIMIT")) {
                modifiers.add("LIMIT");
            }
            if (query.contains("COUNT")) {
                modifiers.add("COUNT");
            }
            if (query.contains("GROUP BY")) {
                modifiers.add("GROUP BY");
            }
            if (query.contains("ORDER BY")) {
                modifiers.add("ORDER BY");
            }
            if (query.contains("UNION")) {
                modifiers.add("UNION");
            }
            if (query.contains("HAVING")) {
                modifiers.add("HAVING");
            }
            
            if (query.contains("NOW(") || query.contains("now(")) {
                modifiers.add("NOW");
            }
            if (query.contains("YEAR(") || query.contains("year(") ) {
                modifiers.add("YEAR");
            }

            //se ho trovato almeno un modifier, aggiungo la question alla lista 
            if (!modifiers.isEmpty()) {
                q.setModifiers(modifiers);
                modqID.add(q.getId());
            }

        }
        return modqID;
    }

     */
    public static List<String> checkFilters(String query) {
        List<String> modifiers = new ArrayList<>();

        //Query forms
        if (query.contains("ASK")) {
            modifiers.add("ASK");
        }
        if (query.contains("CONSTRUCT")) { //no
            modifiers.add("CONSTRUCT");
        }
        if (query.contains("DESCRIBE")) { //no
            modifiers.add("DESCRIBE");
        }

        //Patterns
        if (query.contains("BIND")) {
            modifiers.add("BIND");
        }
        if (query.contains("FILTER") || query.contains("filter")) {
            modifiers.add("FILTER");
        }
        if (query.contains("HAVING")) {
            modifiers.add("HAVING");
        }
        if (query.contains("GROUP BY")) {
            modifiers.add("GROUP BY");
        }
        if (query.contains("UNION")) {
            modifiers.add("UNION");
        }

        //Modifiers
        if (query.contains("LIMIT")) {
            modifiers.add("LIMIT");
        }
        if (query.contains("OFFSET")) {
            modifiers.add("OFFSET");
        }
        if (query.contains("ORDER BY")) {
            modifiers.add("ORDER BY");
        }

        //Aggregate functions
        if (query.contains("COUNT")) {
            modifiers.add("COUNT");
        }
        if (query.contains("SUM")) { //no
            modifiers.add("SUM");
        }
        if (query.contains("MIN")) { //no
            modifiers.add("MIN");
        }
        if (query.contains("MAX")) { //no
            modifiers.add("MAX");
        }
        if (query.contains("AVG")) { //no
            modifiers.add("AVG");
        }

        if (query.contains("NOW(") || query.contains("now(")) {
            modifiers.add("NOW");
        }
        if (query.contains("YEAR(") || query.contains("year(")) {
            modifiers.add("YEAR");
        }

        return modifiers;
    }

    /*
    public static List<Question> getQuestionsWithModifiers(List<Question> questions) {
        List<Question> modQuestions = new ArrayList<>();
        for (Question q : questions) {
            String query = q.getQuery();
            List<String> modifiers = new ArrayList<>();

            if (query.contains("ASK")) {
                modifiers.add("ASK");
            }
            if (query.contains("FILTER")) {
                modifiers.add("FILTER");
            }
            if (query.contains("LIMIT")) {
                modifiers.add("LIMIT");
            }
            if (query.contains("COUNT")) {
                modifiers.add("COUNT");
            }
            if (query.contains("GROUP BY")) {
                modifiers.add("GROUP BY");
            }
            if (query.contains("ORDER BY")) {
                modifiers.add("ORDER BY");
            }
            if (query.contains("UNION")) {
                modifiers.add("UNION");
            }

            //se ho trovato almeno un modifier, aggiungo la question alla lista 
            if (!modifiers.isEmpty()) {
                q.setModifiers(modifiers);
                modQuestions.add(q);
            }

        }
        return modQuestions;
    } */
}
