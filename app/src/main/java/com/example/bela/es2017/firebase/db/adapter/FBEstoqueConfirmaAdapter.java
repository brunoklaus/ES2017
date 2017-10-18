package com.example.bela.es2017.firebase.db.adapter;

import android.content.Context;

import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.RecebeSeleciona;

import java.util.List;

/**
 * Created by klaus on 15/10/17.
 */

public class FBEstoqueConfirmaAdapter extends FBEstoqueAdapter {
    String btn_sel_str = "";
    boolean btn_sel = false;
    InstIngrediente btn_sel_result = null;
    RecebeSeleciona<InstIngrediente> rc;


    public FBEstoqueConfirmaAdapter(Context context, RecebeSeleciona<InstIngrediente> rc)
    {
        super(context);
        this.rc = rc;

    }

    public void btnSel(String str){
        this.btn_sel_str = str;
        this.btn_sel = true;
    }



    @Override
    public void onSearchFinished(String input, List<InstIngrediente> results, QueryRunnable<InstIngrediente> q, boolean update){
        if (btn_sel && input.trim().equals(btn_sel_str.trim()) && update) {
            this.btn_sel = false;
            rc.onSelectBtnSearchFinished(input,results);
        }
        this.onQueryFinished(results,q,update);
    }



}
