package com.example.bela.es2017.firebase.db.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.runnable.AQTEstoque;
import com.example.bela.es2017.firebase.db.viewholder.InstIngredienteViewHolder;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.google.firebase.database.DatabaseReference;

/**
 * Ver documentacao em FBAdapter.
 */
public class FBEstoqueAdapter extends FBAdapter<InstIngrediente> {



    final SearchView t;
    final TextView s;
    public FBEstoqueAdapter(Context context)
    {
        super(context);
        t =  ((Activity)context).findViewById(R.id.barras_entrada_search);
        s =  ((Activity)context).findViewById(R.id.barras_entrada_unidade);
    }


    @Override
    QueryRunnable createQueryRunnable(String str, DatabaseReference mDatabase) {
        //nosso queryrunnable aqui eh um ReceitaTituloRunnable
        return new AQTEstoque(this, mDatabase, str, InstIngrediente.class);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_instingrediente, parent, false);
        InstIngredienteViewHolder holder = new InstIngredienteViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        final InstIngredienteViewHolder holder = (InstIngredienteViewHolder) viewHolder;

        final InstIngrediente ingr = this.getModel().get(position);
        holder.getNome().setText(ingr.nome.trim());
        holder.getQtde().setText(Double.toString(ingr.qtde).trim());
        holder.getIMedida().setText(ingr.unidade.trim());

        holder.getNome().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.setQuery(holder.getNome().getText(),true);
                s.setText(holder.getIMedida().getText().toString());

            }
        });

    }


}