package visualizapasso;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bela.es2017.R;

/**
 * Created by klaus on 30/10/17.
 */

public class EstoqueMatcherViewHolder extends RecyclerView.ViewHolder {
    private TextView estoque;
    private  TextView receita;

    public TextView getEstoque(){return  estoque;}
    public TextView getReceita(){return  receita;}

    public EstoqueMatcherViewHolder(View itemView) {
        super(itemView);
        estoque = itemView.findViewById(R.id.estoquematcher_ingr_est);
        receita = itemView.findViewById(R.id.estoquematcher_ingr_rec);
    }
}
