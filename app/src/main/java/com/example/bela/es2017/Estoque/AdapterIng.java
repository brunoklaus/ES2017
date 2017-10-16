package com.example.bela.es2017.Estoque;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;
import com.example.bela.es2017.R;

import java.util.List;
import java.util.Locale;

public class AdapterIng extends RecyclerView.Adapter {

    private List<Ingrediente> ingredientes;
    private Context context;

    public AdapterIng(List<Ingrediente> ing, Context context) {
        ingredientes = ing;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_adding, parent, false);
        ViewHolder_addIng holder = new ViewHolder_addIng(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder_addIng holder = (ViewHolder_addIng) viewHolder;
        final Ingrediente ingrediente  = ingredientes.get(position) ;

        holder.info.setText(ingrediente.getNomeIngrediente());
        holder.check.setChecked(ingrediente.getSelecionado());
        holder.check.setTag(ingrediente);
        holder.check.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Ingrediente ing = (Ingrediente) cb.getTag();

                ing.setSelecionado(cb.isChecked());
                ingrediente.setSelecionado(cb.isChecked());

                /*
                Toast.makeText(
                        v.getContext(),
                        "Você selecionou: " + ingrediente.getNomeIngrediente(), Toast.LENGTH_LONG).show();
                */
            }

        });
    }

    @Override
    public int getItemCount() {
        return ingredientes.size();
    }

    public List<Ingrediente> getListaIngrediente() {
        return ingredientes;
    }
}
