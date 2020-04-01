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

package di.uniba.it.mqald;

import di.uniba.it.mqald.Answer;
import di.uniba.it.mqald.Question;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lucia
 */
public class QaldIO {
    
    // dato il file di input crea due output uno con modifier l'altro senza modifier
    public static void filterModifiers(File inputFile) throws Exception {
        FileWriter writer = new FileWriter("/home/lucia/data/QALD/qald_train_7_8_9_MODS.json");
        JSONObject fileObj = new JSONObject();
        JSONArray questionsArray = new JSONArray();
        int qModsID = 1;

        FileWriter noModsWriter = new FileWriter("/home/lucia/data/QALD/qald_train_7_8_9_NO_MODS.json");
        JSONObject noModsFileObj = new JSONObject();
        JSONArray noModsQuestionsArray = new JSONArray();
        int noModsID = 1;

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader(inputFile));

        System.out.println("***Reading " + inputFile + " ...");

        JSONArray qs = (JSONArray) data.get("questions");
        Iterator it = qs.iterator();

        while (it.hasNext()) {
            JSONObject q = (JSONObject) it.next();

            String query = (String) ((JSONObject) q.get("query")).get("sparql");

            List<String> mods = Utils.checkFilters(query);
            if (!mods.isEmpty()) {
                JSONArray modifiers = new JSONArray();
                for (String m : mods) {
                    modifiers.add(m);
                }
                q.put("modifiers", modifiers);
                q.replace("id", qModsID);
                questionsArray.add(q);
                qModsID++;
            } else {

                q.replace("id", noModsID);
                noModsQuestionsArray.add(q);
                noModsID++;

            }
        }

        fileObj.put("questions", questionsArray);
        writer.write(fileObj.toJSONString());
        writer.close();

