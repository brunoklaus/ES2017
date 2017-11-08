package com.example.bela.es2017.Estoque;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bela.es2017.Estoque.Estoque;
import com.example.bela.es2017.Estoque.Ingrediente;
import com.example.bela.es2017.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class AdapterEstoque extends RecyclerView.Adapter {

    private List<Ingrediente> ingredientes;
    private Context context;

    public AdapterEstoque(List<Ingrediente> ing, Context context) {
        ingredientes = ing;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_estoque, parent, false);
        ViewHolder_addEstoque holder = new ViewHolder_addEstoque(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder_addEstoque holder = (ViewHolder_addEstoque) viewHolder;
        final Ingrediente ingrediente  = ingredientes.get(position) ;

        holder.info.setText(ingrediente.getNomeIngrediente());
       // holder.edita.setOnClickListener(view -> editaItem(position));
        final int pos = position;
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    deletaItem(pos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //private void editaItem(int position) {
    //}

    //apaga o item
    private void deletaItem(int position) throws FileNotFoundException {

        Toast.makeText(context.getApplicationContext(),"Item excluído.",Toast.LENGTH_SHORT).show();

        ingredientes.remove(position);
        //new Estoque().updateArquivo();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, ingredientes.size());
    }

    @Override
    public int getItemCount() {
        return ingredientes.size();
    }
}

class ViewHolder_addEstoque extends RecyclerView.ViewHolder{

    final TextView info;
    final ImageButton delete;
    final ImageButton edita;

    public ViewHolder_addEstoque(View view) {
        super(view);

        info = (TextView) view.findViewById(R.id.editText_ingrediente_estoque);
        delete = (ImageButton) view.findViewById(R.id.imageButton_deletaIng_estoque);
        edita = (ImageButton) view.findViewById(R.id.imageButton_editaIng_estoque);
    }
}