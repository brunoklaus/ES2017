package com.example.bela.es2017.Estoque.Add_Ing_Estoque;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bela.es2017.R;

public class ViewHolder_addIng extends RecyclerView.ViewHolder{

    final TextView info;
    final CheckBox check;

    public ViewHolder_addIng(View view) {
        super(view);

        info = (TextView) view.findViewById(R.id.editText_ingrediente_add);
        check = (CheckBox) view.findViewById(R.id.checkBox);
    }
}
