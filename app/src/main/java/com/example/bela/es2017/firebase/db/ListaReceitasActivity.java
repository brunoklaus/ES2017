package com.example.bela.es2017.firebase.db;

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

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.adapter.FBAdapter;
import com.example.bela.es2017.firebase.db.adapter.FBReceitasAdapter;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.helpers.FBInsereReceitas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaReceitasActivity<T extends FBAdapter> extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView rView;
    private SearchManager searchManager;
    private android.widget.SearchView searchView;
    private MenuItem searchItem;
    private FBReceitasAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        rView = (RecyclerView) findViewById(R.id.recycler1);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setupViews();

    }

    void setupViews() {
        mAdapter = new FBReceitasAdapter(this);
        mAdapter.filter("", mDatabase);
        rView.setAdapter(mAdapter);
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
        mAdapter.filter(query, mDatabase);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.filter(newText, mDatabase);
        return false;
    }

}
