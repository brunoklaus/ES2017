package com.example.bela.es2017.firebase.db.model.sapo;

import java.util.List;

/**
 * Created by klaus on 24/09/17.
 */

public class SapoReceita {

    public String titulo;
    public String subtitulo;
    public String autor;
    public String category;
    public String difficulty;
    public String duration;
    public String servings;
    public List<String> ingr;
    public String img;
    public String descr;

    public SapoReceita(String titulo, String subtitulo, String autor, String category, String difficulty, String duration, String servings, List<String> ingr, String img, String descr) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.autor = autor;
        this.category = category;
        this.difficulty = difficulty;
        this.duration = duration;
        this.servings = servings;
        this.ingr = ingr;
        this.img = img;
        this.descr = descr;
    }
}
