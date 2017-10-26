package com.example.bela.es2017.firebase.db.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.runnable.AQTReceita;
import com.example.bela.es2017.firebase.db.viewholder.ReceitaViewHolder;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.helpers.StringHelper;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

/**
 * Ver documentacao em FBAdapter.
 */
public class FBReceitasAdapter extends FBAdapter<Receita> {


    public FBReceitasAdapter(Context context) {
        super(context);
    }

    @Override
    QueryRunnable createQueryRunnable(String str, DatabaseReference mDatabase) {
        //nosso queryrunnable aqui eh um ReceitaTituloRunnable
        return new AQTReceita(this, mDatabase, str, Receita.class);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_receita, parent, false);
        ReceitaViewHolder holder = new ReceitaViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ReceitaViewHolder holder = (ReceitaViewHolder) viewHolder;

        Receita receita = this.getModel().get(position);
        int MAX_LEN = 27;
        holder.getNome().setText(receita.titulo.trim());
        holder.getDescricao().setText(receita.subtitulo.trim());
        holder.getIngredientes().setText(StringHelper.getIngredientStr(receita.ingredientesUsados).trim());
        if (receita.img != -1) {
            holder.getImagem().setImageResource(receita.img);
        } else if (receita.imgLink != null && !receita.imgLink.isEmpty()){
            Picasso.with(this.context).load(receita.imgLink).into(holder.getImagem());
        }
    }


}