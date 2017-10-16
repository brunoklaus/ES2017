package com.example.bela.es2017.Estoque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bela.es2017.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

//
public class AddIngredientes extends AppCompatActivity {

    private Ingrediente ing1, ing2, ing3;
    private RecyclerView recyclerView;
    private Button confirma;
    private AdapterIng adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientes);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_addestoque);
        confirma = (Button) findViewById(R.id.button_confirma);

        //add item a lista
        ing1 = new Ingrediente("carne", FALSE);
        ing2 = new Ingrediente("frango", FALSE);
        ing3 = new Ingrediente("carne de porco", FALSE);
        List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
        ingredientes.add(ing1);
        ingredientes.add(ing2);
        ingredientes.add(ing3);

        adapter = new AdapterIng(ingredientes, this);
        recyclerView.setAdapter(adapter);

        //gridlayout
        RecyclerView.LayoutManager layout = new GridLayoutManager(this, 2);

        //linearlayout
        //LayoutManager layout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(layout);


        //botao mostra os ingredientes selecionados
        confirma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String data = "";
                List<Ingrediente> stList = ((AdapterIng) adapter)
                        .getListaIngrediente();

                for (int i = 0; i < stList.size(); i++) {
                    Ingrediente ing = stList.get(i);
                    if (ing.getSelecionado() == true) {
                        data = data + "\n" + ing.getNomeIngrediente().toString();
                    }

                }

                Toast.makeText(AddIngredientes.this,
                        "Ingredientes selecionados: \n" + data, Toast.LENGTH_LONG)
                        .show();



                Intent it = new Intent(AddIngredientes.this, Estoque.class);
                startActivity(it);
            }
        });
    }
}
