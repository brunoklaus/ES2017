package com.example.bela.es2017.firebase.db.runnable;

import android.util.Pair;

import com.example.bela.es2017.conversor.FBUnidadeConversorPreload;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.searcher.Searcher;
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

import visualizareceita.EstoqueMatcherFragment;

/**Classe que
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

    /** Conjunto que mapeia a receita ao seu score quanto a similaridade com o estoque
     * do usuario
     */
    final HashMap<Receita,Double> score = new HashMap<Receita, Double>();
    /**  Conjunto de  {@code (a,b)} : {@code a} eh um ingrediente encontrada na receita,
     * e  {@code b} eh o melhor match no estoque do usuario*/
    final HashMap<InstIngrediente,InstIngrediente> mapToEstoque = new HashMap<>();
    private static final int CUTOFF_RECEITA_SCORE = 30;
    private static final int CUTOFF_INGR_SCORE = 70;
    private static final int FOUND_INGR_REWARD = 100;




    private void classificaReceitasA(){
        mDatabase.child("receitas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Receita r = snapshot.getValue(Receita.class);
                    int scoreReceita = 0;
                    if (r.ingredientesUsados == null || r.ingredientesUsados.isEmpty()
                    || r.passos == null) {
                        score.put(r,0.0);
                        continue;
                    }
                    boolean achouIngredientes = true;
                    for (InstIngrediente usado : r.ingredientesUsados) {
                        InstIngrediente maxEst = EstoqueMatcherFragment.matchEstoque(estoque,usado);
                        double maxScoreIngr = EstoqueMatcherFragment.getScore(usado,maxEst);
                        if (maxScoreIngr < CUTOFF_INGR_SCORE) {
                            //achouIngredientes = false;
                            maxScoreIngr = 0;
                        } else {
                            maxScoreIngr = FOUND_INGR_REWARD;
                        }
                        mapToEstoque.put(usado, maxEst);
                        scoreReceita += maxScoreIngr;
                    }
                    double avgScore = scoreReceita / r.ingredientesUsados.size();
                    if (avgScore >= CUTOFF_RECEITA_SCORE) {
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
        List<Receita> receitaList = new ArrayList<>();
        for (Receita r : score.keySet()) {
            if (score.get(r) > CUTOFF_RECEITA_SCORE) {
                receitaList.add(r);
            }
        }

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
                    score.put(p.first,
                            (score.get(p.first) - 0.5*FOUND_INGR_REWARD) /
                                    (p.first.ingredientesUsados.size())
                            );
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



