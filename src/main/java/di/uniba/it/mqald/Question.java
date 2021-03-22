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

import java.util.List;
import org.json.simple.JSONObject;

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
    
    private String qaldVersion;

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

    /**
     *
     * @return
     */
    public List<String> getModifiers() {
        return modifiers;
    }

    /**
     *
     * @param modifiers
     */
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

    /**
     *
     * @return
     */
    public JSONObject getAnswerObj() {
        return answerObj;
    }

    /**
     *
     * @param answerObj
     */
    public void setAnswerObj(JSONObject answerObj) {
        this.answerObj = answerObj;
    }

    public String getQaldVersion() {
        return qaldVersion;
    }

    public void setQaldVersion(String qaldVersion) {
        this.qaldVersion = qaldVersion;
    }
    
    

}
