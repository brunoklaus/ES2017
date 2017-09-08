package com.example.bela.es2017;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Adapter extends RecyclerView.Adapter {

    private List<Receitas> receitas;
    private Context context;

    public Adapter(List<Receitas> receitas, Context context) {
        this.receitas = receitas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_receita, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        Receitas receita  = receitas.get(position) ;

        holder.nome.setText(receita.getNomeReceita());
        holder.descricao.setText(receita.getDescricao());
        holder.ingredientes.setText(receita.getIngredientes());
        holder.imagem.setImageResource(receita.getImagem());
    }

    @Override
    public int getItemCount() {
        return receitas.size();
    }
}