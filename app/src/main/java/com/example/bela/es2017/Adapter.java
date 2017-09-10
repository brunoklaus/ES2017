package com.example.bela.es2017;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bela.es2017.model.InstIngrediente;
import com.example.bela.es2017.model.Receita;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter {

    private List<Receita> receitas;
    private Context context;
    public final int MAX_LEN = 27;

    public Adapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_receita, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        Receita receita  = receitas.get(position) ;

        holder.nome.setText(adjustStr(receita.titulo));
        holder.descricao.setText(adjustStr(receita.subtitulo));
        holder.ingredientes.setText(getIngredientStr(receita.ingredientesUsados));
        holder.imagem.setImageResource(receita.img);
    }

    String getIngredientStr(List<InstIngrediente> ingredientesUsados){
        String str = "";
        int caracterLinha = 0;
        for (int i = 0; i < ingredientesUsados.size(); i++) {
            String nxt = ingredientesUsados.get(i).nome;
            nxt += (i != ingredientesUsados.size() - 1) ? ", " : "";
            caracterLinha += nxt.length();
            if (caracterLinha > MAX_LEN) {
                str += "\n" + nxt;
                caracterLinha = nxt.length();
            } else {
                str += nxt;
            }
        }
        return str;
    }
    String adjustStr(String str) {
        String[] words = str.trim().split(" ");
        if (words.length == 0) return "";
        String res = words[0];
        int caracterLinha = 0;
        for (int i = 1 ; i < words.length; i++) {
            String nxt = " " + words[i];
            caracterLinha += nxt.length();
            if (caracterLinha > MAX_LEN) {
                res += "\n" + nxt;
                caracterLinha = nxt.length();
            } else {
                res += nxt;
            }
        }
        return res;
    }



    public void filter(String str, DatabaseReference mDatabase) {
        receitas = new ArrayList<>();

        final String match = str.toUpperCase();
        mDatabase.child("receitas").orderByChild("titulo").startAt(match).limitToFirst(50)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        receitas.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Receita r = (Receita) snapshot.getValue(Receita.class);
                            if (r.titulo.toUpperCase().startsWith(match)) {
                                receitas.add(r);
                            } else {
                                break;
                            }
                        }
                        notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });}



    @Override
    public int getItemCount() {
        return receitas.size();
    }
}