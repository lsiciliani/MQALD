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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author lucia
 */
public class TeBaQA {

    private static String address = "http://139.18.2.39:8187/qa?query=";

    public static JSONObject getAPIAnswer(String question) {
     
        JSONObject answer = new JSONObject();
        try {
            URL url = new URL(address + URLEncoder.encode(question, "utf-8"));

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
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
            JSONObject apians = (JSONObject) parser.parse(response.toString());

            JSONObject output = fixJSONOutput(apians);
            JSONArray questions = (JSONArray) output.get("questions");
            JSONObject questionObj = (JSONObject) questions.get(0);
            JSONObject ansObj = (JSONObject) questionObj.get("question");

            JSONArray questionsArray = new JSONArray();
            questionsArray.add(ansObj);
            answer.put("questions", questionsArray);

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

    private static JSONObject fixJSONOutput(JSONObject output) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONArray questions = (JSONArray) output.get("questions");
        JSONObject question = (JSONObject) questions.get(0);
        JSONObject qObj = (JSONObject) question.get("question");
        String answerString = (String) qObj.get("answers");
        if (answerString == null) {
            answerString = "{\"head\":{\"vars\":[\"uri\"]},\"results\":{\"bindings\":[]}}";
        }
        JSONObject ans = (JSONObject) parser.parse(answerString);

        qObj.remove("answers");

        JSONArray answers = new JSONArray();
        answers.add(ans);
        qObj.put("answers", answers);

        return output;
    }
}
