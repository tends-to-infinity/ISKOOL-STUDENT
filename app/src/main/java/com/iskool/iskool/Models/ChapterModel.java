package com.iskool.iskool.Models;

import java.util.ArrayList;

public class ChapterModel {

    ModelClass self;
    ModelClass parent;
    ArrayList<ModelClass> topics;

    public ChapterModel(ModelClass self, ModelClass parent) {
        this.self = self;
        this.parent = parent;
    }

    public ModelClass getSelf() {
        return self;
    }

    public void setSelf(ModelClass self) {
        this.self = self;
    }

    public ModelClass getParent() {
        return parent;
    }

    public void setParent(ModelClass parent) {
        this.parent = parent;
    }

    public ArrayList<ModelClass> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<ModelClass> topics) {
        this.topics = topics;
    }

    public ChapterModel() {
    }

    public ChapterModel(ModelClass self, ModelClass parent, ArrayList<ModelClass> topics) {
        this.self = self;
        this.parent = parent;
        this.topics = topics;
    }
}
