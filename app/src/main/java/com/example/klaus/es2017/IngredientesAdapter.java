package com.example.klaus.es2017;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by brunogata on 09/09/17.
 */

public class IngredientesAdapter extends BaseAdapter{

    private final List<Ingrediente> meus_ingredientes;
    private final Activity activity;

    public IngredientesAdapter(List<Ingrediente> meus_ingredientes, Activity activity){
        this.meus_ingredientes = meus_ingredientes;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.meus_ingredientes.size();
    }

    @Override
    public Object getItem(int i) {
        return this.meus_ingredientes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.meus_ingredientes.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View linha = convertView;
        Ingrediente ingrediente = meus_ingredientes.get(position);

        if(linha == null){ // primeira chamada
            linha = this.activity.getLayoutInflater().inflate(R.layout.celula_ingrediente, parent, false);
        }

        TextView nome = (TextView) linha.findViewById(R.id.ingrediente_textview);
        TextView quantidade = (TextView) linha.findViewById(R.id.quantidade_textview);
        TextView marca = (TextView) linha.findViewById(R.id.marca_textview);

        nome.setText(ingrediente.getNome());
        quantidade.setText(ingrediente.getQuantidade());
        marca.setText(ingrediente.getMarca());

        return null;
    }
}
