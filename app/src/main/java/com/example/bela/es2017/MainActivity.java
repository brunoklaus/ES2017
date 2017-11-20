package com.example.bela.es2017;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.bela.es2017.Add_receita.Adicionar_receita;
import com.example.bela.es2017.Estoque.Estoque;
import com.example.bela.es2017.firebase.db.searchActivity.ListaReceitasActivity;
import com.example.bela.es2017.leitordebarras.AdicionaNoEstoqueActivity;
import com.example.bela.es2017.leitordebarras.LeitorDeBarras;
import com.example.bela.es2017.texttospeech.TTSActivity;
import com.example.bela.es2017.timer.TimerActivity;

import visualizapasso.ScreenSlideActivity;

public class MainActivity extends AppCompatActivity  {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        /*
        Button botao_barras = (Button) findViewById(R.id.botao_barras);
        botao_barras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it2 = new Intent(MainActivity.this, LeitorDeBarras.class);
                startActivity(it2);
            }
        });


        Button botao_estoque = (Button) findViewById(R.id.botao_estoque);
        botao_estoque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it2 = new Intent(MainActivity.this, Estoque.class);
                startActivity(it2);
            }
        });

        Button botao_estoque_add = (Button) findViewById(R.id.botao_estoque_manual);
        botao_estoque_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it2 = new Intent(MainActivity.this, AdicionaNoEstoqueActivity.class);
                startActivity(it2);
            }
        });


        Button botao_add_receitas = (Button) findViewById(R.id.botao_add_receita);
        botao_add_receitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it3 = new Intent(MainActivity.this, Adicionar_receita.class);
                startActivity(it3);
            }
        });
        Button botao_timer = (Button) findViewById(R.id.botao_timer);
        botao_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it3 = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(it3);
            }
        });
        Button botao_busca = (Button) findViewById(R.id.botao_busca);
        botao_busca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it3 = new Intent(MainActivity.this, ListaReceitasActivity.class);
                startActivity(it3);
            }
        });

        Button botao_tts = (Button) findViewById(R.id.botao_tts);
        botao_tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it7 = new Intent(MainActivity.this, TTSActivity.class);
                startActivity(it7);
            }
        });
        */


    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = FirstFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = FirstFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = FirstFragment.class;
                break;
            default:
                fragmentClass = FirstFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }
}
