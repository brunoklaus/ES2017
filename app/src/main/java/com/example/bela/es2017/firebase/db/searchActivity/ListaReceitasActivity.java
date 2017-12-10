package com.example.bela.es2017.firebase.db.searchActivity;

import android.support.v7.widget.RecyclerView;

import com.example.bela.es2017.R;
import com.example.bela.es2017.SideBarInfo;
import com.example.bela.es2017.firebase.db.adapter.FBReceitasAdapter;

public class ListaReceitasActivity extends SearchActivity {

    protected void initAdapter(){
        this.mAdapter = new FBReceitasAdapter(this);
        mAdapter.filter("", mDatabase);
    }
    protected RecyclerView getRecyclerView() {
        return (RecyclerView) findViewById(R.id.recycler1);
    }

    @Override
    protected SideBarInfo getInfo(){
        return new SideBarInfo("EasyFeed - Busca por Receita",R.layout.activity_lista);
    }


}
