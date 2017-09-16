package com.example.bela.es2017.firebase.db.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by klaus on 09/09/17.
 */
@IgnoreExtraProperties
public class InstIngrediente {

    public String nome;
    public double qtde;
    String unidade;


    public InstIngrediente() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public InstIngrediente(String nome, double qtde, String unidade){
        this.nome = nome.toLowerCase();
        this.qtde = qtde;
        this.unidade = unidade;
    }


}
