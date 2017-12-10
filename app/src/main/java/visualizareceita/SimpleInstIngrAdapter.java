package visualizareceita;

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

/** Adapter para ser usado numa RecyclerView que mostra ingredientes. Ele eh responsavel por
 * "inflar" os itens listando ingredientes e colocar a informacao certa para cada item por meio
 * de {@link InstIngredienteViewHolder}
 * Created by klaus on 30/10/17.
 */

public class SimpleInstIngrAdapter extends RecyclerView.Adapter{

        ArrayList<InstIngrediente> ingredientes;
        Context c;

    /**
     * Construtor para o viewholder
     * @param ingr  Lista de ingredientes a ser utilizada
     * @param context   contexto a ser usado
     */
        public SimpleInstIngrAdapter(ArrayList<InstIngrediente> ingr, Context context) {
            c = context;
            this.ingredientes = ingr;
        }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.item_instingrediente_transparent, parent, false);
        InstIngredienteViewHolder holder = new InstIngredienteViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final InstIngredienteViewHolder holder = (InstIngredienteViewHolder) viewHolder;

        final InstIngrediente ingr = this.ingredientes.get(position);
        holder.getNome().setText(ingr.nome.trim());
        if (!StringHelper.interpretQtde(ingr,5,true).isEmpty()) {
            holder.getIMedida().setText(ingr.unidade.trim());

        } else {
            holder.getIMedida().setText("");
        }
        holder.getQtde().setText(StringHelper.interpretQtde(ingr,13,false));
    }



    @Override
        public int getItemCount() {
            return ingredientes.size();
        }
}
