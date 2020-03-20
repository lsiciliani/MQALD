/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mqald.QASystems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author lucia
 */
public class GAnswer{
    
    private static String address = "http://ganswer.gstore-pku.com/api/qald.jsp?query=";
    
    public static JSONObject getAPIAnswer(String question) {
        JSONObject answer = new JSONObject();
        try {
            URL url = new URL(address + URLEncoder.encode(question, "utf-8"));

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
            answer = (JSONObject) parser.parse(response.toString());
            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GAnswer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GAnswer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GAnswer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(GAnswer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return answer;
    }
}
