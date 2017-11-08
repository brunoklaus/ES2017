package visualizapasso;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bela.es2017.Estoque.Estoque;
import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.Passo;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;

/**
 * Created by klaus on 30/10/17.
 */

public class EstoqueMatcherFragment extends  Fragment{

    private static String ARG_RECEITA = "receita";
    private Receita r;

    private List<InstIngrediente> estoque;
    private RecyclerView rView;






        /**
         * Factory method for this fragment class. Constructs a new fragment for the given page number.
         */
        public static EstoqueMatcherFragment create(Receita r) {
            EstoqueMatcherFragment fragment = new EstoqueMatcherFragment();
            Bundle args = new Bundle();
            Gson g = new Gson();
            args.putString(ARG_RECEITA, g.toJson(r));
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments().getString(ARG_RECEITA) == null) {
                throw new IllegalStateException("fragment de visualizacao " +
                        "nao recebeu receita");
            }
            r = (Receita) new Gson().fromJson(getArguments().getString(ARG_RECEITA), Receita.class);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout containing a title and body text.
            ViewGroup rootView = (ViewGroup) inflater
                    .inflate(R.layout.fragment_estoquematcher, container, false);

            rView = (RecyclerView) rootView.findViewById(R.id.estoquematcher_rview);
            final EstoqueMatcherAdapter adapter = new EstoqueMatcherAdapter(r.ingredientesUsados,getContext());
            rView.setAdapter(adapter);
            RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rView.setLayoutManager(layout);
            EstoqueFinder finder = new EstoqueFinder();
            finder.getEstoque(new Searcher<InstIngrediente>() {
                @Override
                public void onSearchFinished(String query, List<InstIngrediente> results, QueryRunnable<InstIngrediente> q, boolean update) {
                    estoque = results;
                    if (!results.isEmpty()) {
                        for (InstIngrediente ingrReceita : r.ingredientesUsados) {
                            double maxScore = 0.0;
                            String ingrNome = ingrReceita.nome;
                            InstIngrediente maxEst = results.get(0);
                            for (InstIngrediente inst : results) {
                                double score = FuzzySearch.partialRatio(ingrNome, inst.nome) +
                                        FuzzySearch.ratio(ingrNome, inst.nome);
                                score *= 0.5;
                                if (score > maxScore) {
                                    maxScore = score;
                                    maxEst = inst;
                                }
                            }
                            if (maxScore > 70.0) {
                                adapter.addMap(ingrNome, maxEst);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            return  rootView;

        }

    }



