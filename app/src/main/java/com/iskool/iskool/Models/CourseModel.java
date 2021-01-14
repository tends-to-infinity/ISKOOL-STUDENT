package com.iskool.iskool.Models;

import java.util.ArrayList;

public class CourseModel {


    ModelClass self;
    ArrayList<ModelClass> subjects;
    ArrayList<ModelClass> quiz;
    ArrayList<ModelClass> assignments;
    ArrayList<ModelClass> exams;

    public ArrayList<ModelClass> getExams() {
        return exams;
    }

    public void setExams(ArrayList<ModelClass> exams) {
        this.exams = exams;
    }

    public CourseModel(ModelClass self, ArrayList<ModelClass> subjects, ArrayList<ModelClass> quiz, ArrayList<ModelClass> assignments, ArrayList<ModelClass> exams) {
        this.self = self;
        this.subjects = subjects;
        this.quiz = quiz;
        this.assignments = assignments;
        this.exams = exams;
    }

    public ArrayList<ModelClass> getQuiz() {
        return quiz;
    }

    public void setQuiz(ArrayList<ModelClass> quiz) {
        this.quiz = quiz;
    }

    public CourseModel(ModelClass self, ArrayList<ModelClass> subjects, ArrayList<ModelClass> quiz, ArrayList<ModelClass> assignments) {
        this.self = self;
        this.subjects = subjects;
        this.quiz = quiz;
        this.assignments = assignments;
    }

    public ArrayList<ModelClass> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<ModelClass> assignments) {
        this.assignments = assignments;
    }

    public CourseModel(ModelClass self, ArrayList<ModelClass> subjects, ArrayList<ModelClass> assignments) {
        this.self = self;
        this.subjects = subjects;
        this.assignments = assignments;
    }

    public ArrayList<ModelClass> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<ModelClass> subjects) {
        this.subjects = subjects;
    }

    public CourseModel(ModelClass self, ArrayList<ModelClass> subjects) {
        this.self = self;
        this.subjects = subjects;
    }

    public CourseModel() {
    }

    public ModelClass getSelf() {
        return self;
    }

    public void setSelf(ModelClass self) {
        this.self = self;
    }


}
