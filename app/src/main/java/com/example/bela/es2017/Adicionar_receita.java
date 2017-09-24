package com.example.bela.es2017;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Adicionar_receita extends AppCompatActivity {

    private EditText ingrediente;
    private EditText modo_prepato;
    private EditText tempo_preparo;
    private EditText dificuldade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_receita);

        ingrediente = (EditText)findViewById(R.id.editText_ingrediente);
        modo_prepato = (EditText)findViewById(R.id.editText_preparo);
        tempo_preparo = (EditText)findViewById(R.id.editText_tempo);
        dificuldade = (EditText)findViewById(R.id.editText_dificuldade);

        //os hints nao aparecem...desisto
        ingrediente.setHint("oi");
        modo_prepato.setHint("tchau");
    }
}
