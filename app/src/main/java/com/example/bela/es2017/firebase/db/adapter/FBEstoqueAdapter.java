package com.example.bela.es2017.firebase.db.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.User;
import com.example.bela.es2017.firebase.db.runnable.AQTEstoque;
import com.example.bela.es2017.firebase.db.viewholder.InstIngredienteViewHolder;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.example.bela.es2017.helpers.FBInsereReceitas;
import com.example.bela.es2017.helpers.StringHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**Adapter para o estoque. Responsavel por
 * @see {FBAdapter}
 */
public class FBEstoqueAdapter extends FBAdapter<InstIngrediente> {



    public FBEstoqueAdapter(Context context)
    {
        super(context);
    }


    @Override
    QueryRunnable createQueryRunnable(String str, DatabaseReference mDatabase) {
        //nosso queryrunnable aqui eh um ReceitaTituloRunnable
        return new AQTEstoque(this, mDatabase, str, InstIngrediente.class);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_instingrediente, parent, false);
        InstIngredienteViewHolder holder = new InstIngredienteViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        final InstIngredienteViewHolder holder = (InstIngredienteViewHolder) viewHolder;

        final InstIngrediente ingr = this.getModel().get(position);
        holder.getNome().setText(ingr.nome.trim());
        if (!StringHelper.interpretQtde(ingr,5,true).isEmpty()) {
            holder.getIMedida().setText(ingr.unidade.trim());

        } else {
            holder.getIMedida().setText("");
        }
        holder.getQtde().setText(StringHelper.interpretQtde(ingr,13,false));


        holder.getNome().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Quando clicar no ingrediente, preencher campos
                SearchView s = ((Activity)context).findViewById(R.id.barras_entrada_search);
                s.setQuery(holder.getNome().getText(),true);
                TextView t =  ((Activity)context).findViewById(R.id.barras_entrada_unidade);
                t.setText(holder.getIMedida().getText().toString());

            }
        });
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Searcher<Boolean> updateItems = new Searcher<Boolean>() {
            @Override
            public void onSearchFinished(String query, List<Boolean> results, QueryRunnable<Boolean> q, boolean update) {

                filter("", FirebaseDatabase.getInstance().getReference());
            }
        };
        holder.getRemoveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FBInsereReceitas.removedoEstoque(user,mDatabase,holder.getNome().getText().toString(),updateItems);
            }
        });

    }


}