package com.iskool.iskool.Models;

import com.google.firebase.Timestamp;

public class ExamModel {
    ModelClass self;
    String link;
    Timestamp startTime;
    int duration;

    public ModelClass getSelf() {
        return self;
    }

    public void setSelf(ModelClass self) {
        this.self = self;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ExamModel(ModelClass self, String link, Timestamp startTime, int duration) {
        this.self = self;
        this.link = link;
        this.startTime = startTime;
        this.duration = duration;
    }

    public ExamModel() {
    }
}
