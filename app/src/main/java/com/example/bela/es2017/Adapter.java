package com.example.bela.es2017;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Collections;
import java.util.List;

public class Adapter extends RecyclerView.Adapter {

    private List<Receita> receitas = new ArrayList<>();
    private Context context;
    public final int MAX_LEN = 27;
    volatile QueryRunnable nextToRun = null;
    volatile QueryRunnable currentlyRunning = null;
    public Adapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_receita, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public List<Receita> getReceitas(){
        return receitas;
    }
    public void setReceitas(List<Receita> r) {
        receitas = r;
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


    public synchronized void onQueryFinished(List<Receita> r, QueryRunnable q){
        Log.d("d","teste - " + q.str);
        if (q.equals(this.currentlyRunning)) {
            this.receitas = r;
            notifyDataSetChanged();
        }
        if (nextToRun == null) {
            this.currentlyRunning = null;
        } else {
            this.currentlyRunning = nextToRun;
            this.nextToRun = null;
            new Thread(currentlyRunning).start();
        }

    }

    public synchronized void filter(String str, DatabaseReference mDatabase) {
        Log.d("d","Called filter");
        if (this.currentlyRunning == null) {
            this.currentlyRunning = new QueryRunnable(this,mDatabase,str);
            Thread t = new Thread(currentlyRunning);
            Log.d("d","currentlyrunning is null, create thread " + t.getId());
            t.start();
        } else if (this.currentlyRunning.mustBeTerminated == false){
            Log.d("d","must terminate current");
            this.nextToRun = new QueryRunnable(this,mDatabase,str);
            this.currentlyRunning.mustBeTerminated = true;
        } else {
            this.nextToRun = new QueryRunnable(this,mDatabase,str);
        }

    }



    @Override
    public int getItemCount() {
        return receitas.size();
    }
}