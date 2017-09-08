package com.example.bela.es2017;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class Lista extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private RecyclerView rView;
    private Receitas receita1, receita2;
    private SearchManager searchManager;
    private android.widget.SearchView searchView;
    private MenuItem searchItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        rView = (RecyclerView) findViewById(R.id.recycler1);

        receita1= new Receitas("Hamburguer de carne", "Como fazer um delicioso hamburguer", "Carne, ovo, farinha de trigo", R.drawable.hamburguer);
        receita2 = new Receitas("Molho bolonhesa", "Para a melhor macarronada", "Carne, extrato de tomate, pimenta", R.drawable.molho_bolonhesa);

        List<Receitas> receitas = new ArrayList<Receitas>();
        receitas.add(receita1);
        receitas.add(receita2);

        rView.setAdapter(new Adapter(receitas, this));

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rView.setLayoutManager(layout);

        //Set up searchview
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo
                (searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.requestFocus();

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

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //TODO: filtrar recyclerview
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //TODO: filtrar recyclerview
        return false;
    }

}
