package com.example.bela.es2017.Estoque;

import android.text.BoringLayout;

public class Ingrediente {

    private final String nomeIngrediente;
    private Boolean selecionado;
    //private final String quantidade;
    //private final String unidade;
    //private final Double preco;

    public Ingrediente(String nomeIngrediente, Boolean s) {

        this.nomeIngrediente = nomeIngrediente;
        selecionado = s;
        //this.quantidade = quantidade;
        //this.unidade = unidade;
    }

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public Boolean getSelecionado() { return selecionado; }

    public void setSelecionado(Boolean selecionado) { this.selecionado = selecionado; }
}
