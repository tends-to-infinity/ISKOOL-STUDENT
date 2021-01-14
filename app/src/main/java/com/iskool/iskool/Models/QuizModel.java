package com.iskool.iskool.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizModel {


        ModelClass self,topic;
        ArrayList<Integer> answer;
        ArrayList<String> ques;
        HashMap<String,Integer> students = new HashMap<>();

    public HashMap<String, Integer> getStudents() {
        return students;
    }

    public void setStudents(HashMap<String, Integer> students) {
        this.students = students;
    }

    public QuizModel(ModelClass self, ModelClass topic, ArrayList<Integer> answer, ArrayList<String> ques, HashMap<String, Integer> students, HashMap<String, ArrayList<String>> options) {
        this.self = self;
        this.topic = topic;
        this.answer = answer;
        this.ques = ques;
        this.students = students;
        this.options = options;
    }

    HashMap<String,ArrayList<String>> options = new HashMap<>();

        public QuizModel() {
        }

        public ModelClass getSelf() {
            return self;
        }

        public void setSelf(ModelClass self) {
            this.self = self;
        }

        public ModelClass getTopic() {
            return topic;
        }

        public void setTopic(ModelClass topic) {
            this.topic = topic;
        }

        public ArrayList<Integer> getAnswer() {
            return answer;
        }

        public void setAnswer(ArrayList<Integer> answer) {
            this.answer = answer;
        }

        public ArrayList<String> getQues() {
            return ques;
        }

        public void setQues(ArrayList<String> ques) {
            this.ques = ques;
        }

        public HashMap<String, ArrayList<String>> getOptions() {
            return options;
        }

        public void setOptions(HashMap<String, ArrayList<String>> options) {
            this.options = options;
        }

        public QuizModel(ModelClass self, ModelClass topic, ArrayList<Integer> answer, ArrayList<String> ques, HashMap<String, ArrayList<String>> options) {
            this.self = self;
            this.topic = topic;
            this.answer = answer;
            this.ques = ques;
            this.options = options;
        }


}
