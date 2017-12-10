package com.example.bela.es2017.firebase.db.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.runnable.AQTReceita;
import com.example.bela.es2017.firebase.db.viewholder.ReceitaViewHolder;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.example.bela.es2017.helpers.FBInsereReceitas;
import com.example.bela.es2017.helpers.StringHelper;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import visualizareceita.VisualizaReceitaActivity;

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
        } else {
            //Desabilitar imagem
            holder.getImagem().setVisibility(View.GONE);
        }
        //Adicionar listener para quando clicar na receita
        if (receita.passos != null && !receita.passos.isEmpty()) {
            //Se tem passos, eh a nova versao da classe receita e podemos abrir a visualizacao
            holder.getCardView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    adicionaReceitaALista(receita);

                    Intent it = VisualizaReceitaActivity.getIntentTo(context, receita);
                    context.startActivity(it);
                }
            });
        } else {
            //Sempre adicionamos ela na "receitasfeitas"
            holder.getCardView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adicionaReceitaALista(receita);
                }
            });

        }


    }

    private void adicionaReceitaALista(Receita r){


        final Receita rFin = r;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        FBInsereReceitas.leReceitasFeitas(user, ref, new Searcher<ArrayList<String>>() {
            @Override
            public void onSearchFinished(String query, List<ArrayList<String>> results, QueryRunnable<ArrayList<String>> q, boolean update) {
                if (results == null || results.isEmpty()) {
                    throw new IllegalArgumentException("Busca de receitasfeitas retornou" +
                            " formato errado");
                }
                ArrayList<String> l = results.get(0);
                l.add(rFin.id);
                FBInsereReceitas.atualizaReceitasFeitas(user,ref,l);
            }
        });


    }
}