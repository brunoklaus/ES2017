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

import com.example.bela.es2017.model.InstIngrediente;
import com.example.bela.es2017.model.Receita;
import com.example.bela.es2017.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lista extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private RecyclerView rView;
    private Receitas receita1, receita2;
    private SearchManager searchManager;
    private android.widget.SearchView searchView;
    private MenuItem searchItem;
    List<Receita> receitas = new ArrayList<Receita>();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        //adicionaReceitasIniciais();
        rView = (RecyclerView) findViewById(R.id.recycler1);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("receitas")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Receita r = (Receita) snapshot.getValue(Receita.class);
                            receitas.add(r);
                        }
                        setupViews();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });

       // receita1= new Receitas("Hamburguer de carne", "Como fazer um delicioso hamburguer", "Carne, ovo, farinha de trigo", R.drawable.hamburguer);
       // receita2 = new Receitas("Molho bolonhesa", "Para a melhor macarronada", "Carne, extrato de tomate, pimenta", R.drawable.molho_bolonhesa);

        //receitas.add(receita1);
        //receitas.add(receita2);
        setupViews();

    }

    void setupViews(){
        rView.setAdapter(new Adapter(receitas, this));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(layout);
        //Set up searchview
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    }



    private void adicionaHamburger(){

        InstIngrediente i1 = new InstIngrediente("Carne de vaca picada", 500, "g");
        InstIngrediente i2 = new InstIngrediente(" Cebola picada", 1, "");
        InstIngrediente i3 = new InstIngrediente("Mostarda", 1, "colher de sopa");
        InstIngrediente i4 = new InstIngrediente("Mostarda", 1, "colher de sopa");
        InstIngrediente i5 = new InstIngrediente("Molho inglẽs", 1, "colher de sopa");
        InstIngrediente i6 = new InstIngrediente("Sal", -1, "");
        InstIngrediente i7 = new InstIngrediente("Pimenta", -1, "");
        InstIngrediente i8 = new InstIngrediente("Salsa picada", 1, "raminho");

        List<InstIngrediente> l = Arrays.asList(i1,i2,i3,i4,i5,i6,i7,i8);

        String d1 = "Numa taça misturar todos os ingredientes, excepto o sal.\n" +
                "Retirar porções de carne para formar os hambúrgueres, começando por fazer uma bola e, depois, espalmando com a mão.\n" +
                "Deixar os hambúrgueres a repousar cerca de 15 minutos no frigorífico.\n" +
                "Levar o grelhador ao lume para que aqueça. Temperar os hambúrgueres com sal.\n" +
                "Untar o grelhador com manteiga (poderá ser manteiga com salsa e alho já incorporados) e colocar os hambúrgueres a grelhar.\n" +
                "Servir com salada.";


        Receita hamb = new Receita("Hamburguer de carne","Como fazer um delicioso hamburguer",
                d1,l,R.drawable.hamburguer);
        mDatabase.child("receitas").push().setValue(hamb);
    }
    private void adicionaBolonhesa(){

        InstIngrediente i1 = new InstIngrediente("Carne de vaca picada", 500, "g");
        InstIngrediente i2 = new InstIngrediente("Polpa de tomate", 200, "g");
        InstIngrediente i3 = new InstIngrediente("Massa Espaguete", 350, "g");
        InstIngrediente i4 = new InstIngrediente("Dentes de alho", 3, "");
        InstIngrediente i5 = new InstIngrediente("Tomates maduros", 4, "200g");
        InstIngrediente i6 = new InstIngrediente("Cebola", 1, "");
        InstIngrediente i7 = new InstIngrediente("Orégão", -1, "");
        InstIngrediente i8 = new InstIngrediente("Azeite", 1, "");
        InstIngrediente i9 = new InstIngrediente("Sal", 1, "");
        List<InstIngrediente> l = Arrays.asList(i1,i2,i3,i4,i5,i6,i7,i8,i9);
        String d1 = "Num tacho alto, fritar a cebola e o alho picados.\n" +
                "Deitar de seguida o tomate em pedaços e a polpa de tomate.\n" +
                "Deixar refogar.\n" +
                "Juntar a carne picada, temperar com o sal. Deixar cozer.\n" +
                "Levar ao lume um tacho com a água temperada com sal, deixar ferver.\n" +
                "Deitar o esparguete. Deixar cozer, mexer a espaços até ficar al dente.\n" +
                "Juntar o molho ao esparguete.\n" +
                "Temperar com orégãos a gosto.\n" +
                "Servir quente.";
        Receita bol = new Receita("Molho bolonhesa", "Para a melhor macarronada",
                d1,l,R.drawable.molho_bolonhesa);
        mDatabase.child("receitas").push().setValue(bol);

    }



    void adicionaReceitasIniciais() {
        adicionaBolonhesa();
        adicionaHamburger();

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
