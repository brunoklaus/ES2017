package com.example.bela.es2017;

import android.util.Log;

import com.example.bela.es2017.Adapter;
import com.example.bela.es2017.model.Receita;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klaus on 13/09/17.
 */

public class QueryRunnable implements  Runnable {
    String str;
    Adapter adapter;
    DatabaseReference mDatabase;
    public boolean mustBeTerminated = false;
    QueryRunnable(Adapter adapter, DatabaseReference mDatabase, String str) {
        this.adapter = adapter;
        this.mDatabase = mDatabase;
        this.str = str;
    }

    @Override
    public void run() {

        final QueryRunnable q = this;
        final String match = str.toUpperCase();

        mDatabase.child("receitas").orderByChild("titulo").startAt(match).limitToFirst(50)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Receita> receitas = new ArrayList<Receita>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Receita r = (Receita) snapshot.getValue(Receita.class);
                            if (r.titulo.toUpperCase().startsWith(match)) {
                                receitas.add(r);
                            } else {
                                break;
                            }
                            if (mustBeTerminated) {
                                adapter.onQueryFinished(receitas,q);
                                Log.d("d","terminated current");
                                return;
                            }
                        }
                        if (mustBeTerminated){
                            Log.d("d","terminated current");
                            adapter.onQueryFinished(receitas,q);
                            return;
                        }
                        adapter.onQueryFinished(receitas,q);


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

    }
}
