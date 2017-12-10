package com.example.bela.es2017.receitaspossiveis;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.bela.es2017.R;
import com.example.bela.es2017.SideBarActivity;
import com.example.bela.es2017.SideBarInfo;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.runnable.AQTEstoqueDisp;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klaus on 08/11/17.
 */

public class ReceitasPossiveisActivity extends SideBarActivity{

    private RecyclerView rView;
    Context context;

    @Override
    protected SideBarInfo getInfo() {
        return new SideBarInfo("EasyFeed - Receitas pelo Estoque", R.layout.activity_procura_receita_estoque);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rView = (RecyclerView) findViewById(R.id.possivel_rView);
        context = this;

        new AQTEstoqueDisp(new Searcher<Receita>() {
            @Override
            public void onSearchFinished(String query, List<Receita> results, QueryRunnable<Receita> q, boolean update) {
                if (!results.isEmpty()) {
                    Toast.makeText(context, results.get(0).titulo, Toast.LENGTH_LONG).show();
                    RecyclerView.LayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    rView.setLayoutManager(layout);
                    rView.setAdapter(new RAdapter(context,new ArrayList<>(results)));
                }
            }
        }).go();

    }






}
