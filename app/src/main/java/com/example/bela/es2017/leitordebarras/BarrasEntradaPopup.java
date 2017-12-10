package com.example.bela.es2017.leitordebarras;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bela.es2017.R;
import com.example.bela.es2017.SideBarInfo;
import com.example.bela.es2017.conversor.Conversor;
import com.example.bela.es2017.firebase.db.adapter.FBEstoqueConfirmaAdapter;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.db.searchActivity.SearchActivity;
import com.example.bela.es2017.firebase.searcher.RecebeSeleciona;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.example.bela.es2017.helpers.FBInsereReceitas;
import com.example.bela.es2017.helpers.StringHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

/**
 * Created by klaus on 14/10/17.
 */

public class BarrasEntradaPopup extends SearchActivity implements RecebeSeleciona<InstIngrediente>,
        Searcher<InstIngrediente> {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    protected RecyclerView rView;

    protected Button btn_seleciona;
    protected Button btn_confirma;
    protected Button btn_cancela;
    protected String codigoDeBarras = null;
    protected LinearLayout menu_confirma;
    protected LinearLayout menu_sel;
    protected TextView entrada_qtde;
    protected TextView entrada_unidade;
    protected  TextView confirma_nome;
    protected TextView confirma_antes;
    protected TextView confirma_depois;
    protected TextView barrasEncontrado;
    protected InstIngrediente ingrResultante = null;
    protected InstIngrediente ingrAdicionado = null;
    protected boolean esperandoSelect = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        Bundle bundle = getIntent().getExtras();

        if (bundle == null || bundle.get("barras") == null ||
                bundle.get("found") == null) {
            throw new IllegalStateException("Popup nao recebeu argumentos suficientes no bundle");
        }

        //Pegar codigo de barras
        codigoDeBarras = bundle.get("barras").toString();

        //Verificar se achamos o codigo de barras no banco de dados
        if (bundle.get("found").equals("TRUE")) {
            barrasEncontrado.setText("Foi encontrado o código de barras no banco de dados.");
            //Seleciona nome
            searchView.setQuery(bundle.get("nome").toString(),true);
            //Seleciona qtde
            entrada_qtde.setText(bundle.get("qtde").toString());

            //Seleciona unidade
            String unidadeFound = bundle.get("unidade").toString();
            entrada_unidade.setText(unidadeFound);
        }

    }

    @Override
    protected SideBarInfo getInfo(){
        return new SideBarInfo("EasyFeed - Código de Barras",R.layout.leitor_barras_popup_seleciona);
    }

        protected void setContentView(){
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
        entrada_unidade = (TextView) findViewById(R.id.barras_entrada_unidade);
        barrasEncontrado = (TextView) findViewById(R.id.barras_editText_encontrado);
        searchView = (SearchView) findViewById(R.id.barras_entrada_search);
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
        if(esperandoSelect || query.trim().isEmpty()) return;

        Double qtdeDbl = new Double(0);
        try {
            qtdeDbl = StringHelper.parseQtde(entrada_qtde.getText().toString());
        } catch( NumberFormatException ex) {
            Toast.makeText(this,"quantidade invalida",Toast.LENGTH_LONG);
            ex.printStackTrace();
            return;
        }
        ingrResultante = new InstIngrediente(query,qtdeDbl,entrada_unidade.getText().toString());

        //Verifica se item existe no estoque
        esperandoSelect = true;
        FBEstoqueConfirmaAdapter fb = (FBEstoqueConfirmaAdapter) this.mAdapter;
        fb.btnSelSetOptions(query);
        mAdapter.filter(query, mDatabase);

    }
    public void confirm(){
        if (ingrAdicionado == null) throw new IllegalStateException("Ingrediente a ser associado " +
                "com o  banco de dados eh nulo");
        //Insere codigo de barras no banco de dados
        FBInsereReceitas.insereCodigoBarras(mDatabase,ingrAdicionado,codigoDeBarras,false);
        FBInsereReceitas.inserenoEstoque(FirebaseAuth.getInstance().getCurrentUser(),
                mDatabase,ingrResultante,false);
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

        ingrAdicionado = ingrResultante;
        if (found != null) {
            this.confirma_antes.setText(StringHelper.interpretQtde(found,13,true));
            Conversor.adicionaComFB(found, ingrResultante, this);
        } else {
            this.confirma_antes.setText( StringHelper.interpretQtde(ingrResultante,13,true));
            this.onSearchFinished("", Arrays.asList(ingrResultante),null,true);
        }

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
        searchView.setSearchableInfo
                (searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.requestFocus();


        return true;
    }

    @Override
    public void onSearchFinished(String query, List<InstIngrediente> results, QueryRunnable<InstIngrediente> q, boolean update) {
        esperandoSelect = false;
        if (results.isEmpty()) {
            Toast.makeText(this,"Firebase nao conseguiu achar uma conversao entre as unidades",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Firebase conseguiu achar uma conversao entre as unidades",
                    Toast.LENGTH_SHORT).show();
            ingrResultante = results.get(0);
            this.confirma_depois.setText( StringHelper.interpretQtde(ingrResultante,13,true));
            menu_sel.setVisibility(View.GONE);
            getRecyclerView().setVisibility(View.GONE);
            menu_confirma.setVisibility(View.VISIBLE);
            this.confirma_nome.setText(query);
        }
    }
}
