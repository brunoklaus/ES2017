package com.example.bela.es2017.firebase.db.runnable;

import android.util.Log;

import com.example.bela.es2017.firebase.db.adapter.FBAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma query que pode ser feita em paralelo. Ele possui informacao da
 * referencia de banco de dados utilizado, bem como a string de entrada. As subclasses de queryrunnable
 * essencialmente decide os candidatos iniciais (ex: todas receitas) a possivelmente darem match, e
 * como definir se deu match da entrada com um candidato.
 * Created by klaus on 13/09/17.
 */

public abstract class QueryRunnable<T> implements Runnable {
    public boolean mustBeTerminated = false;
    String queryInput;  //Entrada da query
    FBAdapter adapter;  //Adapter que criou essa classe, faremos callback nele
    DatabaseReference mDatabase;//referencia do banco de dados
    Class myClass;

    public QueryRunnable(FBAdapter adapter, DatabaseReference mDatabase, String str, Class myClass) {
        this.adapter = adapter;
        this.mDatabase = mDatabase;
        this.queryInput = str;
        this.myClass = myClass;
    }

    public String getString() {
        return queryInput;
    }

    /**
     * Define candidatos iniciais a serem considerados
     *
     * @param input a string de entrada da query
     * @return
     */
    abstract Query startingCandidates(String input);

    /**
     * Define se deu match da entrada com o candidato (representado por um DataSnapshot)
     *
     * @param input string de entrada da query
     * @param snap  o candidato a match
     * @return
     */
    abstract boolean isMatch(String input, DataSnapshot snap);

    @Override
    public void run() {

        final QueryRunnable q = this;
        final String match = queryInput.toUpperCase();

        this.startingCandidates(match)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<T> newModel = new ArrayList<T>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            T possibleMatch = (T) snapshot.getValue(myClass);
                            if (isMatch(match, snapshot)) {
                                newModel.add(possibleMatch);
                            } else {
                                break;
                            }
                            if (mustBeTerminated) {
                                adapter.onQueryFinished(newModel, q,false);
                                Log.d("d", "terminated current");
                                return;
                            }
                        }
                        if (mustBeTerminated) {
                            Log.d("d", "terminated current");
                            adapter.onQueryFinished(newModel, q,false);
                            return;
                        }
                        adapter.onQueryFinished(newModel, q,true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

    }
}
