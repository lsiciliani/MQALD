package di.uniba.it.mqald;

import di.uniba.it.mqald.QaldIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lucia
 */
public class Test {

    public static void main(String[] args) {
        try {

            // QaldIO.mergeDatasets(new File("/home/lucia/data/QALD/allQALD/train"));
            // QaldIO.filterDataset();
            QaldIO.filterModifiers(new File("/home/lucia/data/QALD/qald_train_7_8_9.json"));
            //QaldIO.createCSV(new File("/home/lucia/data/QALD/qald_7_8_9_all_mods.json"));

            //QaldIO.write(modQuestions, new File("/home/lucia/data/QALD/modDataset/qald9_test_modQuestions.json"));
            //QaldIO.mergeDatasets(new File("/home/lucia/data/QALD/allQALD"));
            System.out.println("DONE!");

        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