        noModsFileObj.put("questions", noModsQuestionsArray);
        noModsWriter.write(noModsFileObj.toJSONString());
        noModsWriter.close();
    }
    
    //salva question in un file
    public static void write(List<Question> questions, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        JSONObject fileObj = new JSONObject();
        JSONArray questionsArray = new JSONArray();
        for (Question q : questions) {
            JSONObject obj = new JSONObject();

            obj.put("id", q.getId());

            obj.put("answertype", q.getAnswertype());

            JSONArray question = new JSONArray();
            JSONObject qust = new JSONObject();
            qust.put("language", "en");
            qust.put("string", q.getText());
            question.add(qust);
            obj.put("question", question);

            JSONArray modifiers = new JSONArray();
            for (String m : q.getModifiers()) {
                JSONObject mod = new JSONObject();
                modifiers.add(m);
            }
            obj.put("modifiers", modifiers);

            JSONObject query = new JSONObject();
            query.put("sparql", q.getQuery());
            obj.put("query", query);

            JSONArray answers = new JSONArray();
            List<Answer> answer = q.getAnswers();
            if (!answer.isEmpty()) {
                for (Answer a : answer) {
                    JSONObject ans = new JSONObject();
                    ans.put("type", a.getType());
                    ans.put("value", a.getText());
                    answers.add(ans);
                }
            } else {
                JSONObject answerObj = q.getAnswerObj();

                JSONObject ans = new JSONObject();
                ans.put("type", "boolean");
                ans.put("value", answerObj.get("boolean").toString());
                answers.add(ans);
            }
            obj.put("answers", answers);

            questionsArray.add(obj);
        }
        fileObj.put("questions", questionsArray);
        writer.write(fileObj.toJSONString());
        writer.close();
    }

    /**
     *
     * @param questions
     */
    public static void prettyPrint(List<Question> questions) {
        for (Question q : questions) {
            System.out.println("id: " + q.getId());
            System.out.println("ansType: " + q.getAnswertype());
            System.out.println("query: " + q.getQuery());
            System.out.println("text: " + q.getText());

            int i = 0;
            for (Answer a : q.getAnswers()) {
                System.out.println("ans" + i + " " + a.getText() + "\ttype:" + a.getType());
                i++;
            }
            System.out.println("\n\n\n");
        }
    }

    /**
     *
     * @param inputFile
     * @return
     * @throws IOException
     */
    public static List<Question> read(File inputFile) throws IOException {
        List<Question> questions = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(inputFile));

            System.out.println("Reading " + inputFile + " ...");

            JSONArray qs = (JSONArray) data.get("questions");
            Iterator it = qs.iterator();

            while (it.hasNext()) {
                Question question = new Question();
                JSONObject q = (JSONObject) it.next();

                question.setId(q.get("id").toString());
              
                question.setAnswertype((String) q.get("answertype"));
//                question.setAggregation((boolean) q.get("aggregation"));
//                question.setOnlydbo((boolean) q.get("onlydbo"));
//                question.setHybrid((boolean) q.get("hybrid"));

                if (q.containsKey("question")) {
                    JSONArray elem = (JSONArray) q.get("question");
                    Iterator it_elem = elem.iterator();
                    while (it_elem.hasNext()) {
                        JSONObject q_obj = (JSONObject) it_elem.next();
                        if (((String) q_obj.get("language")).equals("en")) //finding the question written in english
                        {
                            question.setText((String) q_obj.get("string"));
                        }
                    }
                }
                if (q.containsKey("query")) {
                    question.setQuery((String) ((JSONObject) q.get("query")).get("sparql"));
                }
                JSONArray ans_array = (JSONArray) q.get("answers");
                Iterator it_ans = ans_array.iterator();
                JSONObject answ = (JSONObject) it_ans.next();
                question.setAnswerObj(answ);
                JSONObject res = (JSONObject) answ.get("results");
                List<Answer> answers = new ArrayList<>();
                if (res != null && !res.isEmpty()) {
                    JSONArray bindings = (JSONArray) res.get("bindings");
                    Iterator it_bin = bindings.iterator();
                    while (it_bin.hasNext()) {
                        JSONObject binding = (JSONObject) it_bin.next();
                        String key = (String) binding.keySet().iterator().next();

                        JSONObject single_bind = (JSONObject) binding.get(key);
                        Answer ans = new Answer((String) single_bind.get("value"), (String) single_bind.get("type"));
                        answers.add(ans);

                    }

                }

                question.setAnswers(answers);

                questions.add(question);
            }

        } catch (ParseException ex) {
            Logger.getLogger(QaldIO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return questions;
    }
    
    //togli dal test tutte le query che stanno nel traning
    public static void filterDataset() throws Exception {
        List<Question> train = QaldIO.read(new File("/home/lucia/data/QALD/qald_train_7_8_9.json"));
        List<String> trainQuestions = new ArrayList<>();
        for (Question q : train) {
            trainQuestions.add(q.getText());
        }

        FileWriter writer = new FileWriter("/home/lucia/data/QALD/qald_test_7_8_9_filtered.json");
        JSONObject fileObj = new JSONObject();
        JSONArray questionsArray = new JSONArray();

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader("/home/lucia/data/QALD/qald_test_7_8_9.json"));
        JSONArray qs = (JSONArray) data.get("questions");
        Iterator it = qs.iterator();

        int totalnumber = 0;
        int contained = 0;
        int i = 1;
        while (it.hasNext()) {
            totalnumber++;
            JSONObject q = (JSONObject) it.next();
            JSONArray quesArray = (JSONArray) q.get("question");
            Iterator iterator = quesArray.iterator();
            String qString = "";
            while (iterator.hasNext()) {
                JSONObject questionLanguage = (JSONObject) iterator.next();
                String lan = (String) questionLanguage.get("language");
                if (lan.equals("en")) {
                    qString = (String) questionLanguage.get("string");
                }
            }
            System.out.println(qString);
            if (!trainQuestions.contains(qString)) {
                q.replace("id", i);
                i++;
                questionsArray.add(q);
            } else {
                System.out.println("*");
                contained++;
            }
        }
        fileObj.put("questions", questionsArray);
        writer.write(fileObj.toJSONString());
        writer.close();
        System.out.println("total number: " + totalnumber);
        System.out.println("contained: " + contained);

    }
    
    // merge dei dataset quello passato per primo e quello più aggiornato quindi non viene fatto il replace della domanda
    public static void mergeDatasets(File dir) throws Exception {
        FileWriter writer = new FileWriter("/home/lucia/data/QALD/qald_train_7_8_9.json");
        JSONObject fileObj = new JSONObject();
        JSONArray questionsArray = new JSONArray();

        //File[] directoryListing = dir.listFiles();
        List<File> directoryListing = new ArrayList<>();
        directoryListing.add(new File("/home/lucia/data/QALD/allQALD/train/qald9_train.json"));
        directoryListing.add(new File("/home/lucia/data/QALD/allQALD/train/qald8_train.json"));
        directoryListing.add(new File("/home/lucia/data/QALD/allQALD/train/qald7_train.json"));

        JSONParser parser = new JSONParser();
        List<String> storedQuestions = new ArrayList<>();
        int contained = 0;
        int totalnumber = 0;
        int i = 1;
        for (File f : directoryListing) {
            System.out.println("******" + f.getCanonicalPath());
            JSONObject data = (JSONObject) parser.parse(new FileReader(f));
            JSONArray qs = (JSONArray) data.get("questions");
            Iterator it = qs.iterator();

            while (it.hasNext()) {
                totalnumber++;
                JSONObject q = (JSONObject) it.next();
                JSONArray quesArray = (JSONArray) q.get("question");
                Iterator iterator = quesArray.iterator();
                String qString = "";
                while (iterator.hasNext()) {
                    JSONObject questionLanguage = (JSONObject) iterator.next();
                    String lan = (String) questionLanguage.get("language");
                    if (lan.equals("en")) {
                        qString = (String) questionLanguage.get("string");
                    }
                }
                System.out.println(qString);
                if (!storedQuestions.contains(qString.toLowerCase())) {
                    storedQuestions.add(qString.toLowerCase());
                    q.replace("id", i);
                    i++;
                    questionsArray.add(q);
                } else {
                    System.out.println("*");
                    contained++;
                }

            }
        }
        fileObj.put("questions", questionsArray);
        writer.write(fileObj.toJSONString());
        writer.close();
        System.out.println("total number: " + totalnumber);
        System.out.println("contained: " + contained);
    }
    
    // crea CSV con statistiche modifier
    // IMPR: modifier caricati da file
    // non utilizzando come input
    public static void createCSV(File file) throws Exception {
        FileWriter fw = new FileWriter(new File("/home/lucia/data/QALD/csv_qald_train_7_8_9_MODS.csv"));
        BufferedWriter bw = new BufferedWriter(fw);
        CSVPrinter csvPrinter = new CSVPrinter(bw, CSVFormat.TDF.withHeader("id", "question", "sparql", "ask", "bind", "filter", "having", "group by", "union", "limit", "offset", "order by", "count", "now", "year"));
        List<String> modsLabes = new ArrayList<>();
        modsLabes.add(0, "ASK");
        modsLabes.add(1, "BIND");
        modsLabes.add(2, "FILTER");
        modsLabes.add(3, "HAVING");
        modsLabes.add(4, "GROUP BY");
        modsLabes.add(5, "UNION");
        modsLabes.add(6, "LIMIT");
        modsLabes.add(7, "OFFSET");
        modsLabes.add(8, "ORDER BY");
        modsLabes.add(9, "COUNT");
        modsLabes.add(10, "NOW");
        modsLabes.add(11, "YEAR");

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader(file));

        System.out.println("Reading " + file + " ...");

        JSONArray qs = (JSONArray) data.get("questions");
        Iterator it = qs.iterator();

        while (it.hasNext()) {
            JSONObject q = (JSONObject) it.next();

            String id = String.valueOf(q.get("id"));
            String questionText = "";
            JSONArray questionJson = (JSONArray) q.get("question");

            Iterator it_elem = questionJson.iterator();
            while (it_elem.hasNext()) {
                JSONObject q_obj = (JSONObject) it_elem.next();
                if (((String) q_obj.get("language")).equals("en")) //finding the question written in english
                {
                    questionText = (String) q_obj.get("string");
                }
            }

            List<String> mods = new ArrayList<>();
            mods.add(0, "");
            mods.add(1, "");
            mods.add(2, "");
            mods.add(3, "");
            mods.add(4, "");
            mods.add(5, "");
            mods.add(6, "");
            mods.add(7, "");
            mods.add(8, "");
            mods.add(9, "");
            mods.add(10, "");
            mods.add(11, "");

            for (String ml : modsLabes) {
                JSONArray jsonMods = (JSONArray) q.get("modifiers");
                Iterator iterator = jsonMods.iterator();

                while (iterator.hasNext()) {
                    String m = (String) iterator.next();
                    if (m.equals(ml)) {
                        mods.add(modsLabes.indexOf(ml), "x");
                    }
                }
            }

            JSONObject query = (JSONObject) q.get("query");
            String queryText = (String) query.get("sparql");

            csvPrinter.printRecord(id, questionText, queryText, mods.get(0), mods.get(1), mods.get(2), mods.get(3), mods.get(4), mods.get(5), mods.get(6), mods.get(7), mods.get(8), mods.get(9), mods.get(10), mods.get(11));

        }
        csvPrinter.flush();
        csvPrinter.close();
    }

}
