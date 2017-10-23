package com.example.bela.es2017.firebase.db.model;

/**
 * Created by klaus on 22/10/17.
 */

public enum Dificuldade {
    MUITO_FACIL,FACIL,INTERMEDIARIO,AVANCADO,MUITO_AVANCADO;

    @Override public String toString(){
        switch (this) {
            case MUITO_FACIL: return "Muito Fácil";
            case FACIL: return "Fácil";
            case INTERMEDIARIO: return  "Intermediário";
            case AVANCADO: return "Avançado";
            case MUITO_AVANCADO: return "Muito avançado";
            default: throw new IllegalArgumentException("Nao achou string pra unidade");

        }
    }

}
