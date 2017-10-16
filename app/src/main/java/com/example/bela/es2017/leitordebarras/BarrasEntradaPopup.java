package com.example.bela.es2017.leitordebarras;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.adapter.FBEstoqueConfirmaAdapter;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.searchActivity.SearchActivity;
import com.example.bela.es2017.firebase.searcher.RecebeSeleciona;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by klaus on 14/10/17.
 */

public class BarrasEntradaPopup extends SearchActivity implements RecebeSeleciona<InstIngrediente> {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView rView;

    private Button btn_seleciona;
    private Button btn_confirma;
    private Button btn_cancela;

    private LinearLayout menu_confirma;
    private LinearLayout menu_sel;
    private TextView entrada_qtde;
    private Spinner entrada_unidade;
    private  TextView confirma_nome;
    private TextView confirma_antes;
    private TextView confirma_depois;
    private InstIngrediente ingrResultante = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    protected void setContentView(){
        setContentView(R.layout.leitor_barras_popup_seleciona);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
      //  getWindow().setLayout(width, (int) (height * 0.8));
        getSupportActionBar().hide();
        menu_confirma = (LinearLayout) findViewById(R.id.barras_entrada_menuconfirma);
        menu_sel = (LinearLayout) findViewById(R.id.barras_entrada_menusel);
        confirma_antes = (TextView) findViewById(R.id.barras_textview_result_antes);
        confirma_depois = (TextView) findViewById(R.id.barras_textview_result_depois);
        confirma_nome = (TextView) findViewById(R.id.barras_textview_result_nome);
        entrada_qtde = (TextView) findViewById(R.id.barras_entrada_qtde);
        entrada_unidade = (Spinner) findViewById(R.id.barras_entrada_unidade);


        menu_confirma.setVisibility(View.GONE);
        btn_seleciona = (Button) findViewById(R.id.barras_btn_sel);
        btn_cancela = (Button) findViewById(R.id.barras_btn_cancela);
        btn_confirma = (Button) findViewById(R.id.barras_btn_confirma);


        btn_seleciona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(searchView.getQuery().toString());
            }
        });
        btn_confirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
        btn_cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });


    }

    public void select(String query){
        if(query.trim().isEmpty()) return;
        FBEstoqueConfirmaAdapter fb = (FBEstoqueConfirmaAdapter) this.mAdapter;
        fb.btnSel(query);
        mAdapter.filter(query, mDatabase);

        Double qtdeDbl = new Double(-1);
        String unStr = "";
        if (entrada_unidade.getSelectedItem() != null) {
            unStr = entrada_unidade.getSelectedItem().toString();
        }
        try{
            qtdeDbl = Double.parseDouble(entrada_qtde.getText().toString());
        } catch( NumberFormatException ex) {
            ex.printStackTrace();
        }
        ingrResultante = new InstIngrediente(query,qtdeDbl,unStr);
    }
    public void confirm(){
        super.finish();
    }
    public void cancel(){
        ingrResultante = null;
        menu_sel.setVisibility(View.VISIBLE);
        getRecyclerView().setVisibility(View.VISIBLE);
        menu_confirma.setVisibility(View.GONE);
    }



    public void onSelectBtnSearchFinished(String query, List<InstIngrediente> res){
        query = query.trim().toUpperCase();
        InstIngrediente found = null;
        for (InstIngrediente ingr : res) {
            if (ingr.nome.trim().toUpperCase().equals(query)) {
                found = ingr;
            }
        }
        menu_sel.setVisibility(View.GONE);
        getRecyclerView().setVisibility(View.GONE);
        menu_confirma.setVisibility(View.VISIBLE);
        this.confirma_nome.setText(query);
        if (found != null) {
            this.confirma_antes.setText( Double.toString(found.qtde) + " " + found.unidade);
            this.confirma_depois.setText( Double.toString(found.qtde) + " " + found.unidade);

        }
        ingrResultante = found;

    }

    @Override
    protected RecyclerView getRecyclerView() {
        return (RecyclerView) findViewById(R.id.barras_entrada_rview);
    }
    @Override
    protected void initAdapter(){
        this.mAdapter = new FBEstoqueConfirmaAdapter(this,this);
        mAdapter.filter("", mDatabase);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) findViewById(R.id.barras_entrada_search);
        searchView.setSearchableInfo
                (searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.requestFocus();


        return true;
    }

}
