package com.example.bela.es2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.bela.es2017.Add_receita.Adicionar_receita;
import com.example.bela.es2017.Estoque.Estoque;
import com.example.bela.es2017.firebase.db.searchActivity.ListaEstoqueActivity;
import com.example.bela.es2017.firebase.db.searchActivity.ListaReceitasActivity;
import com.example.bela.es2017.leitordebarras.LeitorDeBarras;
import com.example.bela.es2017.texttospeech.TTSActivity;
import com.example.bela.es2017.timer.TimerActivity;

public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Intent it = new Intent(MainActivity.this, LeitorDeBarras.class);
                //startActivity(it);
            }
        });

        Button botao_estoque = (Button) findViewById(R.id.button2);
        botao_estoque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it2 = new Intent(MainActivity.this, Estoque.class);
                startActivity(it2);
            }
        });

        Button botao_add_receitas = (Button) findViewById(R.id.button3);
        botao_add_receitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it3 = new Intent(MainActivity.this, Adicionar_receita.class);
                startActivity(it3);
            }
        });
        Button botao_timer = (Button) findViewById(R.id.button4);
        botao_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it3 = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(it3);
            }
        });
        Button botao_busca = (Button) findViewById(R.id.button5);
        botao_busca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it3 = new Intent(MainActivity.this, ListaReceitasActivity.class);
                startActivity(it3);
            }
        });

        Button botao_tts = (Button) findViewById(R.id.button7);
        botao_tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it7 = new Intent(MainActivity.this, TTSActivity.class);
                startActivity(it7);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
