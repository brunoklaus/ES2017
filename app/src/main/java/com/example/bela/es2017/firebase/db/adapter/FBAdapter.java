package com.example.bela.es2017.firebase.db.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.viewholder.ReceitaViewHolder;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A classe FBAdapter eh o modelo de um adaptador que faz queries no banco de dados do Firebase. Sua
 * funcao eh  representar uma lista de objeto (por exemplo, receitas) na tela. O parametro T eh
 * exatamente esse objeto (por exemplo, uma receita). FBAdapter possui metodo filter que eh chamado
 * para filtrar o banco de dados. Ele delega a filtragem a um QueryRunnable, que define a semantica
 * da filtragem (por exemplo, interpretar a entrada como o titulo da receita e retornar receitas que
 * comecem com esse titulo). Subclasses de FBAdapter escolhem o QueryRunnable desejado ao implementar
 * createQueryRunnable. FBAdapter possui um sistema que evita overload de queries. Especificamente,
 * o uso de QueryRunnable permite que, ao terminar a ultima query, a proxima a ser executada seja
 * a mais recentemente recebida, pulando queries intermediarias (caso contrario, 10 queries seriam
 * enviadas ao firebase quando digitassemos "hamburguer")
 * @param <T> FBADapter
 */
public class FBAdapter<T> extends RecyclerView.Adapter implements Searcher<T>{

    private List<T> model = new ArrayList<>();//Uma lista com o tipo de dados guardado
    Context context;
    volatile QueryRunnable nextToRun = null;    //proximo runnable a rodar
    volatile QueryRunnable currentlyRunning = null; //runnable que esta rodando
    public FBAdapter(Context context) {
        this.context = context;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        throw new IllegalStateException("Must implement onCreateViewHolder");
    }

    public List<T> getModel(){
        return model;
    }
    public void setModel(List<T> r) {
        model = r;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        throw new IllegalStateException("Must implement onBindViewHolder");
    }
    QueryRunnable createQueryRunnable(String str, DatabaseReference mDatabase) {
        throw new IllegalStateException("Did not implement createQueryRunnable");
    }

    /**
     * Executado quando recebe notificacao que uma query terminou
     * @param r O resultado da query, que serah o novo model
     * @param q o queryrunnable que terminou
     */
    public synchronized void onQueryFinished(List<T> r, QueryRunnable q, boolean update){
        Log.d("d","Finished Query of " + q.getString());
        if (q.equals(this.currentlyRunning)) {
            this.model = r;
            if(update) notifyDataSetChanged();
        }
        if (nextToRun == null) {
            this.currentlyRunning = null;
        } else {
            this.currentlyRunning = nextToRun;
            this.nextToRun = null;
            new Thread(currentlyRunning).start();
        }

    }
    public void onSearchFinished(String input, List<T> results, QueryRunnable<T> q, boolean update){
        this.onQueryFinished(results,q,update);
    }


    /**
     * Filter deve ser chamado sempre que se deseja usar esse adapter para filtrar
     * o banco de dados.
     * @param str a string sendou buscada
     * @param mDatabase o referencia do banco em que a busca ocorre
     */
    public synchronized void filter(String str, DatabaseReference mDatabase) {
        Log.d("d","Called filter");
        if (this.currentlyRunning == null) {
            this.currentlyRunning = createQueryRunnable(str, mDatabase);
            Thread t = new Thread(currentlyRunning);
            Log.d("d","currentlyrunning is null, create thread " + t.getId());
            t.start();
        } else if (this.currentlyRunning.mustBeTerminated == false){
            Log.d("d","must terminate current");
            this.nextToRun = createQueryRunnable(str, mDatabase);
            this.currentlyRunning.mustBeTerminated = true;
        } else {
            this.nextToRun = createQueryRunnable(str, mDatabase);
        }

    }



    @Override
    public int getItemCount() {
        return model.size();
    }
}