/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package visualizareceita;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bela.es2017.MainActivity;
import com.example.bela.es2017.R;
import com.example.bela.es2017.SideBarActivity;
import com.example.bela.es2017.SideBarInfo;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.example.bela.es2017.helpers.FBInsereReceitas;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity que consiste na visualizacao de uma dada receita. O fluxo dessa activity consiste
 * na troca de fragments por meio de um tablayout, utilizando para isso um {@link ViewPager}
 * para trocar entre fragments.
 * @see FPasso
 * @see FResumo
 * @see EstoqueMatcherFragment
 */
public class VisualizaReceitaActivity extends SideBarActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private int NUM_PAGES;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    /** A receita cujos passos serao mostrados **/
    private Receita r;

    /** Fragment para visualizar resumo da receita*/
    private Fragment fragVisReceita;
    /** Fragment para visualizar correspondencia de ingredientes no estoque **/
    private Fragment fragIngredientes;
    /** Fragment para visualizar os passos da receita **/
    private Fragment fragPasso;







    @Override
    protected SideBarInfo getInfo(){
        return new SideBarInfo(null,R.layout.activity_screen_slide);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String target = getIntent().getStringExtra("MyObjectAsString");
        if (target == null) {
            throw new IllegalArgumentException("Precisa da receita como Gson");
        }
        Gson g = new Gson();
        r = (Receita) g.fromJson(target, Receita.class);



        //Criar fragments
         fragVisReceita = FResumo.create(r);
        fragIngredientes = EstoqueMatcherFragment.create(r);
        fragPasso = FListaPassos.create(r);


        ImageView image = (ImageView) findViewById(R.id.image);

        if (r.imgStorage != null){
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(r.imgStorage);
            // Load the image using Glide
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(ref)
                    .into(image);
        } else {
            image.setMaxHeight(0);
        }

        NUM_PAGES = r.passos.size();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });
        final Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar3);
        int cord[] = new int [2];
        actionBar.getLocationInWindow (cord);
        mPager.setY(cord[1] + actionBar.getHeight());


        // Specify that tabs should be displayed in the action bar.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mPager);

        updateInfo(new SideBarInfo(r.titulo,R.layout.activity_lista));


    }

    @Override
    public void onBackPressed() {
    super.onBackPressed();


    }

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static Intent getIntentTo(Context c, Receita r) {
        Intent i = new Intent(c, VisualizaReceitaActivity.class);
        Gson g = new Gson();
        i.putExtra("MyObjectAsString", g.toJson(r, Receita.class));
        return i;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? "finish"
                        : "next");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A simple pager adapter that represents 5 {@link FPasso} objects, in
     * sequence.
     */
    private class TabPagerAdapter extends FragmentStatePagerAdapter {
        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int passo) {
            if (passo == 0) {
                return fragVisReceita;
            } else if (passo == 1) {
                return fragIngredientes;
            }

            return fragPasso;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int pos) {
            if (pos == 0) {
                return "Resumo";
            } else if (pos == 1) {
                return "Ingredientes";
            }
            return "Passos";
        }

    }
}
