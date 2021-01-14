package com.iskool.iskool.Models;

public class TopicModel
{

    ModelClass self;
    ModelClass parent;
    String vpath;

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

    public String getVpath() {
        return vpath;
    }

    public void setVpath(String vpath) {
        this.vpath = vpath;
    }

    public TopicModel() {
    }

    public TopicModel(ModelClass self, ModelClass parent) {
        this.self = self;
        this.parent = parent;
    }

    public TopicModel(ModelClass self, ModelClass parent, String vpath) {
        this.self = self;
        this.parent = parent;
        this.vpath = vpath;
    }

}
