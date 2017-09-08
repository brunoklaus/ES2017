package com.example.bela.es2017;

public class Livros {

    private final String nomeLivro;
    private final String nomeAutor;
    private final String descricao;
    private final Double preco;
    private final int im;

    public Livros(String nomeLivro, String nomeAutor,
                  String descricao, Double preco, int im) {

        this.nomeLivro = nomeLivro;
        this.nomeAutor = nomeAutor;
        this.descricao = descricao;
        this.preco = preco;
        this.im = im;
    }

    public String getNomeLivro() {
        return nomeLivro;
    }
    public String getNomeAutor() {
        return nomeAutor;
    }
    public String getDescricao() {
        return descricao;
    }
    public Double getPreco() {
        return preco;
    }
    public int getIm() { return  im; }
}
