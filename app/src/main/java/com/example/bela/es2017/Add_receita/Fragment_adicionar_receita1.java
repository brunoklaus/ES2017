package com.example.bela.es2017.Add_receita;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

    public Fragment_adicionar_receita1() {
        // Required empty public constructor
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

        return view;
    }

    @Override
    public void onClick(View view) {
        //no lugar de getActivity estava getBaseContext
        LayoutInflater layoutInflater =
                (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView_item = layoutInflater.inflate(R.layout.item_adicionar_receita_ingrediente, add_container, false);

        TextView textOut = (TextView) addView_item.findViewById(R.id.editText_ingrediente);
        TextView textOut2 = (TextView) addView_item.findViewById(R.id.editText_quantidade);
        Spinner textOut3 = (Spinner) addView_item.findViewById(R.id.spinner_unidade);

        textOut.setText(ingrediente.getText().toString());
        textOut2.setText(quantidade.getText().toString());

        add_container.addView(addView_item);

        quantidade.setText(" ");
        ingrediente.setText(" ");

        //testando o botao
        //Intent intent = new Intent(getActivity(), MainActivity.class);
        //startActivity(intent);
    }

}
