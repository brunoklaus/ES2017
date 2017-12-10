package com.example.bela.es2017.firebase.db.adapter;

import android.content.Context;

import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.RecebeSeleciona;

import java.util.List;

/**
 * Versao de EstoqueAdapter que faz callback em um @link{RecebeSeleciona} quando acaba a busca.
 * Created by klaus on 15/10/17.
 * @see {com.example.bela.es2017.firebase.db.adapter.FBEstoqueAdapter}
 */

public class FBEstoqueConfirmaAdapter extends FBEstoqueAdapter {
    String btn_sel_str = "";
    boolean btn_sel = false;    //Verdadeiro quando estamos pesquisando
    InstIngrediente btn_sel_result = null;
    RecebeSeleciona<InstIngrediente> rc;

    /**
     * Construtor
     * @param context contexto
     * @param rc
     */
    public FBEstoqueConfirmaAdapter(Context context, RecebeSeleciona<InstIngrediente> rc)
    {
        super(context);
        this.rc = rc;

    }
    /*
    *   Guarda a string a ser selecionada e indica que estamos pesquisando
     */
    public void btnSelSetOptions(String str){
        this.btn_sel_str = str;
        this.btn_sel = true;
    }


    /**
     * Quando terminada a busca, tambem chamamos callback indicando que a busca que se originou
     * ao se clicar no botao Select acabou
     */
    @Override
    public void onSearchFinished(String input, List<InstIngrediente> results, QueryRunnable<InstIngrediente> q, boolean update){
        if (btn_sel && input.trim().equals(btn_sel_str.trim()) && update) {
            this.btn_sel = false;
            rc.onSelectBtnSearchFinished(input,results);
        }
        this.onQueryFinished(results,q,update);
    }



}
