package com.example.bela.es2017.firebase.db.searchActivity;

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
import com.example.bela.es2017.firebase.db.model.sapo.SapoReceita;
import com.example.bela.es2017.helpers.FBInsereReceitas;
import com.example.bela.es2017.helpers.StringHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView rView;
    private SearchManager searchManager;
    private android.widget.SearchView searchView;
    private MenuItem searchItem;
    FBAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        rView = (RecyclerView) findViewById(R.id.recycler1);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setupViews();
        /*
        Gson g = new Gson();
        String founderGson = StringHelper.readFromJsonFile(this);
        Type foundListType = new TypeToken<ArrayList<SapoReceita>>(){}.getType();
        List<SapoReceita> str = new Gson().fromJson(founderGson, foundListType);
        FBInsereReceitas.adicionaReceitasIniciais(mDatabase);

        for (SapoReceita s : str) {
            List<InstIngrediente> ingr = new ArrayList<>();
            for (int i = 1; i < s.ingr.size(); i+=2) {
                String ingrStr = s.ingr.get(i);
                String numberStr = StringHelper.findLongestMatch("\\d*\\.?\\d+",ingrStr);
                String otherStr  = ingrStr.replaceAll("\\d*\\.?\\d+","");
                Double d = (numberStr == null || numberStr.isEmpty()) ? new Double("-1") : Double.parseDouble(numberStr);
                ingr.add(new InstIngrediente(s.ingr.get(i-1),d,otherStr));
            }
            Receita r = new Receita(s.titulo,s.subtitulo,s.descr, ingr, -1);
            if(s.descr.isEmpty() || ingr.isEmpty()) continue;
            FBInsereReceitas.insereReceita(mDatabase,r,true);
        }
        int a = 1;
        */

    }

    abstract void initAdapter();

    void setupViews() {
        initAdapter();
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
