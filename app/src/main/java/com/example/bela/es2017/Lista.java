package com.example.bela.es2017;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Lista extends AppCompatActivity {

    private RecyclerView rView;
    private Livros livro1, livro2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        rView = (RecyclerView) findViewById(R.id.recycler1);

        livro1 = new Livros("Crimes do ABC", " - Agatha Christie", "Suspense Policial", (double) 13, R.drawable.crimes_do_abc);
        livro2 = new Livros("Um estudo em vermelho", " - Arthur Conan Doyle", "Suspense Policial", (double) 14, R.drawable.estudo_vermelho_sherlock);

        List<Livros> livros = new ArrayList<Livros>();
        livros.add(livro1);
        livros.add(livro2);

        rView.setAdapter(new Adapter(livros, this));

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rView.setLayoutManager(layout);
    }
}
