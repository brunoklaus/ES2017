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



public class AQTScoreOnce<T> extends AQT<T> {

    public AQTScoreOnce(Searcher adapter, DatabaseReference mDatabase, String str, Class myClass) {
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



                        List<T> newModel = new ArrayList<T>();

                        List<FBResult<T>> results = new ArrayList<FBResult<T>>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            T possibleMatch = (T) snapshot.getValue(myClass);

                            List<String> relStrings = getRelevantStrings(possibleMatch);
                            int score =  calculateScore(match, relStrings);

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
