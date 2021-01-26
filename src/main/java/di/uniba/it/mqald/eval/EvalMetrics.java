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

/**
 *
 * @author pierpaolo
 */
public class EvalMetrics {

    double precision = 0;

    double recall = 0;

    double f = 0;

    /**
     *
     */
    public EvalMetrics() {
    }

    /**
     *
     * @param d
     * @param d1
     */
    public EvalMetrics(double precision, double recall) {
        this.precision = precision;
        this.recall = recall;
    }

    /**
     *
     * @param d
     * @param d1
     * @param d2
     */
    public EvalMetrics(double precision, double recall, double f) {
        this.precision = precision;
        this.recall = recall;
        this.f = f;
    }

    /**
     *
     * @return
     */
    public double getPrecision() {
        return precision;
    }

    /**
     *
     * @param precision
     */
    public void setPrecision(double precision) {
        this.precision = precision;
    }

    /**
     *
     * @return
     */
    public double getRecall() {
        return recall;
    }

    /**
     *
     * @param recall
     */
    public void setRecall(double recall) {
        this.recall = recall;
    }

    /**
     *
     * @return
     */
    public double getF() {
        return f;
    }

    /**
     *
     * @param f
     */
    public void setF(double f) {
        this.f = f;
    }

    /**
     *
     * @param p
     * @param r
     * @return
     */
    public static double computeF1(double p, double r) {
        if ((p + r) == 0) {
            return 0;
        } else {
            return 2 * p * r / (p + r);
        }
    }

    /**
     *
     * @param m
     */
    public void add(EvalMetrics m) {
        this.precision += m.getPrecision();
        this.recall += m.getRecall();
        this.f += m.getF();
    }

    /**
     *
     * @param n
     */
    public void div(double n) {
        if (n != 0) {
            this.precision /= n;
            this.recall /= n;
            this.f /= n;
        }
    }

    @Override
    public String toString() {
        return "EvalMetrics{" + "precision=" + precision + ", recall=" + recall + ", f=" + f + '}';
    }

}
