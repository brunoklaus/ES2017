package com.example.bela.es2017;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class Adicionar_receita_Adapter extends BaseAdapter {

    public List<Ingredientes> ingredientes = new ArrayList<Ingredientes>();

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (ingredientes != null && ingredientes.size() != 0) {
            return ingredientes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return ingredientes.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder1 holder;
        if (view == null) {

            holder = new ViewHolder1();
            //LayoutInflater inflater = Main.this.getLayoutInflater();
            //convertView = inflater.inflate(R.layout.item_adicionar_receita_ingrediente, null);
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_adicionar_receita_ingrediente, parent, false);

            holder.editText1 = (EditText) view.findViewById(R.id.ItemCaption);

            view.setTag(holder);

        } else {

            holder = (ViewHolder1) view.getTag();
        }

        holder.ref = position;

        return view;
    }

    private class ViewHolder1 {
        EditText editText1;
        int ref;
    }
}
