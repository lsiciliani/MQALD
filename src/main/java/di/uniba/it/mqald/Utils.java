package di.uniba.it.mqald;


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

   
}
