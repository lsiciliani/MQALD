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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

    private static final Logger LOG = Logger.getLogger(QaldIO.class.getName());

    /**
     * Given an input file, this method produces two files: one with queries
     * that contain modifiers and another one containing queries without
     * modifiers
     *
     * @param inputFile Input file
     * @param outputModFile Output file with modifiers
     * @param outputFile Output file without modifiers
     * @throws Exception
     */
    public static void filterModifiers(File inputFile, File outputModFile, File outputFile) throws Exception {
        FileWriter writerMod = new FileWriter(outputModFile);
        JSONObject modsFileObj = new JSONObject();
        JSONArray questionsArray = new JSONArray();
        int qModsID = 1;

        FileWriter noModsWriter = new FileWriter(outputFile);
        JSONObject noModsFileObj = new JSONObject();
        JSONArray noModsQuestionsArray = new JSONArray();
        int noModsID = 1;

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader(inputFile));

        LOG.log(Level.INFO, "Reading file: {0}", inputFile);

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
                q.put("modifiers", new JSONArray());
                noModsQuestionsArray.add(q);
                noModsID++;

            }
        }

        modsFileObj.put("questions", questionsArray);
        writerMod.write(modsFileObj.toJSONString());
        writerMod.close();

        noModsFileObj.put("questions", noModsQuestionsArray);
        noModsWriter.write(noModsFileObj.toJSONString());
        noModsWriter.close();
    }

    /**
     * Save a list of questions into an outputFile. The output file will contain
     * information about modifiers for each query
     *
     * @param questions List of questions
     * @param outputFile Output outputFile
     * @throws IOException
     */
    public static void write(List<Question> questions, File outputFile) throws IOException {
        FileWriter writer = new FileWriter(outputFile);
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
     * Read a QALD JSON file and return a list of questions
     *
     * @param inputFile Input QALD file
     * @return List of questions
     * @throws IOException
     */
    public static List<Question> read(File inputFile) throws IOException {
        List<Question> questions = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(inputFile));
            LOG.log(Level.INFO, "Reading file: {0}", inputFile);
            JSONArray qs = (JSONArray) data.get("questions");
            Iterator it = qs.iterator();
            while (it.hasNext()) {
                Question question = new Question();
                JSONObject q = (JSONObject) it.next();
                question.setId(q.get("id").toString());
                question.setAnswertype((String) q.get("answertype"));
                //question.setAggregation((boolean) q.get("aggregation"));
                //question.setOnlydbo((boolean) q.get("onlydbo"));
                //question.setHybrid((boolean) q.get("hybrid"));
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
                        Iterator it_keyset = binding.keySet().iterator();
                        while (it_keyset.hasNext()) {
                            String key = (String) it_keyset.next();
                            JSONObject single_bind = (JSONObject) binding.get(key);
                            Answer ans = new Answer((String) single_bind.get("value"), (String) single_bind.get("type"));
                            answers.add(ans);
                        }
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

    
    
    /**
     * This method removes from the testing all the queries that occur into the
     * training updating them 
     *
     * @param testingFile Testing file
     * @param trainingFile Training file
     * @param outputFile Output file
     * @throws Exception
     */
    public static void filterDataset(File testingFile, File trainingFile, File outputFile) throws Exception {
        List<Question> train = QaldIO.read(trainingFile);
        List<String> trainQuestions = new ArrayList<>();
        for (Question q : train) {
            trainQuestions.add(q.getText());
        }

        FileWriter writer = new FileWriter(outputFile);
        JSONObject fileObj = new JSONObject();
        JSONArray questionsArray = new JSONArray();

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader(testingFile));
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
            LOG.info(qString);
            if (!trainQuestions.contains(qString)) {
                q.replace("id", i);
                i++;
                questionsArray.add(q);
            } else {
                LOG.info("*");
                contained++;
            }
        }
        fileObj.put("questions", questionsArray);
        writer.write(fileObj.toJSONString());
        writer.close();
        LOG.log(Level.INFO, "total number: {0}", totalnumber);
        LOG.log(Level.INFO, "contained: {0}", contained);

    }


    /**
     * This method merges several QALD files. IMPORTANT: The order of input
     * files is relevant. The first input file should be the most recent QALD
     * dataset.
     *
     * @param outputFile Output file
     * @param inputFiles Input files
     * @throws Exception
     */
    public static void mergeDatasets(File outputFile, File... inputFiles) throws Exception {
        FileWriter writer = new FileWriter(outputFile);
        JSONObject fileObj = new JSONObject();
        JSONArray questionsArray = new JSONArray();
        List<File> files = new ArrayList<>();
        for (File f : inputFiles) {
            files.add(f);
        }
        JSONParser parser = new JSONParser();
        List<String> storedQuestions = new ArrayList<>();
        int contained = 0;
        int totalnumber = 0;
        int i = 1;
        for (File f : files) {
            LOG.log(Level.INFO, "Reading: {0}", f.getName());
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
                LOG.info(qString);
                if (!storedQuestions.contains(qString.toLowerCase())) {
                    storedQuestions.add(qString.toLowerCase());
                    q.replace("id", i);
                    q.put("qald-version", f.getName().replaceAll("[^0-9]", ""));
                    i++;
                    questionsArray.add(q);
                } else {
                    LOG.info("*");
                    q.put("qald-version", f.getName().replaceAll("[^0-9]", ""));
                    questionsArray = updateQuestionArray(questionsArray, q);
                    contained++;
                }

            }
        }
        fileObj.put("questions", questionsArray);
        writer.write(fileObj.toJSONString());
        writer.close();
        LOG.log(Level.INFO, "total number: {0}", totalnumber);
        LOG.log(Level.INFO, "contained: {0}", contained);
    }

    public static JSONArray updateQuestionArray(JSONArray questionsArray, JSONObject q) {
        JSONArray qArray = (JSONArray) q.get("question");
        Iterator iterator = qArray.iterator();
        String qString = "";
        while (iterator.hasNext()) {
            JSONObject questionLanguage = (JSONObject) iterator.next();
            String lan = (String) questionLanguage.get("language");
            if (lan.equals("en")) {
                qString = (String) questionLanguage.get("string");
            }
        }

        Iterator it = questionsArray.iterator();
        while (it.hasNext()) {
            JSONObject qa = (JSONObject) it.next();
            JSONArray quesArray = (JSONArray) qa.get("question");
            Iterator iteratorqa = quesArray.iterator();
            String qaString = "";
            while (iteratorqa.hasNext()) {
                JSONObject questionLanguage = (JSONObject) iteratorqa.next();
                String lanqa = (String) questionLanguage.get("language");
                if (lanqa.equals("en")) {
                    qaString = (String) questionLanguage.get("string");
                }
            }
            if (qaString.equals(qString)) {
                String qVer = (String) q.get("qald-version");
                String qaVer = (String) qa.get("qald-version");
                if (Integer.parseInt(qVer) > Integer.parseInt(qaVer)) {
                    qa.put("answertype", q.get("answertype"));
                    qa.put("aggregation", q.get("aggregation"));
                    qa.put("onlydbo", q.get("onlydbo"));
                    qa.put("hybrid", q.get("hybrid"));
                    qa.put("query", q.get("query"));
                    qa.put("answers", q.get("answers"));
                    qa.put("modifiers", q.get("modifiers"));
                    qa.put("qald-version", q.get("qald-version"));
                }

            }

        }
        return questionsArray;
    }

    /**
     * This method creates a CSV file that contains information about modifiers
     * for each query occurring into the input file (QALD JSON)
     *
     * @param inputFile Input file
     * @param outputFile Output CSV file
     * @throws Exception
     */
    public static void createCSV(File inputFile, File outputFile) throws Exception {
        FileWriter fw = new FileWriter(outputFile);
        BufferedWriter bw = new BufferedWriter(fw);
        Set<String> modSet = Utils.loadModifiersFromFile(new File("modifiers"));
        String[] modsLabels = modSet.toArray(new String[modSet.size()]);
        String[] header = new String[modsLabels.length + 3];
        header[0] = "id";
        header[1] = "question";
        header[2] = "sparql";
        System.arraycopy(modsLabels, 0, header, 3, modsLabels.length);
        CSVPrinter csvPrinter = new CSVPrinter(bw, CSVFormat.DEFAULT.withDelimiter(',').withQuote('"').withHeader(header));

        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(new FileReader(inputFile));

        System.out.println("Reading " + inputFile + " ...");

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

            String[] mods = new String[modsLabels.length];
            Arrays.fill(mods, "0");

            for (int k = 0; k < modsLabels.length; k++) {
                JSONArray jsonMods = (JSONArray) q.get("modifiers");
                Iterator iterator = jsonMods.iterator();

                while (iterator.hasNext()) {
                    String m = (String) iterator.next();
                    if (m.equals(modsLabels[k])) {
                        mods[k] = "1";
                    }
                }
            }

            JSONObject query = (JSONObject) q.get("query");
            String queryText = (String) query.get("sparql");

            String[] values = new String[mods.length + 3];
            values[0] = id;
            values[1] = questionText;
            values[2] = queryText;
            System.arraycopy(mods, 0, values, 3, mods.length);

            csvPrinter.printRecord(values);
        }
        csvPrinter.flush();
        csvPrinter.close();
    }

}
