package com.example.bela.es2017.receitaspossiveis;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.viewholder.ReceitaViewHolder;
import com.example.bela.es2017.helpers.StringHelper;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import visualizareceita.VisualizaReceitaActivity;

/**
 * Created by klaus on 08/11/17.
 */

public class RAdapter extends  RecyclerView.Adapter {

    Context context;
    List<Receita> model;


    public RAdapter(Context c, List<Receita> model) {
        context = c;
        this.model = model;
    }

    public List<Receita> getModel(){
        return model;
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

        final Receita receita = this.getModel().get(position);
        int MAX_LEN = 27;
        holder.getNome().setText(receita.titulo.trim());
        holder.getDescricao().setText(receita.subtitulo.trim());
        holder.getIngredientes().setText(StringHelper.getIngredientStr(receita.ingredientesUsados,true).trim());
        if (receita.imgLink != null && !receita.imgLink.isEmpty()){
            Picasso.with(this.context).load(receita.imgLink).into(holder.getImagem());
        } else if (receita.imgStorage != null){
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(receita.imgStorage);
            // Load the image using Glide
            Glide.with(context.getApplicationContext())
                    .using(new FirebaseImageLoader())
                    .load(ref)
                    .into(holder.getImagem());
        }
        //Se receita possuir passos, adicionar onclicklistener para atividade que acompanha passos
        if (receita.passos != null && !receita.passos.isEmpty()) {
            holder.getCardView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = VisualizaReceitaActivity.getIntentTo(context, receita);
                    context.startActivity(it);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return getModel().size();
    }
}
