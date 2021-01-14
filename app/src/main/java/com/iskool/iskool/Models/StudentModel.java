package com.iskool.iskool.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentModel {

    ModelClass self,course;
    HashMap<String,String> assignments=new HashMap<>();
    HashMap<String,String> exams = new HashMap<>();
    HashMap<String,Squiz> quiz = new HashMap<>();

    HashMap<String,ModelClass> topics = new HashMap<>();

    public HashMap<String, ModelClass> getTopics() {
        return topics;
    }

    public void setTopics(HashMap<String, ModelClass> topics) {
        this.topics = topics;
    }

    public StudentModel(ModelClass self, ModelClass course, HashMap<String, String> assignments, HashMap<String, String> exams, HashMap<String, Squiz> quiz, HashMap<String, ModelClass> topics) {
        this.self = self;
        this.course = course;
        this.assignments = assignments;
        this.exams = exams;
        this.quiz = quiz;
        this.topics = topics;
    }

    public HashMap<String, String> getExams() {
        return exams;
    }

    public void setExams(HashMap<String, String> exams) {
        this.exams = exams;
    }

    public StudentModel(ModelClass self, ModelClass course, HashMap<String, String> assignments, HashMap<String, String> exams, HashMap<String, Squiz> quiz) {
        this.self = self;
        this.course = course;
        this.assignments = assignments;
        this.exams = exams;
        this.quiz = quiz;
    }

    public HashMap<String, Squiz> getQuiz() {
        return quiz;
    }

    public void setQuiz(HashMap<String, Squiz> quiz) {
        this.quiz = quiz;
    }

    public StudentModel(ModelClass self, ModelClass course, HashMap<String, String> assignments, HashMap<String, Squiz> quiz) {
        this.self = self;
        this.course = course;
        this.assignments = assignments;
        this.quiz = quiz;
    }

    public HashMap<String, String> getAssignments() {
        return assignments;
    }

    public void setAssignments(HashMap<String, String> assignments) {
        this.assignments = assignments;
    }

    public StudentModel(ModelClass self, ModelClass course, HashMap<String, String> assignments) {
        this.self = self;
        this.course = course;
        this.assignments = assignments;
    }

    public StudentModel() {
    }

    public ModelClass getSelf() {
        return self;
    }

    public void setSelf(ModelClass self) {
        this.self = self;
    }

    public ModelClass getCourse() {
        return course;
    }

    public void setCourse(ModelClass course) {
        this.course = course;
    }

    public StudentModel(ModelClass self, ModelClass course) {
        this.self = self;
        this.course = course;
    }
}
