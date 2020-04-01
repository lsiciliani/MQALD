/**
 * Copyright (c) 2020, the MQALD AUTHORS.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the University of Bari nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * GNU GENERAL PUBLIC LICENSE - Version 3, 29 June 2007
 *
 */
package di.uniba.it.mqald.qasystems;

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
public class QAnswer implements QASystem {

    private static final String ADDRESS = "http://qanswer-core1.univ-st-etienne.fr/api/gerbil?kb=dbpedia&query=";

    /**
     *
     * @param question
     * @return
     */
    @Override
    public JSONObject getAnswer(String question) {

        JSONObject answer = new JSONObject();
        try {
            URL url = new URL(ADDRESS + URLEncoder.encode(question, "utf-8"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            //con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setConnectTimeout(1000 * 60);
            con.setReadTimeout(1000 * 60);
            StringBuilder response = new StringBuilder();
            int code = con.getResponseCode();
            if (code != 500) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));

                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            } else {
                response.append("{\"questions\":[{\"question\":{\"answers\":\"{\\\"head\\\":{\\\"vars\\\":[\\\"x\\\"]},\\\"results\\\":{\\\"bindings\\\":[]}}\"}}]}");
            }
            con.disconnect();
            //System.out.println(response.toString());
            JSONParser parser = new JSONParser();
            JSONObject apians = (JSONObject) parser.parse(response.toString());
            JSONObject output = fixOutput(apians);
            JSONArray questions = (JSONArray) output.get("questions");
            JSONObject questionObj = (JSONObject) questions.get(0);
            JSONObject ansObj = (JSONObject) questionObj.get("question");
            JSONArray questionsArray = new JSONArray();
            questionsArray.add(ansObj);
            answer.put("questions", questionsArray);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GAnswer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException | ParseException ex) {
            Logger.getLogger(GAnswer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GAnswer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return answer;
    }

    private JSONObject fixOutput(JSONObject output) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONArray questions = (JSONArray) output.get("questions");
        JSONObject question = (JSONObject) questions.get(0);
        JSONObject qObj = (JSONObject) question.get("question");
        String answerString = (String) qObj.get("answers");
        if (answerString == null) {
            answerString = "{\"head\":{\"vars\":[\"uri\"]},\"results\":{\"bindings\":[]}}";
        }
        answerString = answerString.replaceAll("\\s", "");
        if (answerString.contains("\"bindings\":[{}]")) {
            answerString = answerString.replace("\"bindings\":[{}]", "\"bindings\":[]");
        }
        JSONObject ans = (JSONObject) parser.parse(answerString);
        qObj.remove("answers");
        JSONArray answers = new JSONArray();
        answers.add(ans);
        qObj.put("answers", answers);
        return output;
    }
}
