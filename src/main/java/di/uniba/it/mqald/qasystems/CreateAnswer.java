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

import di.uniba.it.mqald.QaldIO;
import di.uniba.it.mqald.Question;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * This class creates a JSON QALD file that contains answers to a JSON QALD file
 * Currently, three systems are available: GAnswer, QAnswer, TeBaQA.
 *
 * @author lucia
 */
public class CreateAnswer {

    private static final Logger LOG = Logger.getLogger(CreateAnswer.class.getName());

    static Options options;

    static CommandLineParser cmdParser = new DefaultParser();

    static {
        options = new Options();
        options.addOption("i", true, "Input file (questions)")
                .addOption("o", true, "Output file (answers)")
                .addOption("s", true, "System name (available systems: GAnswer, QAnswer, TeBaQA");
    }

    /**
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        LOG.setLevel(Level.INFO);
        try {
            CommandLine cmd = cmdParser.parse(options, args);
            if (cmd.hasOption("i") && cmd.hasOption("o") && cmd.hasOption("s")) {
                try {
                    FileWriter tmpfile = new FileWriter(cmd.getOptionValue("o") + ".tmp");
                    QASystem system = (QASystem) ClassLoader.getSystemClassLoader().loadClass("di.uniba.it.mqald.qasystems." + cmd.getOptionValue("s")).newInstance();
                    long time = 0;
                    int numq = 0;
                    int numqa = 0;
                    List<Question> questions = QaldIO.read(new File(cmd.getOptionValue("i")));
                    FileWriter writer = new FileWriter(cmd.getOptionValue("o"));
                    JSONObject fileObj = new JSONObject();
                    JSONArray questionsArray = new JSONArray();
                    for (Question question : questions) {
                        numq++;
                        long starTime = System.currentTimeMillis();
                        LOG.log(Level.INFO, "{0}: {1}", new Object[]{question.getId(), question.getText()});
                        try {
                            JSONObject answersToQuestion = system.getAnswer(question.getText());
                            JSONArray questionsA = (JSONArray) answersToQuestion.get("questions");
                            JSONObject ans = (JSONObject) questionsA.get(0);
                            if (ans.get("id") != null) {
                                ans.replace("id", question.getId());
                            } else {
                                ans.put("id", question.getId());
                            }
                            questionsArray.add(ans);
                            tmpfile.write(ans.toJSONString());
                            tmpfile.write("\n");
                            tmpfile.flush();
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
                    tmpfile.close();
                    LOG.log(Level.INFO, "Total questions/Answered questions: {0}/{1}", new Object[]{numq, numqa});
                    LOG.log(Level.INFO, "Total time: {0}", time);
                    LOG.log(Level.INFO, "Avg time: {0}", time / numq);
                } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
                    Logger.getLogger(QaldIO.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("Create the answers JSON file for a specific QA system. Available systems are: GAnswer, QAnswer, TeBaQA.", options, true);
            }
        } catch (ParseException ex) {
            Logger.getLogger(QaldIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
