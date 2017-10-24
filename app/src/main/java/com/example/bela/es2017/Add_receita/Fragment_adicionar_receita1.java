package com.example.bela.es2017.Add_receita;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.bela.es2017.R;

/**
 * Created by klaus on 21/10/17.
 */

public class Fragment_adicionar_receita1 extends Fragment {


    private EditText etNome;
    private EditText etDescricao;


    public String getNome()  {
        return etNome.getText().toString();
    }

    public String getDescricao() throws NumberFormatException {
        if (etDescricao.getText().toString().trim().isEmpty()) {
            Log.d("D","descricao estava vazio");
            return "";
        } else {
            return etDescricao.getText().toString();
        }
    }


    public Fragment_adicionar_receita1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_adicionar_receita1, container, false);

        etNome = view.findViewById(R.id.frag1_editText_nome);
        etDescricao = view.findViewById(R.id.frag1_editText_descricao);
        return view;
    }
}

