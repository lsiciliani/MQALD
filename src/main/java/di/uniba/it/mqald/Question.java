package di.uniba.it.mqald;


import java.util.List;
import org.json.simple.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author lucia
 */
public class Question {

    private String id;

    private String answertype;

    private boolean aggregation;

    private boolean onlydbo;

    private boolean hybrid;

    private String text;

    private String query;
    
    private List<String> modifiers;

    private List<Answer> answers;

    private JSONObject answerObj;

    /**
     *
     */
    public Question() {
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getAnswertype() {
        return answertype;
    }

    /**
     *
     * @param answertype
     */
    public void setAnswertype(String answertype) {
        this.answertype = answertype;
    }

    /**
     *
     * @return
     */
    public boolean isAggregation() {
        return aggregation;
    }

    /**
     *
     * @param aggregation
     */
    public void setAggregation(boolean aggregation) {
        this.aggregation = aggregation;
    }

    /**
     *
     * @return
     */
    public boolean isOnlydbo() {
        return onlydbo;
    }

    /**
     *
     * @param onlydbo
     */
    public void setOnlydbo(boolean onlydbo) {
        this.onlydbo = onlydbo;
    }

    /**
     *
     * @return
     */
    public boolean isHybrid() {
        return hybrid;
    }

    /**
     *
     * @param hybrid
     */
    public void setHybrid(boolean hybrid) {
        this.hybrid = hybrid;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     *
     * @param query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }

    /**
     *
     * @return
     */
    public List<Answer> getAnswers() {
        return answers;
    }

    /**
     *
     * @param answers
     */
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public JSONObject getAnswerObj() {
        return answerObj;
    }

    public void setAnswerObj(JSONObject answerObj) {
        this.answerObj = answerObj;
    }

}

