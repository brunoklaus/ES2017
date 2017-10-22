package com.example.bela.es2017.firebase.db.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klaus on 27/09/17.
 */

public class ReceitaComPasso extends Receita{
    //Atributos que vieram do SAPO
    public String autor;
    public String category;
    public String difficulty;
    public String duration;
    public ArrayList<Passo> passos;
    public ArrayList<String> tags;

    public ReceitaComPasso() {
        super();
    }

    public ReceitaComPasso(String titulo, String subtitulo, String descr,
                           List<InstIngrediente> ingredientesUsados, String imgLink,
                           String autor, String category, String difficulty, String duration,
                           ArrayList<Passo> passos, ArrayList<String> tags) {
        super(titulo, subtitulo, descr, ingredientesUsados, -1, imgLink);
        this.autor = autor;
        this.category = category;
        this.difficulty = difficulty;
        this.duration = duration;
        this.passos = passos;
        this.tags = tags;
    }
}
