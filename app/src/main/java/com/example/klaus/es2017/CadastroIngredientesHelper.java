package com.example.klaus.es2017;

import android.widget.EditText;

/**
 * Created by brunogata on 09/09/17.
 */

public class CadastroIngredientesHelper {

    private Ingrediente ingrediente;

    private EditText nome_text;
    private EditText quantidade_text;
    private EditText marca_text;

    public CadastroIngredientesHelper(CadastroIngredienteActivity activity){

        this.ingrediente = new Ingrediente();
        this.nome_text = (EditText) activity.findViewById(R.id.ingredienteText);
        this.quantidade_text = (EditText) activity.findViewById(R.id.quantidadeText);
        this.marca_text = (EditText) activity.findViewById(R.id.marcaText);
    }

    public Ingrediente addIngrediente(){

        ingrediente.setNome(nome_text.getText().toString());
        ingrediente.setQuantidade(quantidade_text.getText().toString());
        ingrediente.setMarca(marca_text.getText().toString());

        return ingrediente;
    }

    public void pegaIngrediente(Ingrediente ingrediente){
        nome_text.setText(ingrediente.getNome());
        quantidade_text.setText(ingrediente.getQuantidade());
        marca_text.setText(ingrediente.getMarca());
        this.ingrediente = ingrediente;
    }
}