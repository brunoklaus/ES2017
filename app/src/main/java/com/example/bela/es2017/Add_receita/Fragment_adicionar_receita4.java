package com.example.bela.es2017.Add_receita;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.Dificuldade;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_adicionar_receita4 extends Fragment {


    private EditText etTempo_Horas;
    private EditText etTempo_Minutos;
    private EditText etTempo_Segundos;
    private EditText etPorcoes;
    private Spinner sDificuldade;
    private EditText etTags;

    public Integer getPorcoes() throws NumberFormatException {
        if (etPorcoes.getText().toString().trim().isEmpty()) {
            Log.d("D","porcoes estava vazio");
            return null;
        } else {
            return Integer.parseInt(etPorcoes.getText().toString());
        }
    }

    public Dificuldade getDificuldade() {
        return (Dificuldade) sDificuldade.getSelectedItem();
    }
    public ArrayList<String> getTags(){
        ArrayList<String> tags = new ArrayList<>();
        String[] tagsSplit = etTags.getText().toString().split(",");

        for (String str : tagsSplit) {
            str = str.trim();
            if (!str.isEmpty()) {
                tags.add(str);
            }
        }
        return tags;
    }


    public Integer getTempo() throws NumberFormatException{
        String h = etTempo_Horas.getText().toString().trim();
        String m = etTempo_Minutos.getText().toString().trim();
        String s =  etTempo_Segundos.getText().toString().trim();

        if (h.isEmpty() && m.isEmpty() && s.isEmpty()) {
            Log.d("D","tempo estava vazio");
            return null;
        } else {
            Integer duration = 0;
            if (!h.isEmpty()){
                duration += 3600 * Integer.parseInt(h);
            }
            if (!m.isEmpty()) {
                duration += 60 * Integer.parseInt(m);
            }
            if (!s.isEmpty()) {
                duration += Integer.parseInt(s);
            }
            return duration;
        }
    }


    public Fragment_adicionar_receita4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_adicionar_receita4, container, false);

        etTempo_Horas = (EditText) view.findViewById(R.id.frag4_entrada_tempo_etHora);
        etTempo_Minutos = (EditText) view.findViewById(R.id.frag4_entrada_tempo_etMinuto);
        etTempo_Segundos = (EditText) view.findViewById(R.id.frag4_entrada_tempo_etSegudo);
        sDificuldade = (Spinner) view.findViewById(R.id.frag4_editText_dificuldade);
        sDificuldade.setAdapter(new ArrayAdapter<Dificuldade>(getActivity(),
                android.R.layout.simple_list_item_1,Dificuldade.values()));
        sDificuldade.setSelection(2);
        etPorcoes = (EditText) view.findViewById(R.id.frag4_editText_porcoes);
        etTags = (EditText) view.findViewById(R.id.frag4_editText_tags);


        //Mudar o foco pro prox. apos digitar hora ou minuto
        etTempo_Horas.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    etTempo_Minutos.requestFocus();
                }
                return false;
            }
        });
        etTempo_Minutos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    etTempo_Segundos.requestFocus();
                }
                return false;
            }
        });

        return view;
    }

}
