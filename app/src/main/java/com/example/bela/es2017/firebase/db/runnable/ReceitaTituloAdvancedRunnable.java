package com.example.bela.es2017.firebase.db.runnable;

import com.example.bela.es2017.firebase.db.adapter.FBAdapter;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by klaus on 23/09/17.
 */

public class ReceitaTituloAdvancedRunnable  extends QueryRunnable<Receita> {
    public ReceitaTituloAdvancedRunnable(FBAdapter adapter, DatabaseReference mDatabase, String str, Class myClass) {
        super(adapter, mDatabase, str, myClass);
    }
    @Override
    Query startingCandidates(String input){
        return  mDatabase.child("receitas").orderByChild("titulo").startAt(input).limitToFirst(50);
    }
    @Override
    boolean isMatch(String input, DataSnapshot snap) {
        int score = 0;

        String[] words = input.trim().split("[, ]");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].toUpperCase();
        }

        String snapStr =snap.child("titulo").getValue(String.class).toUpperCase();




        return snapStr.toUpperCase().startsWith(input);
    }


}
