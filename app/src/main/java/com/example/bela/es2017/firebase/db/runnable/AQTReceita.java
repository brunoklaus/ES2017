package com.example.bela.es2017.firebase.db.runnable;

import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.example.bela.es2017.helpers.StringHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;

/**
 * Classe que representa uma query que pode ser feita em paralelo. Ele possui informacao da
 * referencia de banco de dados utilizado, bem como a string de entrada. As subclasses de queryrunnable
 * essencialmente decide os candidatos iniciais (ex: todas receitas) a possivelmente darem match, e
 * como definir se deu match da entrada com um candidato.
 * Created by klaus on 13/09/17.
 */

public class AQTReceita extends AQTScoreOnce<Receita> {

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
                getIngredientStr(possibleMatch.ingredientesUsados,false).
                toUpperCase();
        String title = possibleMatch.titulo.toUpperCase();
        List<String> rs = new ArrayList<>();
        rs.add(matchWords);
        rs.add(title);
        return rs;
    }
    @Override
    int calculateScore(String inputword, List<String> relStrings){
       int scoreTitle =
               (100 * (FuzzySearch.partialRatio(inputword,relStrings.get(1))))/100;
        int scoreIngr =
                (100 * (FuzzySearch.partialRatio(inputword,relStrings.get(0))))/100;

        return (int)(0.8 * scoreTitle + 0.2*scoreIngr);
    }


}
