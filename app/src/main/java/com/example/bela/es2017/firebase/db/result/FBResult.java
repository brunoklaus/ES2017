package com.example.bela.es2017.firebase.db.result;

import com.example.bela.es2017.firebase.db.model.Receita;

/**
 * Created by klaus on 14/10/17.
 */

public class FBResult<T> {

    public T r;
    public int score = 0;
    public FBResult(T r, int score){
        this.r = r;
        this.score = score;
    }
}
