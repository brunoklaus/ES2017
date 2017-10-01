package com.example.bela.es2017;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

public class Ingredientes extends AppCompatActivity {

    private final String nomeIngrediente;
    private final String quantidade;
    //private final Spinner unidade;
    private final int imagem;

    public Ingredientes(String nomeIngrediente, String quantidade,
                     int imagem) {

        this.nomeIngrediente = nomeIngrediente;
        this.quantidade = quantidade;
       // this.unidade = unidade;
        this.imagem = imagem;
    }

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
        setContentView(R.layout.activity_ingredientes);
    }
}
