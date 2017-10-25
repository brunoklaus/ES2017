package com.example.bela.es2017.firebase.db.model;

/**
 * Created by klaus on 24/10/17.
 */

public class ConversorEdge {
    public String id;
    public String n1;
    public String n2;
    public String label;
    public double w;

    public ConversorEdge() {
        //Default Constructor
    }

    public ConversorEdge(String n1, String n2, double w) {
        this.n1 = n1;
        this.n2 = n2;
        this.w = w;
    }

    public ConversorEdge(String n1, String n2, double w, String label) {
        this.n1 = n1;
        this.n2 = n2;
        this.w = w;
        this.label = label;
    }

    public ConversorEdge(ConversorEdge og, boolean reverseDirection) {
        if (reverseDirection) {
            this.n1 = og.n2;
            this.n2 = og.n1;
            this.w = (og.w == 0.0) ? 0.0 : 1/og.w;
        } else {
            this.n1 = og.n1;
            this.n2 = og.n2;
            this.w = og.w;
        }
        this.label = og.label;
    }

    }
