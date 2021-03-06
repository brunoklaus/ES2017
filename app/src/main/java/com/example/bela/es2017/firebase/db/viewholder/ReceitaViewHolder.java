package com.example.bela.es2017.firebase.db.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bela.es2017.R;
import com.squareup.picasso.Picasso;

public class ReceitaViewHolder extends RecyclerView.ViewHolder {


    final TextView nome;
    final TextView descricao;
    //final TextView preco;
    final TextView ingredientes;
    final ImageView imagem;
    final CardView cView;

    public TextView getNome() {
        return nome;
    }

    public TextView getDescricao() {
        return descricao;
    }

    public TextView getIngredientes() {
        return ingredientes;
    }
    public CardView getCardView(){return cView;}

    public ImageView getImagem() {
        return imagem;
    }



    public ReceitaViewHolder(View view) {
        super(view);
        nome = (TextView) view.findViewById(R.id.item_receita_nome);
        descricao = (TextView) view.findViewById(R.id.item_receita_descricao);
        ingredientes = (TextView) view.findViewById(R.id.item_receita_ingredientes);
        imagem = (ImageView) view.findViewById(R.id.item_receita_imagem);
        cView = (CardView) view.findViewById(R.id.item_receita_CardView);

        //preco = (TextView) view.findViewById(R.id.item_livro_preco);

    }
}