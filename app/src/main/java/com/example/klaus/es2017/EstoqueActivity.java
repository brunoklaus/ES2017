package com.example.klaus.es2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class EstoqueActivity extends AppCompatActivity {

    ListView minhaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EstoqueActivity.this, CadastroIngredienteActivity.class);
                startActivity(intent);
            }
        });

        minhaLista = (ListView) findViewById(R.id.estoque_list);
    }

    @Override
    protected void onResume(){
        carregaLista();
        super.onResume();
    }

    private void carregaLista(){

        IngredientesDAO dao = new IngredientesDAO(this);
        List<Ingrediente> ingredientes = dao.getLista();
        dao.close();
        IngredientesAdapter adaptador = new IngredientesAdapter(ingredientes, this);

        this.minhaLista.setAdapter(adaptador);
    }

}
