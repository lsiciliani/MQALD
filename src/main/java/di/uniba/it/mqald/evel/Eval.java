/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mqald.evel;

import di.uniba.it.mqald.QaldIO;
import di.uniba.it.mqald.Question;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
                .addOption("s", true, "System file");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            CommandLine cmd = cmdParser.parse(options, args);
            if (cmd.hasOption("g") && cmd.hasOption("s")) {
                try {
                    File gf = new File(cmd.getOptionValue("g"));
                    List<Question> goldQuesitons = QaldIO.read(gf);
                    File sf = new File(cmd.getOptionValue("s"));
                    List<Question> sysQuesitons = QaldIO.read(sf);
                    if (goldQuesitons.size() == sysQuesitons.size()) {
                        Collections.sort(goldQuesitons, new QuestionIdComparator());
                        Collections.sort(sysQuesitons, new QuestionIdComparator());
                        int i = 0;
                        EvalMetrics m = new EvalMetrics(0, 0, 0);
                        while (i < goldQuesitons.size()) {
                            Question gq = goldQuesitons.get(i);
                            Question sq = sysQuesitons.get(i);
                            if (gq.getId().equals(sq.getId())) {
                                LOG.log(Level.INFO, "Quesiton id: {0}", gq.getId());
                                EvalMetrics r = QaldEval.compareAnswersJSON(gq.getAnswerObj(), sq.getAnswerObj());
                                System.out.println(r.toString());
                                m.add(r);
                            } else {
                                LOG.warning("Invalid id");
                                break;
                            }
                            i++;
                        }
                        m.div(goldQuesitons.size());
                        System.out.println(m.toString());
                        System.out.println("F-QALD: "+EvalMetrics.computeF1(m.getPrecision(), m.getRecall()));
                    } else {
                        LOG.log(Level.WARNING, "Gold and sysytem have different size");
                    }
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            } else {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("Evalutaion script", options, true);
            }
        } catch (ParseException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

}
