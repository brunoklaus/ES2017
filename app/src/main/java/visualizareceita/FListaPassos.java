package visualizareceita;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.google.gson.Gson;

import static visualizareceita.FResumo.ARG_RECEITA;

/** Fragment que serve para alternar escolher entre os fragments de cada passo individual {@link FPasso}.
 * Utiliza um {@link ViewPager} para mudar o passo por meio de swipe horizontal.
 * Created by klaus on 09/12/17.
 * @see  FPasso
 */

public class FListaPassos extends Fragment {


    Receita r;
    ViewPager slidePager;
    PagerAdapter slideAdapter;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static FListaPassos create(Receita r) {
        FListaPassos fragment = new FListaPassos();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.content_listapasso, container, false);

        // Instantiate a ViewPager and a PagerAdapter.
        slidePager = (ViewPager) rootView.findViewById(R.id.slidepager);
        slideAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        slidePager.setAdapter(slideAdapter);
        slidePager.setCurrentItem(0);

        return rootView;
    }

/**
 * A simple pager adapter that represents 5 {@link FPasso} objects, in
 * sequence.
 */
        private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
            public ScreenSlidePagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int passo) {
                return FPasso.create(r,passo);
            }

            @Override
            public int getCount() {
                return r.passos.size();
            }

        }
}
