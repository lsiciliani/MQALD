package di.uniba.it.mqald;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author lucia
 */
public class Answer {
    
     private String text;
     
     private String type;

    /**
     *
     * @param text
     * @param type
     */
    public Answer(String text, String type) {
        this.text = text;
        this.type = type;
    }
    
    /**
     *
     * @param text
     */
    public Answer(String text) {
        this.text = text;
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
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
}
