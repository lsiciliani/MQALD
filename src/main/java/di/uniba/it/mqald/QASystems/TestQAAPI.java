/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mqald.QASystems;

import di.uniba.mqald.QaldIO;
import di.uniba.mqald.Question;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author lucia
 */
public class TestQAAPI {

    private static final Logger LOG = Logger.getLogger(TestQAAPI.class.getName());

    public static void main(String[] args) {
        LOG.setLevel(Level.INFO);
        long time = 0;
        int numq = 0;
        int numqa = 0;
        try {
            List<Question> questions = QaldIO.readJSON(new File(args[0]));
            FileWriter writer = new FileWriter(args[1]);
            JSONObject fileObj = new JSONObject();
            JSONArray questionsArray = new JSONArray();

            for (Question question : questions) {
                numq++;
                long starTime = System.currentTimeMillis();
                LOG.log(Level.INFO, "{0}: {1}", new Object[]{question.getId(), question.getText()});
                try {
                    //JSONObject answersToQuestion = YaqaQASystem.getInstance().getAnswersToQuestion(q, "en");
                    //JSONObject answersToQuestion = GanswerQASystem.getInstance().getAnswersToQuestion(q, "en");
                    //JSONObject answersToQuestion = QAnswerQASystem.getInstance().getAnswersToQuestion(q, "en");
                    
                    
                    //JSONObject answersToQuestion = GAnswer.getAPIAnswer(question.getText());
                   //JSONObject answersToQuestion = QAnswer.getAPIAnswer(question.getText());
                   JSONObject answersToQuestion = TeBaQA.getAPIAnswer(question.getText());


                    JSONArray questionsA = (JSONArray) answersToQuestion.get("questions");
                    JSONObject ans = (JSONObject) questionsA.get(0);
                    if (ans.get("id") != null)
                        ans.replace("id", question.getId());
                    else
                        ans.put("id", question.getId());
                    questionsArray.add(ans);

                    numqa++;
                    long t0 = System.currentTimeMillis() - starTime;
                    time += t0;
                    LOG.log(Level.INFO, "Time: {0}", t0);
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Error to process query: " + question.getText(), ex);
                }
            }
            fileObj.put("questions", questionsArray); //mod by lucia
            writer.write(fileObj.toJSONString()); //
            writer.close();
            LOG.log(Level.INFO, "Total time: {0}", time);
            LOG.log(Level.INFO, "Avg time: {0}", time / numq);
        } catch (IOException ex) {
            Logger.getLogger(QaldIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
