package com.example.bela.es2017.firebase.searcher;

import java.util.List;

/**
 * Created by klaus on 15/10/17.
 */

public interface RecebeSeleciona<T> {
    abstract void onSelectBtnSearchFinished(String query, List<T> res);
}
