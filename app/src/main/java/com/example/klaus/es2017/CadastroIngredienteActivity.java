package com.example.klaus.es2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CadastroIngredienteActivity extends AppCompatActivity {

    CadastroIngredientesHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ingrediente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText ingrediente_text = (EditText) findViewById(R.id.ingredienteText);
        EditText quantidade_text = (EditText) findViewById(R.id.quantidadeText);
        EditText marca_text = (EditText) findViewById(R.id.marcaText);

        helper = new CadastroIngredientesHelper(this);

        FloatingActionButton fab_forms = (FloatingActionButton) findViewById(R.id.fab_forms);
        fab_forms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingrediente ingrediente = helper.addIngrediente();
                Log.i("Meu log", ingrediente.getNome());
                IngredientesDAO dao = new IngredientesDAO(CadastroIngredienteActivity.this);

                if(ingrediente.getId() == null){
                    dao.addIngrediente(ingrediente);
                }
                else{
                    dao.editIngrediente(ingrediente);
                }
                dao.close();
                finish();
            }
        });

        Ingrediente meuIngrediente = new Ingrediente();

        meuIngrediente.setNome("Arroz");
        meuIngrediente.setQuantidade("250g");
        meuIngrediente.setMarca("Tio João");
        meuIngrediente.setId(new Long(10));
        CadastroIngredientesHelper helper = new CadastroIngredientesHelper(this);
        helper.pegaIngrediente(meuIngrediente);

    }

}
