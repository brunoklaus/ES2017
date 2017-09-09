package com.example.bela.es2017;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bela.es2017.model.InstIngrediente;
import com.example.bela.es2017.model.Receita;

import java.util.List;

public class Adapter extends RecyclerView.Adapter {

    private List<Receita> receitas;
    private Context context;

    public Adapter(List<Receita> receitas, Context context) {
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

        Receita receita  = receitas.get(position) ;

        holder.nome.setText(receita.titulo);
        holder.descricao.setText(receita.subtitulo);
        holder.ingredientes.setText(getIngredientStr(receita.ingredientesUsados));
        holder.imagem.setImageResource(receita.img);
    }

    String getIngredientStr(List<InstIngrediente> ingredientesUsados){
        String str = "";
        for (int i = 0; i < ingredientesUsados.size(); i++) {
            str += ingredientesUsados.get(i).nome;
            str += (i != ingredientesUsados.size() - 1) ? ", " : "";
        }

        return str;
    }

    @Override
    public int getItemCount() {
        return receitas.size();
    }
}