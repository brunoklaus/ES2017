package com.example.bela.es2017.firebase.db.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klaus on 09/09/17.
 */
@IgnoreExtraProperties
public class Receita {

    public  String titulo;
    public  String subtitulo;
    public  String descr;
    public  String imgStorage;
    public  String imgLink;

    public int upvotes = 0;
    public int downvotes = 0;
    public ArrayList<InstIngrediente> ingredientesUsados;

    //Novos Atributos
    public String autor;
    public String category;
    public Dificuldade difficulty;
    public String duration;
    public int img;
    public ArrayList<Passo> passos;
    public ArrayList<String> tags;
    public Integer servings;


    public Receita() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public Receita(String titulo, String subtitulo, String descr,
                   List<InstIngrediente> ingredientesUsados, int img) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.descr = descr;
        this.ingredientesUsados = new ArrayList<>(ingredientesUsados);
        this.img = img;
        this.imgLink = "";

    }

    public Receita(String titulo, String subtitulo, String descr,
                   List<InstIngrediente> ingredientesUsados, int img, String imgLink) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.descr = descr;
        this.ingredientesUsados = new ArrayList<>(ingredientesUsados);
        this.img = img;
        this.imgLink = imgLink;

    }

    public Receita(String titulo, String subtitulo, String descr,
                           List<InstIngrediente> ingredientesUsados, String imgLink,
                           String autor, String category, Dificuldade difficulty, String duration,
                           ArrayList<Passo> passos, ArrayList<String> tags, Integer servings) {
        this(titulo, subtitulo, descr, ingredientesUsados, -1, imgLink);
        this.autor = autor;
        this.category = category;
        this.difficulty = difficulty;
        this.duration = duration;
        this.passos = new ArrayList<>(passos);
        this.tags = new ArrayList<>(tags);
        this.servings = servings;
    }


}
