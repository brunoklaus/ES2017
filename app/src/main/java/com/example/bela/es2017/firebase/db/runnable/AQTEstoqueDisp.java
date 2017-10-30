package com.example.bela.es2017.firebase.db.runnable;

import android.os.AsyncTask;
import android.util.Pair;

import com.example.bela.es2017.conversor.Conversor;
import com.example.bela.es2017.conversor.FBUnidadeConversorPreload;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.result.FBResult;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.example.bela.es2017.helpers.StringHelper;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

import me.xdrop.fuzzywuzzy.FuzzySearch;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Created by klaus on 28/10/17.
 */

public class AQTEstoqueDisp {

    List<InstIngrediente> estoque;
    List<Receita> receitas;
    Searcher<Receita> s;
    DatabaseReference mDatabase;
    String uid;
    public AQTEstoqueDisp(Searcher<Receita> adapter) {
        estoque = new ArrayList<>();
        receitas = new ArrayList<>();
        s = adapter;

        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
       this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public void go(){
        mDatabase.child("users").child(uid).
                child("estoque")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            estoque.add(snapshot.getValue(InstIngrediente.class));
                        }
                        classificaReceitasA();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }



    int localScore;
    int maxEst, maxScoreIngr;
    final HashMap<Receita,Double> score = new HashMap<Receita, Double>();
    final HashMap<InstIngrediente,InstIngrediente> mapToEstoque = new HashMap<>();
    private static final int CUTOFF_RECEITA_SCORE = 50;

    private void classificaReceitasA(){
        mDatabase.child("receitas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Receita r = snapshot.getValue(Receita.class);
                    int scoreReceita = 0;
                    if (r.ingredientesUsados == null || r.ingredientesUsados.isEmpty()) {
                        score.put(r,0.0);
                        continue;
                    }
                    boolean achouIngredientes = true;
                    for (InstIngrediente usado : r.ingredientesUsados) {
                        int maxScoreIngr = -1;
                        InstIngrediente maxEst = estoque.get(0);
                        for (InstIngrediente est : estoque) {
                            localScore = (FuzzySearch.partialRatio(usado.nome, est.nome)
                                        + FuzzySearch.ratio(usado.nome, est.nome))/2;
                            if (maxScoreIngr < localScore) {
                                maxEst = est;
                                maxScoreIngr = localScore;
                            }

                        }
                        if (maxScoreIngr < CUTOFF_RECEITA_SCORE) {
                            achouIngredientes = false;
                            break;
                        }
                        mapToEstoque.put(usado, maxEst);
                        scoreReceita += maxScoreIngr;
                    }
                    double avgScore = scoreReceita / r.ingredientesUsados.size();
                    if (achouIngredientes) {
                        score.put(r, avgScore);
                        receitasValidas.add(r);
                    }
                }
                classificaReceitasB();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void incrementReceitaScore(Receita r, int inc){
        double prev = (score.get(r) == null)  ? 0 : score.get(r).intValue();
        score.put(r,prev + inc);
    }
    HashMap<Pair<Receita, InstIngrediente>,Double> convMap = new HashMap<>();
    boolean canFinishConvSearch = false;
    int convSearchesLeft = 0;
    HashSet<Receita> receitasValidas = new HashSet<>();
    private void classificaReceitasB(){
        List<Receita> receitaList = new ArrayList<>(score.keySet());
        for (int i = 0; i < receitaList.size(); i++) {
            Receita r = receitaList.get(i);
            for (InstIngrediente ingr : r.ingredientesUsados) {

                InstIngrediente noEstoque = mapToEstoque.get(ingr);
                if (noEstoque == null) {
                    throw new IllegalStateException("Faltou entrada no hashmap");
                }

                if (i == receitaList.size() - 1) {
                    canFinishConvSearch = true;
                }
                final Pair<Receita, InstIngrediente> p = new Pair<>(r,ingr);
                if (ingr.unidade != null && noEstoque.unidade != null) {
                    convSearchesLeft++;
                    FBUnidadeConversorPreload conv = new FBUnidadeConversorPreload();
                    conv.findConv(ingr.unidade, noEstoque.unidade, ingr.nome, new Searcher<Double>() {
                        @Override
                        public void onSearchFinished(String query, List<Double> results, QueryRunnable<Double> q, boolean update) {
                            if (!results.isEmpty()) {
                                convMap.put(p, results.get(0).doubleValue());
                            }
                            convSearchesLeft--;
                            if (convSearchesLeft == 0 && canFinishConvSearch) {
                                classificaReceitasC();
                            }
                        }
                    });
                }
            }
        }
    }


    private void classificaReceitasC(){
        for (Pair<Receita,InstIngrediente> p : convMap.keySet()) {
            InstIngrediente noEstoque = mapToEstoque.get(p.second);
            if (noEstoque.qtde < convMap.get(p).doubleValue()) {
                //Temos no estoque mas eh qtde menor
                if (receitasValidas.contains(p.first)) {
                    receitasValidas.remove(p.first);
                }
            }
        }
        ArrayList<Receita> l = new ArrayList<Receita>(receitasValidas);
        Collections.sort(l, new Comparator<Receita>() {
            @Override
            public int compare(Receita receita, Receita t1) {
                return -score.get(receita).compareTo(score.get(t1));
            }
        });
        this.s.onSearchFinished("", l,null,true);
    }


}



