package com.example.bela.es2017.Add_receita;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.helpers.StringHelper;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_adicionar_receita2 extends Fragment  implements View.OnClickListener {

    private Button adicionarIng;
    public EditText ingrediente, quantidade;
    public EditText unidade;
    public LinearLayout add_container;
    ArrayList<InstIngrediente> ingredientesEscolhidos;   //Ingredientes recebidos na entrada
    final Fragment_adicionar_receita2 frag = this;

    public Fragment_adicionar_receita2() {
        // Required empty public constructor
    }

    public ArrayList<InstIngrediente> getIngredientes() {
        return ingredientesEscolhidos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_adicionar_receita2, container, false);

        //declarando views do layout do fragment1
        ingrediente = (EditText)view.findViewById(R.id.editText_ingrediente_original);
        quantidade = (EditText)view.findViewById(R.id.editText_quantidade_original);
        unidade = (EditText) view.findViewById(R.id.spinner_unidade_original);

        add_container = (LinearLayout)view.findViewById(R.id.add_container);

        //declarando botao
        adicionarIng = (Button) view.findViewById(R.id.item_adicionar_ingrediente);
        adicionarIng.setOnClickListener(this);

        ingredientesEscolhidos = new ArrayList<>();

        return view;
    }

    /**
     * Remove o ingrediente da lista de ingredientes da entrada {@code ingredientesEscolhidos}
     * @param ingr ingrediente a ser removido
     */
    private void removeFromIngrList(InstIngrediente ingr){
        for (int i = 0; i < ingredientesEscolhidos.size(); i++) {
            InstIngrediente el = ingredientesEscolhidos.get(i);
            if (Double.compare(ingr.qtde, ingr.qtde) == 0 &&
                    ingr.nome == el.nome &&
                    ingr.unidade == el.unidade) {
                ingredientesEscolhidos.remove(i);
                return;
            }
        }
        throw new IllegalStateException("Ingrediente a ser removido nao encontrado entre" +
                "os adicionados");
    }



    @Override
    public void onClick(View view) {
        //no lugar de getActivity estava getBaseContext
        LayoutInflater layoutInflater =
                (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView_item = layoutInflater.inflate(R.layout.item_adicionar_receita_ingrediente, add_container, false);

        final InstIngrediente novoIngr;
        try {
            if (unidade.getText().toString().trim().isEmpty() &&
                    !quantidade.getText().toString().trim().isEmpty()) {
                throw new IllegalArgumentException("unidade nao pode ser vazia se " +
                        "quantidade nao for");
            } else if (quantidade.getText().toString().trim().isEmpty() &&
                    unidade.getText().toString().trim().isEmpty()) {

                novoIngr = new InstIngrediente(ingrediente.getText().toString().trim(),-1,"");
            } else {
                novoIngr = new InstIngrediente(ingrediente.getText().toString().trim(),
                        StringHelper.interpretQtde(quantidade.getText().toString().trim()),
                        unidade.getText().toString().trim());
            }

            ingredientesEscolhidos.add(novoIngr);

        } catch (Exception ex) {
            if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
                Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(),"Dados de entrada invalidos",Toast.LENGTH_SHORT).show();
            }
            return;
        }

        TextView textOut = (TextView) addView_item.findViewById(R.id.editText_ingredienteEsc);
        Button btn_cancela = (Button)addView_item.findViewById(R.id.ingrediente_cancela);
        final Context c = getActivity();
        btn_cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Remove o ingrediente da lista e remove a view correspondente
                removeFromIngrList(novoIngr);
                ViewGroup parentView = (ViewGroup) addView_item.getParent();
                parentView.removeView(addView_item);
            }
        });

        String displayStr;
        if (novoIngr.qtde <= 0) {
            textOut.setText(ingrediente.getText().toString());
        } else {
            textOut.setText(ingrediente.getText().toString() + " - " + quantidade.getText().toString() +
                    " " + unidade.getText().toString());
        }

        add_container.addView(addView_item);
        unidade.setText("");
        quantidade.setText("");
        ingrediente.setText("");
    }

}
