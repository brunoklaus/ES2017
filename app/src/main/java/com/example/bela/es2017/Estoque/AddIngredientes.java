package com.example.bela.es2017.Estoque;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bela.es2017.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

//
public class AddIngredientes extends AppCompatActivity {

    private Ingrediente ing1, ing2, ing3, ing4;
    private RecyclerView recyclerView;
    private Button confirma;
    private AdapterIng adapter;
    public List<Ingrediente> stList;
    public ArrayList<String> listaAux;
    public Estoque estoque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientes);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_addestoque);
        confirma = (Button) findViewById(R.id.button_confirma);
        listaAux = new ArrayList<String>();

        //add item a lista
        ing1 = new Ingrediente("Carne", FALSE);
        ing2 = new Ingrediente("Frango", FALSE);
        ing3 = new Ingrediente("Carne de porco", FALSE);
        ing4 = new Ingrediente("Carne moída", FALSE);
        List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
        ingredientes.add(ing1);
        ingredientes.add(ing2);
        ingredientes.add(ing3);
        ingredientes.add(ing4);

        adapter = new AdapterIng(ingredientes, this);
        recyclerView.setAdapter(adapter);

        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        //gridlayout
        RecyclerView.LayoutManager layout = new GridLayoutManager(this, 2);

        //linearlayout
        //LayoutManager layout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(layout);


        //botao mostra e passa para o Estoque os ingredientes selecionados
        confirma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String data = "";
                stList = ((AdapterIng) adapter).getListaIngrediente();
                Intent ingSelecionados = new Intent();

                for (int i = 0; i < stList.size(); i++) {
                    Ingrediente ing = stList.get(i); //cada ingrediente

                    if (ing.getSelecionado() == true) {
                        data = data + "\n" + ing.getNomeIngrediente().toString();
                        listaAux.add(ing.getNomeIngrediente());
                    }
                }

                Toast.makeText(AddIngredientes.this,
                        "Ingredientes selecionados: " + data, Toast.LENGTH_SHORT)
                        .show();

                ingSelecionados.putStringArrayListExtra("nome_ingrediente", listaAux);
                setResult(2, ingSelecionados);

                finish();
            }
        });
    }
}
