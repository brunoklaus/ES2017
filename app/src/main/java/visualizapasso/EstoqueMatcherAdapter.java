package visualizapasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.viewholder.InstIngredienteViewHolder;
import com.example.bela.es2017.helpers.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by klaus on 30/10/17.
 */

public class EstoqueMatcherAdapter extends RecyclerView.Adapter {
    Context c;
    ArrayList<InstIngrediente> ingredientes;
    HashMap<String, String> receitaToEstoque = new HashMap<>();

    public EstoqueMatcherAdapter(ArrayList<InstIngrediente> ingr, Context context) {
        c = context;
        this.ingredientes = ingr;
    }
    public void addMap(String nomeIngrReceita, InstIngrediente match ) {
        ArrayList<InstIngrediente> l = new ArrayList<>(Arrays.asList(match));
        receitaToEstoque.put(nomeIngrReceita,StringHelper.
                getIngredientStr(l,true));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.item_estoquematcher, parent, false);
        EstoqueMatcherViewHolder holder = new EstoqueMatcherViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final EstoqueMatcherViewHolder holder = (EstoqueMatcherViewHolder) viewHolder;
        ArrayList<InstIngrediente> l = new ArrayList<>(Arrays.asList(ingredientes.get(position)));
        holder.getReceita().setText(StringHelper.getIngredientStr(l,true));
        String match = receitaToEstoque.get(ingredientes.get(position).nome);
        if(match != null){
            holder.getEstoque().setText(match);
        }
    }

    @Override
    public int getItemCount() {
        return ingredientes.size();
    }
}
