package com.example.bela.es2017.firebase.db.searchActivity;

import android.support.v7.widget.RecyclerView;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.adapter.FBReceitasAdapter;

public class ListaReceitasActivity extends SearchActivity {

    protected void initAdapter(){
        this.mAdapter = new FBReceitasAdapter(this);
        mAdapter.filter("", mDatabase);
    }
    protected RecyclerView getRecyclerView() {
        return (RecyclerView) findViewById(R.id.recycler1);
    }
    protected void setContentView(){
        setContentView(R.layout.activity_lista);
    }

}
