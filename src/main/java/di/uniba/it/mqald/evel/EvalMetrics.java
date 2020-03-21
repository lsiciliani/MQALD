/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mqald.evel;

/**
 *
 * @author pierpaolo
 */
public class EvalMetrics {

    double precision = 0;

    double recall = 0;

    double f = 0;

    public EvalMetrics() {
    }

    public EvalMetrics(double precision, double recall) {
        this.precision = precision;
        this.recall = recall;
    }

    public EvalMetrics(double precision, double recall, double f) {
        this.precision = precision;
        this.recall = recall;
        this.f = f;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public static double computeF1(double p, double r) {
        if ((p + r) == 0) {
            return 0;
        } else {
            return 2 * p * r / (p + r);
        }
    }

    public void add(EvalMetrics m) {
        this.precision += m.getPrecision();
        this.recall += m.getRecall();
        this.f += m.getF();
    }

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
