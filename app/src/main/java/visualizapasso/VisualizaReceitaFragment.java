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
import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.Passo;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

/**
 * Created by klaus on 30/10/17.
 */

public class VisualizaReceitaFragment extends Fragment {

    private static String ARG_RECEITA = "receita";
    private Receita r;

    private ImageView iv;
    private TextView autor;
    private TextView porcoes;
    private TextView dificuldade;
    private TextView descr;
    private RecyclerView ingredientes;
    private TextView passos;





    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static VisualizaReceitaFragment create(Receita r) {
        VisualizaReceitaFragment fragment = new VisualizaReceitaFragment();
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
                .inflate(R.layout.fragment_visualiza_receita, container, false);

        iv = (ImageView) rootView.findViewById(R.id.vrec_iv);
        autor = (TextView) rootView.findViewById(R.id.vrec_tv_autor_2);
        porcoes = (TextView) rootView.findViewById(R.id.vrec_tv_servings_2);
        dificuldade = (TextView) rootView.findViewById(R.id.vrec_tv_dificuldade_2);
        descr = (TextView) rootView.findViewById(R.id.vrec_tv_descr_2);
        ingredientes = (RecyclerView) rootView.findViewById(R.id.vrec_tv_ingr_2);
        passos = (TextView) rootView.findViewById(R.id.vrec_tv_preparo_2);

        if (r.imgStorage != null){
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(r.imgStorage);
            // Load the image using Glide
            Glide.with(getContext().getApplicationContext())
                    .using(new FirebaseImageLoader())
                    .load(ref)
                    .into(iv);
        }

        autor.setText(r.autor);
        if(r.servings != null) {
            porcoes.setText(r.servings.toString());
        }
        dificuldade.setText(r.difficulty.toString());
        descr.setText(r.descr);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ingredientes.setLayoutManager(layout);
        ingredientes.setAdapter(new SimpleInstIngrAdapter(r.ingredientesUsados,getContext()));

        String passoStr = "";
        for (Passo p : r.passos) {
            passoStr += p.descr + "\n\n";
        }
        passos.setText(passoStr);


        return  rootView;

    }

}



