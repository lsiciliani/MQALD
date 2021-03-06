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

import di.uniba.it.mqald.QaldIO;
import di.uniba.it.mqald.Question;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
 *
 * @author pierpaolo
 */
public class Eval {

    private static final Logger LOG = Logger.getLogger(Eval.class.getName());

    static Options options;

    static CommandLineParser cmdParser = new DefaultParser();

    static {
        options = new Options();
        options.addOption("g", true, "Gold standard file")
                .addOption("s", true, "System file")
                .addOption("v", false, "Verbose");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<Question> answeredQuestions = new ArrayList<>();
        try {
            CommandLine cmd = cmdParser.parse(options, args);
            if (cmd.hasOption("g") && cmd.hasOption("s")) {
                try {
                    File gf = new File(cmd.getOptionValue("g"));
                    Map<String, Question> goldQuesitons = QaldIO.questionList2Map(QaldIO.read(gf));
                    File sf = new File(cmd.getOptionValue("s"));
                    Map<String, Question> sysQuesitons = QaldIO.questionList2Map(QaldIO.read(sf));
                    if (goldQuesitons.size() != sysQuesitons.size()) {
                        LOG.log(Level.WARNING, "Gold and system have different size");
                        LOG.log(Level.WARNING, "Gold size: {0}", goldQuesitons.size());
                        LOG.log(Level.WARNING, "System size: {0}", sysQuesitons.size());
                    }
                    EvalMetrics m = new EvalMetrics(0, 0, 0);
                    for (String idq : goldQuesitons.keySet()) {
                        Question gq = goldQuesitons.get(idq);
                        Question sq = sysQuesitons.get(idq);
                        if (sq != null) {
                            EvalMetrics r = QaldEval.compareAnswersJSON(gq.getAnswerObj(), sq.getAnswerObj());
                            if (r.getF() != 0) {
                                answeredQuestions.add(gq);
                            }
                            if (cmd.hasOption("v")) {
                                System.out.println("Quesiton id: " + gq.getId());
                                System.out.println(r.toString());
                            }
                            m.add(r);
                        } else {
                            LOG.log(Level.WARNING, "No answer for: {0}", gq.getId());
                            EvalMetrics r = new EvalMetrics(1, 0, 0);
                            m.add(r);
                        }
                    }
                    m.div(goldQuesitons.size());
                    if (cmd.hasOption("v")) {
                        System.out.println("# answered questions: " + answeredQuestions.size());
                        for (Question q : answeredQuestions) {
                            System.out.println("Answered");
                            System.out.println("ID: " + q.getId() + "   " + q.getText());
                        }
                    }
                    System.out.println("Results:");
                    System.out.println(m.toString());
                    System.out.println("F-QALD: " + EvalMetrics.computeF1(m.getPrecision(), m.getRecall()));

                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            } else {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("Evaluate system output against the gold standard.", options, true);
            }
        } catch (ParseException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

}
