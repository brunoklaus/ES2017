package com.example.bela.es2017;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {

    final TextView nome;
    final TextView descricao;
    final TextView preco;
    final TextView autor;
    final ImageView imagem;

    public ViewHolder(View view) {
        super(view);
        nome = (TextView) view.findViewById(R.id.item_livro_nome);
        descricao = (TextView) view.findViewById(R.id.item_livro_descricao);
        preco = (TextView) view.findViewById(R.id.item_livro_preco);
        autor = (TextView) view.findViewById(R.id.item_livro_autor);
        imagem = (ImageView) view.findViewById(R.id.item_livro_imagem);
    }
}