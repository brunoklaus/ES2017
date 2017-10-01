package com.example.bela.es2017;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_adicionar_receita1 extends Fragment{

    //private Ingredientes ing1;
    //private ImageButton adicionarIng;
    //private ArrayList arrTemp = new ArrayList();
    //private ArrayList array = new ArrayList();
    private Adicionar_receita_Adapter adicionarreceitaAdapter;


    //private EditText nome;
    //private EditText quantidade;

    List<Ingredientes> ingredientes = new ArrayList<Ingredientes>();


    public Fragment_adicionar_receita1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_adicionar_receita1, container, false);
    /*
        adicionarIng = (ImageButton) view.findViewById(R.id.item_adicionar_ingrediente);
        adicionarIng.setOnClickListener(this);

        nome = (EditText)view.findViewById(R.id.editText_ingrediente);
        quantidade = (EditText)view.findViewById(R.id.editText_quantidade);
        String nomeIng = nome.getText().toString();
        String quantIng = quantidade.getText().toString();

        ing1 = new Ingredientes(nomeIng, quantIng, R.drawable.ic_action_name);

        ingredientes.add(ing1);

        arrTemp.add(ingredientes);

        adicionarreceitaAdapter= new Adicionar_receita_Adapter();
        ListView listView = (ListView) view.findViewById(R.id.list_ingrediente);
        listView.setAdapter(adicionarreceitaAdapter);
        */
        return view;
    }
    /*
    public void onClick(View view) {
        ingredientes.add(ing1);
        adicionarreceitaAdapter.notifyDataSetChanged();
    }
 */
}
