package com.example.bela.es2017.firebase.db.model;

/**
 * Created by klaus on 27/09/17.
 */

public class Passo {
    public String descr;    //descricao (eventualmente pode ser em HTML)
    public int duration;    //duracao associada a um timer, em segundos (-1 se n houver timer)

    public Passo(String descr, int duration) {
        this.descr = descr;
        this.duration = duration;
    }
}
