package com.example.bela.es2017;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

public class Ingredientes extends AppCompatActivity {


    private  String nomeIngrediente;
    private  String quantidade;
    //private final Spinner unidade;
    private  int imagem;



    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public String getQuantidade() {
        return quantidade;
    }

    //public Spinner getUnidade() {
    //    return unidade;
    //}

    public int getImagem() {
        return imagem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
