package com.example.bela.es2017.firebase.db.runnable;

import android.util.Log;

import com.example.bela.es2017.firebase.db.adapter.FBAdapter;
import com.example.bela.es2017.firebase.db.model.Receita;
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

public class AdvancedQueryRunnable extends QueryRunnable<Receita> {

    public AdvancedQueryRunnable(FBAdapter adapter, DatabaseReference mDatabase, String str, Class myClass) {
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

    class Result {
        public Receita r;
        public int score = 0;
        public Result(Receita r, int score){
            this.r = r;
            this.score = score;
        }
    }



    @Override
    public void run() {

        final AdvancedQueryRunnable q = this;
        final String match = queryInput.trim().toUpperCase();

        this.startingCandidates(match)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String[] inputWords = match.split("([ ])+");

                        List<Receita> newModel = new ArrayList<Receita>();

                        List<Result> results = new ArrayList<Result>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Receita possibleMatch = (Receita) snapshot.getValue(myClass);

                            String matchWords = StringHelper.
                                    getIngredientStr(possibleMatch.ingredientesUsados,999999).
                                    toUpperCase();
                            String title = possibleMatch.titulo.toUpperCase();
                            int score = 0;
                            for (int i = 0; i < inputWords.length; i++) {

                                if (mustBeTerminated) {
                                    adapter.onQueryFinished(newModel, q, false);
                                    Log.d("d", "terminated current");
                                    return;
                                }

                                // Create a Pattern object
                                Pattern r = Pattern.compile(inputWords[i]);
                                // Now create matcher object.
                                Matcher mIngr = r.matcher(matchWords);
                                Matcher mTitle = r.matcher(title);

                                boolean foundIngr = mIngr.find();
                                boolean foundTitle = mTitle.find();

                                if (!foundIngr && !foundTitle && inputWords[i].length() > 3) {
                                    score = 0;
                                    break;
                                }
                                if (foundIngr) {
                                    score++;
                                }
                                if (foundTitle) {
                                    score += 100;
                                }
                            }

                            if (score > 0 ) {
                                results.add(new Result(possibleMatch,score));
                            }
                        }

                        if (mustBeTerminated) {
                            Log.d("d", "terminated current");
                            adapter.onQueryFinished(newModel, q,false);
                            return;
                        }

                        Comparator comp = new Comparator<Result>() {
                            @Override
                            public int compare(Result result, Result t1) {
                                return -Integer.valueOf(result.score).
                                        compareTo(Integer.valueOf(t1.score));
                            }
                        };

                        Collections.sort(results,comp);
                        for (int i = 0; i < results.size(); i++) {
                            newModel.add(results.get(i).r);
                        }
                        adapter.onQueryFinished(newModel, q,true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

    }
}
