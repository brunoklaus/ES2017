package com.example.bela.es2017;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Lista extends AppCompatActivity {

    private RecyclerView rView;
    private Receitas receita1, receita2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        rView = (RecyclerView) findViewById(R.id.recycler1);

        receita1= new Receitas("Hamburguer de carne", "Como fazer um delicioso hamburguer", "Carne, ovo, farinha de trigo", R.drawable.hamburguer);
        receita2 = new Receitas("Molho bolonhesa", "Para a melhor macarronada", "Carne, extrato de tomate, pimenta", R.drawable.molho_bolonhesa);

        List<Receitas> receitas = new ArrayList<Receitas>();
        receitas.add(receita1);
        receitas.add(receita2);

        rView.setAdapter(new Adapter(receitas, this));

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rView.setLayoutManager(layout);
    }
}
