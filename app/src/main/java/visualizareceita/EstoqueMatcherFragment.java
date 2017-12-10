package visualizareceita;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.google.gson.Gson;

import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;

/**
 * Fragment que calcula correspondencia entre os ingredientes de uma receita (recebida por um argumento
 * do Bundle} com os ingredientes do estoque
 * no estoque do usuario
 * Created by klaus on 30/10/17.
 */

public class EstoqueMatcherFragment extends  Fragment{

    //Argumento em que se salva a receita
    private static String ARG_RECEITA = "receita";

    // A receita  cujos ingredientes serao comparados com o estoque
    private Receita r;

    //O estoque do usuario
    private List<InstIngrediente> estoque;

    //RecyclerView para mostrar a lista de correspondencias
    private RecyclerView rView;


    /**
     * Cria uma instancia desse Fragment para uma dada receita. Esse metodo eh o que deve ser
     * chamado para criar uma instancia desse fragment.
     * @pre
     * @param r a
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

    /**
     * Calcula similaridade entre as duas strings
     * @param a Receita #1
     * @param b Receita #2
     * @return similaridade entre 0 e 1
     */
        public static double getScore(InstIngrediente a, InstIngrediente b) {
            return 0.5 * (FuzzySearch.partialRatio(a.nome, b.nome) +
                    FuzzySearch.ratio(a.nome, b.nome));
        }

    /**
     * Calcula o elemento do estoque com nome mais similar ao do ingrediente
     * @param estoqueObtido o estoque do usuario
     * @param query ingrediente
     * @return elemento de {@code estoqueObtido} com nome mais similar a {@code query}
     */
        public static InstIngrediente matchEstoque(List<InstIngrediente> estoqueObtido, InstIngrediente query){
            double maxScore = 0.0;
            InstIngrediente maxEst = estoqueObtido.get(0);
            for (InstIngrediente inst : estoqueObtido) {
                double score = getScore(query, inst);
                if (score > maxScore) {
                    maxScore = score;
                    maxEst = inst;
                }
            }
            return maxEst;
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

            //Usa EstoqueFinder para obter estoque do usuario
            EstoqueFinder finder = new EstoqueFinder();
            finder.getEstoque(new Searcher<InstIngrediente>() {
                @Override
                public void onSearchFinished(String query, List<InstIngrediente> results, QueryRunnable<InstIngrediente> q, boolean update) {
                    estoque = results;
                    if (!results.isEmpty()) {
                        for (InstIngrediente ingrReceita : r.ingredientesUsados) {
                            InstIngrediente maxEst = matchEstoque(results,ingrReceita);
                            if (getScore(ingrReceita, maxEst) > 70.0) {
                                adapter.addMap(ingrReceita.nome, maxEst);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            return  rootView;

        }

    }



