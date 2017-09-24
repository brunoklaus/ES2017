package com.example.bela.es2017.firebase.db.searchActivity;

import com.example.bela.es2017.firebase.db.adapter.FBReceitasAdapter;

public class ListaReceitasActivity extends SearchActivity {

    void initAdapter(){
        this.mAdapter = new FBReceitasAdapter(this);
        mAdapter.filter("", mDatabase);
    }
}
