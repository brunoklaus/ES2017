package com.example.bela.es2017;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Fragment_adicionar_receita2 extends Fragment  implements View.OnClickListener {

    public EditText preparo;
    public LinearLayout add_container_preparo;
    public Button adicionarPrep;
    public int passo = 0;

    public Fragment_adicionar_receita2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_adicionar_receitas2, container, false);

        //declarando views do layout do fragment2
        preparo = (EditText)view.findViewById(R.id.editText_preparo);
        passo++;
        preparo.setText(Integer.toString(passo) + ". ");

        add_container_preparo = (LinearLayout)view.findViewById(R.id.add_container_preparo);

        //declarando botao
        adicionarPrep = (Button) view.findViewById(R.id.item_adicionar_preparo);
        adicionarPrep.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        //no lugar de getActivity estava getBaseContext
        LayoutInflater layoutInflater =
                (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView_item = layoutInflater.inflate(R.layout.item_adicionar_receita_preparo, add_container_preparo, false);

        TextView prep = (TextView) addView_item.findViewById(R.id.editText_preparo_preenchido);

        prep.setText(preparo.getText().toString());

        add_container_preparo.addView(addView_item);

        passo++;
        preparo.setText(Integer.toString(passo) + ". ");
    }

}
