package com.example.bela.es2017.firebase.db.runnable;

import android.util.Log;

import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.result.FBResult;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.example.bela.es2017.helpers.StringHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe que representa uma query que pode ser feita em paralelo. Ele possui informacao da
 * referencia de banco de dados utilizado, bem como a string de entrada. As subclasses de queryrunnable
 * essencialmente decide os candidatos iniciais (ex: todas receitas) a possivelmente darem match, e
 * como definir se deu match da entrada com um candidato.
 * Created by klaus on 13/09/17.
 */

public class AQTReceita extends AQT<Receita> {

    public AQTReceita(Searcher adapter, DatabaseReference mDatabase, String str, Class myClass) {
       super(adapter,mDatabase,str,myClass);
    }
    @Override
    Query startingCandidates(String input){
        return  mDatabase.child("receitas");
    }
    @Override
    boolean isMatch(String input, DataSnapshot snap) {
        String snapStr =snap.child("titulo").getValue(String.class);
        return snapStr.toUpperCase().startsWith(input);
    }

    @Override
    List<String> getRelevantStrings(Receita possibleMatch){
        String matchWords = StringHelper.
                getIngredientStr(possibleMatch.ingredientesUsados).
                toUpperCase();
        String title = possibleMatch.titulo.toUpperCase();
        List<String> rs = new ArrayList<>();
        rs.add(matchWords);
        rs.add(title);
        return rs;
    }
    @Override
    int calculateScore(String inputword, List<String> relStrings){
        // Create a Pattern object
        Pattern r = Pattern.compile(inputword);
        // Now create matcher object.
        Matcher mIngr = r.matcher(relStrings.get(0));
        Matcher mTitle = r.matcher(relStrings.get(1));
        int score = 0;
        boolean foundIngr = mIngr.find();
        boolean foundTitle = mTitle.find();

        if (foundIngr) {
            score += (mIngr.start() == 0) ? 2 : 1;
        }
        if (foundTitle) {
            score += (mTitle.start() == 0) ? 200 : 100;
        }
        return score;
    }


}
