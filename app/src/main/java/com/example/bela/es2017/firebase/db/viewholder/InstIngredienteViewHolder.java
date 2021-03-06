package com.example.bela.es2017.firebase.db.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bela.es2017.R;
import com.squareup.picasso.Picasso;

public class InstIngredienteViewHolder extends RecyclerView.ViewHolder {


    final TextView nome;
    final TextView qtde;
    final TextView medida;
    final ImageView removeBtn;

    public TextView getNome() {
        return nome;
    }

    public TextView getQtde() {
        return qtde;
    }

    public TextView getIMedida() {
        return medida;
    }

    public ImageView getRemoveBtn() {
        return removeBtn;
    }



    public InstIngredienteViewHolder(View view) {
        super(view);
        nome = (TextView) view.findViewById(R.id.item_instingrediente_nome);
        qtde = (TextView) view.findViewById(R.id.item_instingrediente_qtde);
        removeBtn = (ImageView) view.findViewById(R.id.remove_button);
        medida = (TextView) view.findViewById(R.id.item_instingrediente_medida);
    }
}