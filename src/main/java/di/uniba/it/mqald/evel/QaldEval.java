/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mqald.evel;

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
 *
 * @author pierpaolo
 */
public class QaldEval {

    private static final Logger LOG = Logger.getLogger(QaldEval.class.getName());

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
