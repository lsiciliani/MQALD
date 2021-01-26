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
import java.util.Iterator;
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
public class GAnswer implements QASystem {

    private static final String ADDRESS = "http://ganswer.gstore-pku.com/gSolve/?data=";

    /**
     *
     * @param question
     * @return
     */
    @Override
    public JSONObject getAnswer(String question) {
        int attempts = 0;
        JSONObject finalans = new JSONObject();
        JSONObject answer = new JSONObject();
        final String data = "{\"maxAnswerNum\":\"100\", \"maxSparqlNum\":\"1\", \"question\":\"###\"}";
        try {
            URL url = new URL(ADDRESS + URLEncoder.encode(data.replace("###", question), "utf-8"));
            System.out.println("Connecting to " + url.toString() + " Attempt n. " + attempts);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //con.setRequestProperty("Content-Type", "application/json; utf-8");
            //con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setConnectTimeout(1000 * 60);
            con.setReadTimeout(5000 * 60);
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

            String status = (String) answer.get("status");
            if (status.equals("200")) {
                answer.remove("question");
                JSONObject res = (JSONObject) answer.get("results");
                if (res != null && !res.isEmpty()) {
                    JSONArray bindings = (JSONArray) res.get("bindings");
                    Iterator it_bin = bindings.iterator();
                    while (it_bin.hasNext()) {
                        JSONObject binding = (JSONObject) it_bin.next();
                        Iterator it_keyset = binding.keySet().iterator();
                        while (it_keyset.hasNext()) {
                            String key = (String) it_keyset.next();
                            JSONObject single_bind = (JSONObject) binding.get(key);
                            if (single_bind.get("type").equals("uri")) {
                                String value = (String) single_bind.get("value");
                                value = value.replaceAll("<", "");
                                value = value.replaceAll(">", "");
                                value = "http://dbpedia.org/resource/" + value;
                                single_bind.remove("value");
                                single_bind.put("value", value);
                            }
                            if (((String) single_bind.get("value")).startsWith("")) {
                                String value = (String) single_bind.get("value");
                                value = value.replaceAll("\"", "");
                                single_bind.remove("value");
                                single_bind.put("value", value);
                            }
                        }
                    }
                }

                JSONObject head_obj = new JSONObject();
                head_obj.put("vars", answer.get("vars"));
                answer.remove("vars");

                JSONObject ans_obj = new JSONObject();
                ans_obj.put("head", head_obj);
                ans_obj.put("results", answer.get("results"));
                answer.remove("results");

                JSONArray ans_array = new JSONArray();
                ans_array.add(ans_obj);
                answer.put("answers", ans_array);

            } else {
                answer.remove("question");
                JSONArray bindings_array = new JSONArray();
                JSONObject results_obj = new JSONObject();
                results_obj.put("bindings", bindings_array);

                JSONArray vars_array = new JSONArray();
                vars_array.add("s1");
                JSONObject head_obj = new JSONObject();
                head_obj.put("vars", vars_array);

                JSONObject ans_obj = new JSONObject();
                ans_obj.put("head", head_obj);
                ans_obj.put("results", results_obj);

                JSONArray ans_array = new JSONArray();
                ans_array.add(ans_obj);

                answer.put("answers", ans_array);

            }
            //break;

            JSONArray questions = new JSONArray();
            questions.add(answer);
            finalans.put("questions", questions);

        } catch (UnsupportedEncodingException | MalformedURLException | ParseException ex) {
            Logger.getLogger(GAnswer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GAnswer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return finalans;
    }
}
