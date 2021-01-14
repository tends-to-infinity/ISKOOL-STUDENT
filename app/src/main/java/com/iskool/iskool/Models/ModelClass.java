package com.iskool.iskool.Models;

import com.google.firebase.firestore.DocumentReference;

public class ModelClass {

    String name;
    DocumentReference reff;

    public ModelClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentReference getReff() {
        return reff;
    }

    public void setReff(DocumentReference reff) {
        this.reff = reff;
    }

    public ModelClass(String name, DocumentReference reff) {
        this.name = name;
        this.reff = reff;
    }
}
