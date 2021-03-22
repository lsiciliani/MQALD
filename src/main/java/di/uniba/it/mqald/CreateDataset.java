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

import java.io.File;
import java.io.IOException;
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
 * @author lucia
 */
public class CreateDataset {

    private static final Logger LOG = Logger.getLogger(CreateDataset.class.getName());

    static Options options;

    static CommandLineParser cmdParser = new DefaultParser();

    static {
        options = new Options();
        options.addOption("d", true, "Resources directory");
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
            CommandLine cmd = cmdParser.parse(options, args);
            if (cmd.hasOption("d")) {
                try {
                    LOG.info("Check files...");
                    for (int i = 7; i <= 9; i++) {
                        File file = new File(cmd.getOptionValue("d") + "/qald-" + i + "-train-multilingual.json");
                        if (!file.exists()) {
                            throw new IOException("File not present: " + file.getCanonicalPath());
                        }
                        file = new File(cmd.getOptionValue("d") + "/qald-" + i + "-test-multilingual.json");
                        if (!file.exists()) {
                            throw new IOException("File not present: " + file.getCanonicalPath());
                        }
                    }
                    LOG.info("Merge training...");
                    QaldIO.mergeDatasets(new File(cmd.getOptionValue("d") + "/QALD-train-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/qald-9-train-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/qald-8-train-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/qald-7-train-multilingual.json"));
                    LOG.info("Merge test...");
                    QaldIO.mergeDatasets(new File(cmd.getOptionValue("d") + "/test-pre-merge-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/qald-9-test-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/qald-8-test-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/qald-7-test-multilingual.json"));
                    LOG.info("Filtering test...");
                    QaldIO.filterDataset(new File(cmd.getOptionValue("d") + "/test-pre-merge-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/QALD-train-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/QALD-test-multilingual.json"));
                    new File(cmd.getOptionValue("d") + "/test-pre-merge-multilingual.json").delete();
                    LOG.info("Filtering modifiers...");
                    QaldIO.filterModifiers(new File(cmd.getOptionValue("d") + "/QALD-train-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/QALD-train-MOD-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/QALD-train-NOMOD-multilingual.json"));
                    QaldIO.filterModifiers(new File(cmd.getOptionValue("d") + "/QALD-test-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/QALD-test-MOD-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/QALD-test-NOMOD-multilingual.json"));
                    LOG.info("Create MQALD test...");
                    QaldIO.mergeDatasets(new File(cmd.getOptionValue("d") + "MQALD.json"),
                            new File(cmd.getOptionValue("d") + "/MQALD_new_query.json"),
                            new File(cmd.getOptionValue("d") + "/QALD-test-MOD-multilingual.json"));
                    QaldIO.mergeDatasets(new File(cmd.getOptionValue("d") + "MQALD-QALD-test-NOMOD.json"),
                            new File(cmd.getOptionValue("d") + "/MQALD_new_query.json"),
                            new File(cmd.getOptionValue("d") + "/QALD-test-multilingual.json"));
                    LOG.info("Create CSV...");
                    QaldIO.createCSV(new File(cmd.getOptionValue("d") + "/QALD-train-MOD-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/QALD-train-MOD-multilingual.csv"));
                    QaldIO.createCSV(new File(cmd.getOptionValue("d") + "/QALD-test-MOD-multilingual.json"),
                            new File(cmd.getOptionValue("d") + "/QALD-test-MOD-multilingual.csv"));
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            } else {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("Create dataset", options, true);
            }
        } catch (ParseException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

}
