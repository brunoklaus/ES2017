package com.example.bela.es2017;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bela.es2017.Add_receita.Adicionar_receita;
import com.example.bela.es2017.Estoque.Estoque;
import com.example.bela.es2017.firebase.db.searchActivity.ListaReceitasActivity;
import com.example.bela.es2017.leitordebarras.AdicionaNoEstoqueActivity;
import com.example.bela.es2017.leitordebarras.LeitorDeBarras;
import com.example.bela.es2017.texttospeech.TTSActivity;
import com.example.bela.es2017.timer.TimerActivity;

import visualizapasso.ScreenSlideActivity;


public class ButtonFragment extends Fragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup layout = (ViewGroup)  inflater.inflate(R.layout.activity_main, container, false);
        FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(ButtonFragment.this.getActivity(), ScreenSlideActivity.class);
                startActivity(it);
            }
        });

        Button botao_barras = (Button) layout.findViewById(R.id.botao_barras);
        botao_barras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it2 = new Intent(ButtonFragment.this.getActivity(), LeitorDeBarras.class);
                startActivity(it2);
            }
        });


        Button botao_estoque = (Button) layout.findViewById(R.id.botao_estoque);
        botao_estoque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it2 = new Intent(ButtonFragment.this.getActivity(), Estoque.class);
                startActivity(it2);
            }
        });

        Button botao_estoque_add = (Button) layout.findViewById(R.id.botao_estoque_manual);
        botao_estoque_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it2 = new Intent(ButtonFragment.this.getActivity(), AdicionaNoEstoqueActivity.class);
                startActivity(it2);
            }
        });


        Button botao_add_receitas = (Button) layout.findViewById(R.id.botao_add_receita);
        botao_add_receitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it3 = new Intent(ButtonFragment.this.getActivity(), Adicionar_receita.class);
                startActivity(it3);
            }
        });
        Button botao_timer = (Button) layout.findViewById(R.id.botao_timer);
        botao_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it3 = new Intent(ButtonFragment.this.getActivity(), TimerActivity.class);
                startActivity(it3);
            }
        });
        Button botao_busca = (Button) layout.findViewById(R.id.botao_busca);
        botao_busca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it3 = new Intent(ButtonFragment.this.getActivity(), ListaReceitasActivity.class);
                startActivity(it3);
            }
        });

        Button botao_tts = (Button) layout.findViewById(R.id.botao_tts);
        botao_tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it7 = new Intent(ButtonFragment.this.getActivity(), TTSActivity.class);
                startActivity(it7);
            }
        });
        return layout;
    }

}
