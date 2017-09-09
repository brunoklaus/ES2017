package com.example.klaus.es2017;

import java.io.Serializable;

/**
 * Created by brunogata on 09/09/17.
 */

public class Ingrediente implements Serializable{
    private String nome;
    private String quantidade;
    private String marca;

    private Long id;

    @Override
    public String toString(){
        return "(" + id + ")" + nome;
    }

    public String getNome(){
        return nome;
    }

    public String getQuantidade(){
        return quantidade;
    }

    public String getMarca(){
        return marca;
    }

    public Long getId(){
        return id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setQuantidade(String quantidade){
        this.quantidade = quantidade;
    }

    public void setMarca(String marca){
        this.marca = marca;
    }

    public void setId(Long id){
        this.id = id;
    }
}
