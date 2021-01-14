package com.iskool.iskool.Models;

import java.util.ArrayList;

public class Squiz {

    ArrayList<Integer> answers = new ArrayList<>();
    int corr;

    public Squiz() {
    }

    public ArrayList<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Integer> answers) {
        this.answers = answers;
    }

    public int getCorr() {
        return corr;
    }

    public void setCorr(int corr) {
        this.corr = corr;
    }

    public Squiz(ArrayList<Integer> answers, int corr) {
        this.answers = answers;
        this.corr = corr;
    }
}
