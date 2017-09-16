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
    public  int img;

    public int upvotes = 0;
    public int downvotes = 0;
    public ArrayList<InstIngrediente> ingredientesUsados;

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

    }




}
