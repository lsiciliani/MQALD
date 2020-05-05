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

package di.uniba.it.mqald.eval;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class implements the evaluation approach used in QALD-9
 * @author pierpaolo
 */
public class QaldEval {

    private static final Logger LOG = Logger.getLogger(QaldEval.class.getName());

    /**
     *
     * @param expectedAnswers
     * @param systemAnswers
     * @return
     */
    public static EvalMetrics compareAnswersJSON(String expectedAnswers, String systemAnswers) {
        try {
            JSONParser parser = new JSONParser();
            Object o1 = parser.parse(expectedAnswers);
            JSONObject expectedJson = (JSONObject) o1;
            Object o2 = parser.parse(systemAnswers);
            JSONObject systemJson = (JSONObject) o2;
            return compareAnswersJSON(expectedJson, systemJson);
        } catch (ParseException e) {
            LOG.log(Level.SEVERE, "result set could not be parsed", e);
            return new EvalMetrics(0, 0);
        }
    }

    /**
     *
     * @param expectedJson
     * @param systemJson
     * @return
     */
    public static EvalMetrics compareAnswersJSON(JSONObject expectedJson, JSONObject systemJson) {
        double precision = 0;
        double recall = 0;
        double f = 0;
        //Case when one is a SELECT and the other an ASK query
        if (expectedJson.containsKey("boolean") && !systemJson.containsKey("boolean")
                || !expectedJson.containsKey("boolean") && systemJson.containsKey("boolean")) {
            precision = 0;
            recall = 0;
        } else if (expectedJson.containsKey("boolean") && systemJson.containsKey("boolean")) { //Case when both are ASK queries
            if (expectedJson.get("boolean").equals(systemJson.get("boolean"))) {
                precision = 1;
                recall = 1;
            } else {
                precision = 0;
                recall = 0;
            }
        } else if (expectedJson.containsKey("results") && systemJson.containsKey("results")) { //Case when both are SELECT queries
            JSONArray variables1 = (JSONArray) ((JSONObject) expectedJson.get("head")).get("vars");
            JSONArray variables2 = (JSONArray) ((JSONObject) systemJson.get("head")).get("vars");
            if (variables1.size() != variables2.size()) {
                LOG.info("Variables size missmatch");
                precision = 0.0;
                recall = 0.0;
            } else if (variables1.size() == 1) { //both have only one variable
                Iterator<String> iterator = variables1.iterator();
                String var1 = iterator.next();
                Iterator<String> iterator2 = variables2.iterator();
                String var2 = iterator2.next();
                JSONArray expected = (JSONArray) ((JSONObject) expectedJson.get("results")).get("bindings");
                if (expected == null) {
                    expected = new JSONArray();
                }
                JSONArray system = (JSONArray) ((JSONObject) systemJson.get("results")).get("bindings");
                if (system == null) {
                    system = new JSONArray();
                }
                Iterator sys_it = system.iterator();
                boolean empty = true;
                while (sys_it.hasNext()) {
                    JSONObject obj= (JSONObject) sys_it.next();
                    if (!obj.isEmpty()) {
                        empty = false;
                    }
                }
                if (empty == true) {
                    system = new JSONArray();
                }                 
                
                if (expected.isEmpty() && system.isEmpty()) { // If the golden answerset is empty and the system does respond with an empty answer, we set precision, recall and F-measure to 1
                    precision = 1;
                    recall = 1;
                    f = 1;
                } else if (expected.isEmpty() && !system.isEmpty()) { // If the golden answerset is empty but the system responds with any answerset, we set precision, recall and F-measure to 0
                    precision = 0;
                    recall = 0;
                    f = 0;
                } else if (!expected.isEmpty() && system.isEmpty()) { // QALD F If the golden answerset is not empty but the QA system responds with an empty answerset, it is assumed that the system determined that it cannot answer the question. Here we set the precision to 1 and the recall and Fmeasure to 0
                    precision = 1;
                    recall = 0;
                    f = EvalMetrics.computeF1(precision, recall);
                } else {
                    LOG.info("Standard case");
                    Set<String> cg = collectValues(expected, var1);
                    int totalRelevant = cg.size();
                    Set<String> cs = collectValues(system, var2);
                    cg.retainAll(cs);
                    precision = (double) cg.size() / (double) cs.size();
                    recall = (double) cg.size() / (double) totalRelevant;
                    f = EvalMetrics.computeF1(precision, recall);
                }
            } else {
                LOG.warning("Multiple columns not knowledgebase yet");
            }
        } else { //The queries are not of the same type
            precision = 0;
            recall = 0;
            f = 0;
        }
        return new EvalMetrics(precision, recall, f);
    }

    private static Set<String> collectValues(JSONArray array, String var) {
        Set<String> set = new HashSet<>();
        for (Object o : array) {
            set.add(((JSONObject) ((JSONObject) o).get(var)).get("value").toString());
        }
        return set;
    }

}
