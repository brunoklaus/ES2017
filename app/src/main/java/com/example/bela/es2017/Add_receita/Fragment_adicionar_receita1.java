package com.example.bela.es2017.Add_receita;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.Unidade;

import com.example.bela.es2017.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_adicionar_receita1 extends Fragment  implements View.OnClickListener {

    private Button adicionarIng;
    public EditText ingrediente, quantidade;
    public Spinner unidade;
    public LinearLayout add_container;
    ArrayList<InstIngrediente> ingredientesEscolhidos;   //Ingredientes recebidos na entrada
    final Fragment_adicionar_receita1 frag = this;

    public Fragment_adicionar_receita1() {
        // Required empty public constructor
    }

    public ArrayList<InstIngrediente> getIngredientes() {
        return ingredientesEscolhidos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_adicionar_receita1, container, false);

        //declarando views do layout do fragment1
        ingrediente = (EditText)view.findViewById(R.id.editText_ingrediente_original);
        quantidade = (EditText)view.findViewById(R.id.editText_quantidade_original);
        unidade = (Spinner) view.findViewById(R.id.spinner_unidade_original);

        add_container = (LinearLayout)view.findViewById(R.id.add_container);

        //declarando botao
        adicionarIng = (Button) view.findViewById(R.id.item_adicionar_ingrediente);
        adicionarIng.setOnClickListener(this);

        //usa-se adaptador para mostrar os valores possiveis do enum no Spinner
        unidade.setAdapter(new ArrayAdapter<Unidade.uEnum>(this.getContext(),
                android.R.layout.simple_list_item_1, Unidade.uEnum.values()

        ) );

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
                    ingr.unidadeEnum == el.unidadeEnum) {
                ingredientesEscolhidos.remove(i);
                break;
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
            novoIngr = new InstIngrediente(ingrediente.getText().toString(),
                    Double.parseDouble(quantidade.getText().toString()),
                            ((Unidade.uEnum)unidade.getSelectedItem()).toString());
            ingredientesEscolhidos.add(novoIngr);

        } catch (Exception ex) {
            Toast.makeText(getContext(),"Dados de entrada invalidos",Toast.LENGTH_LONG);
            return;
        }

        TextView textOut = (TextView) addView_item.findViewById(R.id.editText_ingredienteEsc);
        Button btn_cancela = (Button)addView_item.findViewById(R.id.ingrediente_cancela);
        final Context c = getContext();
        btn_cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Remove o ingrediente da lista e remove a view correspondente
                removeFromIngrList(novoIngr);
                ViewGroup parentView = (ViewGroup) addView_item.getParent();
                parentView.removeView(addView_item);
            }
        });

        textOut.setText(ingrediente.getText().toString() + " - " + quantidade.getText().toString() +
                " " + ((Unidade.uEnum)unidade.getSelectedItem()).toString());

        add_container.addView(addView_item);

        quantidade.setText(" ");
        ingrediente.setText(" ");
    }

}
