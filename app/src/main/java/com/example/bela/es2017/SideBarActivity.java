package com.example.bela.es2017;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bela.es2017.Add_receita.Adicionar_receita;
import com.example.bela.es2017.firebase.db.searchActivity.ListaReceitasActivity;
import com.example.bela.es2017.leitordebarras.AdicionaNoEstoqueActivity;
import com.example.bela.es2017.leitordebarras.LeitorDeBarras;
import com.example.bela.es2017.receitaspossiveis.ReceitasPossiveisActivity;
import com.example.bela.es2017.timer.TimerActivity;

import recomendareceitas.RecomendaReceitasActivity;

/**
 * Created by klaus on 09/12/17.
 */

public abstract class SideBarActivity extends AppCompatActivity {


    protected abstract SideBarInfo getInfo();


    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout dl;
    private SideBarInfo info;


    private ActionBarDrawerToggle getToggle(){
        return new ActionBarDrawerToggle(this, dl, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("TAG", "onDrawerClosed: " + getTitle());
                invalidateOptionsMenu();
            }
        };
    }

    public void setContentViewWithSideBar(Activity act, int layoutResID){
        act.setContentView(R.layout.menu_drawer);
        act.getLayoutInflater().inflate(layoutResID,(ViewGroup) act.findViewById(R.id.content_frame));
        // DrawerLayout
        dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = getToggle();
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        dl.setDrawerListener(mDrawerToggle);
        NavigationView navView = act.findViewById(R.id.navigation);
        final Activity ac = act;
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent it = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_item_add:
                        it = new Intent(ac, Adicionar_receita.class);
                        break;
                    case R.id.nav_item_barras:
                        it = new Intent(ac, LeitorDeBarras.class);
                        break;
                    case R.id.nav_item_busca:
                        it = new Intent(ac, ListaReceitasActivity.class);
                        break;
                    case R.id.nav_item_estoque:
                        it = new Intent(ac, AdicionaNoEstoqueActivity.class);
                        break;
                    case R.id.nav_item_main:
                        it = new Intent(ac, MainActivity.class);
                        break;
                    case R.id.nav_item_timer:
                        it = new Intent(ac, TimerActivity.class);
                        break;
                    case R.id.nav_item_receitaspossiveis:
                        it = new Intent(ac, ReceitasPossiveisActivity.class);
                        break;
                    case R.id.nav_item_recomenda:
                        it = new Intent(ac, RecomendaReceitasActivity.class);
                        break;


                }
                if (it != null){
                    startActivity(it);
                }
                return true;

            }
        });

    }

    public  void replaceActionBar(AppCompatActivity act, int toolbarLayoutId){
        Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar);
        toolbar.setTitle(info.getTitle());
        toolbar.setTitleTextColor(Color.WHITE);
        //toolbar.setSubtitle("Sub");
        act.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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


        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = getInfo();
        setContentViewWithSideBar(this, info.getLayoutID());
        replaceActionBar(this, R.id.toolbar);
    }

    protected void updateInfo(SideBarInfo inf){
        this.info = inf;
        replaceActionBar(this, R.id.toolbar);
        // DrawerLayout
        dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = getToggle();
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue));
        dl.setDrawerListener(mDrawerToggle);
    }



}
