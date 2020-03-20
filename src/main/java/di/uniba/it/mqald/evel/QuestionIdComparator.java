/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mqald.evel;

import di.uniba.it.mqald.Question;
import java.util.Comparator;

/**
 *
 * @author pierpaolo
 */
public class QuestionIdComparator implements Comparator<Question> {

    @Override
    public int compare(Question t, Question t1) {
        return t.getId().compareTo(t1.getId());
    }

}
