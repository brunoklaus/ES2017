package com.example.bela.es2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.bela.es2017.Add_receita.Adicionar_receita;
import com.example.bela.es2017.Estoque.Estoque;
import com.example.bela.es2017.firebase.db.searchActivity.ListaReceitasActivity;
import com.example.bela.es2017.leitordebarras.AdicionaNoEstoqueActivity;
import com.example.bela.es2017.leitordebarras.LeitorDeBarras;
import com.example.bela.es2017.texttospeech.TTSActivity;
import com.example.bela.es2017.timer.TimerActivity;
import com.example.bela.es2017.timer.TimerFragment;

import java.util.ArrayList;
import java.util.List;

import visualizapasso.ScreenSlideActivity;

public class MainActivity extends AppCompatActivity {

    MainPageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagermain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        List<Fragment> list = new ArrayList<>();
        list.add(new ButtonFragment());
        list.add(new TimerFragment());

        pagerAdapter = new MainPageAdapter(getSupportFragmentManager(), list);
        ViewPager pager = (ViewPager) findViewById(R.id.pagerMain);
        pager.setAdapter(pagerAdapter);
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
