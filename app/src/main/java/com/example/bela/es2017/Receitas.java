package com.example.bela.es2017;

public class Receitas {

    private final String nomeReceita;
    private final String descricao;
    private final String ingredientes; //POSTERIORMENTE CRIAR UMA CLASSE INGREDIENTES
    private final int imagem;

    public Receitas(String nomeReceita, String descricao,
                  String ingredientes, int imagem) {

        this.nomeReceita = nomeReceita;
        this.descricao = descricao;
        this.ingredientes = ingredientes;
        this.imagem = imagem;
    }

    public String getNomeReceita() {
        return nomeReceita;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public int getImagem() {
        return imagem;
    }
}
