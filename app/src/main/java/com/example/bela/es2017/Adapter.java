package com.example.bela.es2017;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Adapter extends RecyclerView.Adapter {

    private List<Livros> livros;
    private Context context;

    public Adapter(List<Livros> livros, Context context) {
        this.livros = livros;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_livro, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        Livros livro  = livros.get(position) ;

        holder.nome.setText(livro.getNomeLivro());
        holder.descricao.setText(livro.getDescricao());
        holder.autor.setText(livro.getNomeAutor());
        holder.preco.setText(livro.getPreco().toString());
        holder.imagem.setImageResource(livro.getIm());
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }
}