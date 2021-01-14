package com.iskool.iskool.Models;

import java.util.HashMap;

public class AssignmentModel {

    ModelClass self;
    ModelClass topicReq;
    String link;
    HashMap<String,String> students=new HashMap<>();

    public HashMap<String, String> getStudents() {
        return students;
    }

    public void setStudents(HashMap<String, String> students) {
        this.students = students;
    }

    public AssignmentModel(ModelClass self, ModelClass topicReq, String link, HashMap<String, String> students) {
        this.self = self;
        this.topicReq = topicReq;
        this.link = link;
        this.students = students;
    }

    public ModelClass getSelf() {
        return self;
    }

    public void setSelf(ModelClass self) {
        this.self = self;
    }

    public ModelClass getTopicReq() {
        return topicReq;
    }

    public void setTopicReq(ModelClass topicReq) {
        this.topicReq = topicReq;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AssignmentModel() {
    }

    public AssignmentModel(ModelClass self, ModelClass topicReq, String link) {
        this.self = self;
        this.topicReq = topicReq;
        this.link = link;
    }


}
