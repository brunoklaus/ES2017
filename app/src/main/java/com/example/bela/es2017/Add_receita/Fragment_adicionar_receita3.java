package com.example.bela.es2017.Add_receita;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bela.es2017.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_adicionar_receita3 extends Fragment {


    private EditText etTempo;
    private EditText etDificuldade;
    private EditText etPorcoes;


    public Integer getPorcoes() throws NumberFormatException {
        if (etPorcoes.getText().toString().trim().isEmpty()) {
            Log.d("D","porcoes estava vazio");
            return null;
        } else {
            return Integer.parseInt(etPorcoes.getText().toString());
        }
    }

    public Integer getDificuldade() throws NumberFormatException{
        return Integer.parseInt(etDificuldade.getText().toString());
    }

    public String getTempo() throws NumberFormatException{
        if (etTempo.getText().toString().trim().isEmpty()) {
            Log.d("D","tempo estava vazio");
            return null;
        } else {
            return etTempo.getText().toString();
        }
    }


    public Fragment_adicionar_receita3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_adicionar_receita3, container, false);

        etTempo = view.findViewById(R.id.frag3_editText_tempo);
        etDificuldade = view.findViewById(R.id.frag3_editText_dificuldade);
        etPorcoes = view.findViewById(R.id.frag3_editText_porcoes);


        return view;
    }

}
