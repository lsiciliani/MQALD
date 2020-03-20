package di.uniba.mqald;


import di.uniba.mqald.QaldIO;
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
    
    public static void main (String[] args) {
        try {
            /*
            List<String> trainQald9 = QaldIO.read(new File("/home/lucia/data/QALD/allQALD/train/qald9_train.json"));
            List<String> trainQald8 = QaldIO.read(new File("/home/lucia/data/QALD/allQALD/train/qald8_train.json"));
            List<String> trainQald7 = QaldIO.read(new File("/home/lucia/data/QALD/allQALD/train/qald7_train.json"));
        
            List<String> testQald9 = QaldIO.read(new File("/home/lucia/data/QALD/allQALD/test/qald9_test.json"));
            List<String> testQald8 = QaldIO.read(new File("/home/lucia/data/QALD/allQALD/test/qald8_test.json"));
            List<String> testQald7 = QaldIO.read(new File("/home/lucia/data/QALD/allQALD/test/qald7_test.json"));
           
            for (String q7 : testQald7) {
                if (trainQald9.contains(q7)) {
                    System.out.println("CONTAINED: " + q7);
                } else {
                    System.out.println("NOT CONTAINED: " + q7);
                }
            }
            */
            
           // QaldIO.mergeDatasets(new File("/home/lucia/data/QALD/allQALD/train"));
          // QaldIO.filterDataset();
//Utils.ganswer("What is the capital of China?");
            QaldIO.filterModifiers(new File("/home/lucia/data/QALD/qald_train_7_8_9.json"));
            //QaldIO.createCSV(new File("/home/lucia/data/QALD/qald_7_8_9_all_mods.json"));
            
           /* List<Question> questions1 = QaldIO.read(new File("/home/lucia/data/QALD/qald_all_modQuestions_merge.json"));
            List<Question> questions2 = QaldIO.read(new File("/home/lucia/data/QALD/qald_all_mods_new.json"));
            List<String> questionsList1 = new ArrayList<>();
            List<String> questionsList2 = new ArrayList<>();
            
            int i = 1;
            for (Question q : questions1) {
                questionsList1.add(q.getText());
                System.out.println(i + " : " + q.getText());
                i++;
            }
            
            int j = 1;
             for (Question q : questions2) {
                questionsList2.add(q.getText());
                
                System.out.println(j + " : " + q.getText());
                if (questionsList1.contains(q.getText()))
                     System.out.println("**");
                j++;
            }
            
            questionsList2.removeAll(questionsList1);
            
             for (String s : questionsList2) {
                 System.out.println(s);
            } */
            
            System.out.println("DONE!");
           //QaldIO.prettyPrint(questions);
            
           //List<String> modQuestions = Utils.getQuestionsIDWithModifiers(questions);
            
           //QaldIO.write(modQuestions, new File("/home/lucia/data/QALD/modDataset/qald9_test_modQuestions.json"));
           
           //QaldIO.mergeDatasets(new File("/home/lucia/data/QALD/allQALD"));
           
           
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
