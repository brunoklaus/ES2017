package com.example.bela.es2017;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class Estoque extends AppCompatActivity {

    private FrameLayout frameLayout;
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fab_cod_barras;
    private FloatingActionButton fab_selecao_itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {

            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(240);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });

        fab_cod_barras = (FloatingActionButton) findViewById(R.id.fab_codigo_barras);
        fab_selecao_itens = (FloatingActionButton) findViewById(R.id.fab_selecao_itens);

        fab_cod_barras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1 = new Intent(Estoque.this, Codigo_de_barras.class);
                startActivity(it1);
            }
        });

        fab_selecao_itens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it2 = new Intent(Estoque.this, Ingredientes.class);
                Bundle b = new Bundle();
                it2.putExtras(b);
                startActivity(it2);
            }
        });

    }
}
