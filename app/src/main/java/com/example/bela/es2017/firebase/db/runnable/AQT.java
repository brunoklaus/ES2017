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


    /**
     * Classe que representa uma query que pode ser feita em paralelo. Ele possui informacao da
     * referencia de banco de dados utilizado, bem como a string de entrada. As subclasses de queryrunnable
     * essencialmente decide os candidatos iniciais (ex: todas receitas) a possivelmente darem match, e
     * como definir se deu match da entrada com um candidato.
     * Created by klaus on 13/09/17.
     */
    public class AQT<T> extends QueryRunnable<T> {

        public AQT(Searcher adapter, DatabaseReference mDatabase, String str, Class myClass) {
            super(adapter,mDatabase,str,myClass);
        }
        @Override
        Query startingCandidates(String input){
            throw new IllegalStateException("Must implement startingCandidates");
        }
        @Override
        boolean isMatch(String input, DataSnapshot snap) {
            throw new IllegalStateException("Must implement isMatch");
        }
        List<String> getRelevantStrings(T possibleMatch){
            throw new IllegalStateException("Must implement getRelevantStrings");
        }

        int calculateScore(String inputword, List<String> relStrings){
            throw new IllegalStateException("Must implement calculateScore");
        }

        @Override
        public void run() {

            final QueryRunnable<T> q = this;
            final String match = queryInput.trim().toUpperCase();

            this.startingCandidates(match)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String[] inputWords = match.split("([ ])+");

                            List<T> newModel = new ArrayList<T>();

                            List<FBResult<T>> results = new ArrayList<FBResult<T>>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                T possibleMatch = (T) snapshot.getValue(myClass);

                                List<String> relStrings = getRelevantStrings(possibleMatch);
                                int score = 0;
                                for (int i = 0; i < inputWords.length; i++) {

                                    if (mustBeTerminated) {
                                        searcher.onSearchFinished(queryInput,newModel, q, false);
                                        Log.d("d", "terminated current");
                                        return;
                                    }
                                    int ns = calculateScore(inputWords[i], relStrings);
                                    if (ns == 0 && inputWords[i].length() >3) break;
                                    score += ns;
                                }

                                if (score > 0 ) {
                                    results.add(new FBResult<T>(possibleMatch,score));
                                }
                            }

                            if (mustBeTerminated) {
                                Log.d("d", "terminated current");
                                searcher.onSearchFinished(queryInput,newModel, q,false);
                                return;
                            }

                            Comparator comp = new Comparator<FBResult<T>>() {
                                @Override
                                public int compare(FBResult<T> result, FBResult<T> t1) {
                                    return -Integer.valueOf(result.score).
                                            compareTo(Integer.valueOf(t1.score));
                                }
                            };

                            Collections.sort(results,comp);
                            for (int i = 0; i < results.size(); i++) {
                                newModel.add(results.get(i).r);
                            }
                            searcher.onSearchFinished(queryInput,newModel, q,true);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }

                    });


    }

}
