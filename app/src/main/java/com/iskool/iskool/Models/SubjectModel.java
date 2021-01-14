package com.iskool.iskool.Models;

import java.util.ArrayList;

public class SubjectModel {

        ModelClass self;
        ModelClass parent;
        ArrayList<ModelClass> chapters;

        public SubjectModel(ModelClass self, ModelClass parent) {
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

        public ArrayList<ModelClass> getChapters() {
            return chapters;
        }

        public void setChapters(ArrayList<ModelClass> chapters) {
            this.chapters = chapters;
        }

        public SubjectModel(ModelClass self, ModelClass parent, ArrayList<ModelClass> chapters) {
            this.self = self;
            this.parent = parent;
            this.chapters = chapters;
        }

        public SubjectModel() {
        }


    }
