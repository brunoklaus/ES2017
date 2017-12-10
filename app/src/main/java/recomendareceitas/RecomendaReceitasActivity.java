package recomendareceitas;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bela.es2017.R;
import com.example.bela.es2017.SideBarActivity;
import com.example.bela.es2017.SideBarInfo;
import com.example.bela.es2017.firebase.db.adapter.FBReceitasAdapter;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.example.bela.es2017.helpers.FBInsereReceitas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by klaus on 10/12/17.
 */

public class RecomendaReceitasActivity extends SideBarActivity {
    private RecyclerView rView;
    private FBReceitasAdapter adapter;

    List<Receita> recom =
            Collections.synchronizedList(new ArrayList<Receita>());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rView = (RecyclerView) findViewById(R.id.rView);
        adapter = new FBReceitasAdapter(this);
        rView.setAdapter(adapter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rView.setLayoutManager(layout);
        //Le receitas recomendadas
        FBInsereReceitas.leReceitasRecomendadas(FirebaseAuth.getInstance().getCurrentUser(),
                FirebaseDatabase.getInstance().getReference(), new Searcher<ArrayList<String>>() {
                    @Override
                    public void onSearchFinished(String query, List<ArrayList<String>> results, QueryRunnable<ArrayList<String>> q, boolean update) {
                        if (results == null || results.isEmpty()) {
                            throw new IllegalArgumentException("Procura por receitas recomendadas " +
                                    "retornou formato inesperado");
                        }
                        procuraReceitasPeloId(results.get(0));
                    }
                });


    }

    private void procuraReceitasPeloId(List<String> listaIds){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        for (String str : listaIds) {
            final String rec = str;
            ref.child("receitas").orderByKey().equalTo(rec).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        adapter.addToModel(snap.getValue(Receita.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected SideBarInfo getInfo() {
        return new SideBarInfo("EasyFeed - Recomendações", R.layout.activity_recomenda_receita);
    }


}
