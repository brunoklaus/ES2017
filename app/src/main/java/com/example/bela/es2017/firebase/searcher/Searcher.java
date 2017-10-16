package com.example.bela.es2017.firebase.searcher;

import com.example.bela.es2017.firebase.db.result.FBResult;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;

import java.util.List;

/**
 * Created by klaus on 14/10/17.
 */

public  interface Searcher<T> {
    public void onSearchFinished(String query, List<T> results, QueryRunnable<T> q, boolean update);
}
