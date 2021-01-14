package com.iskool.iskool.Models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class HomeworkModel {


    ModelClass self;
    ArrayList<ModelClass> topics,quiz,assignments;
    Timestamp timestamp;

    public ModelClass getSelf() {
        return self;
    }

    public void setSelf(ModelClass self) {
        this.self = self;
    }

    public ArrayList<ModelClass> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<ModelClass> topics) {
        this.topics = topics;
    }

    public ArrayList<ModelClass> getQuiz() {
        return quiz;
    }

    public void setQuiz(ArrayList<ModelClass> quiz) {
        this.quiz = quiz;
    }

    public ArrayList<ModelClass> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<ModelClass> assignments) {
        this.assignments = assignments;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public HomeworkModel() {
    }

    public HomeworkModel(ModelClass self, ArrayList<ModelClass> topics, ArrayList<ModelClass> quiz, ArrayList<ModelClass> assignments, Timestamp timestamp) {
        this.self = self;
        this.topics = topics;
        this.quiz = quiz;
        this.assignments = assignments;
        this.timestamp = timestamp;
    }


}
