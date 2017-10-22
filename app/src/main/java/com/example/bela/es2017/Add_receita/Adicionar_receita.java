package com.example.bela.es2017.Add_receita;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bela.es2017.MainActivity;
import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.ReceitaComPasso;
import com.google.firebase.auth.FirebaseAuth;

public class Adicionar_receita extends FragmentActivity {
    /*
    private EditText ingrediente;
    private EditText modo_prepato;
    private EditText tempo_preparo;
    private EditText dificuldade;*/
    Button continuar;
    int status = 0;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ReceitaComPasso minhaReceita;

    @Override
    public void onBackPressed() {
        status--;
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_receita);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        minhaReceita = new ReceitaComPasso();
        minhaReceita.autor = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        minhaReceita.img = -1;


        continuar = (Button)findViewById(R.id.button_continuar);

        //inicia a activity com o fragment1
        Fragment_adicionar_receita0 fragment0 = new Fragment_adicionar_receita0();
        if(null == savedInstanceState) {
            fragmentTransaction.replace(R.id.fragment_container, fragment0);
        }
        fragmentTransaction.commit();
        continuar.setText("Continuar");
        status = 0;

        continuar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                if (status == 0) {
                    Fragment_adicionar_receita1 fragment1 = new Fragment_adicionar_receita1();
                    fragmentTransaction.replace(R.id.fragment_container, fragment1);
                    fragmentTransaction.addToBackStack("0");
                    fragmentTransaction.commit();
                    continuar.setText("Continuar");
                }
                else if(status == 1){
                    Fragment_adicionar_receita2 fragment2 = new Fragment_adicionar_receita2();
                    fragmentTransaction.replace(R.id.fragment_container, fragment2);
                    fragmentTransaction.addToBackStack("1");
                    fragmentTransaction.commit();
                    continuar.setText("Continuar");
                } else if (status == 2) {
                    Fragment_adicionar_receita3 fragment3 = new Fragment_adicionar_receita3();
                    fragmentTransaction.replace(R.id.fragment_container, fragment3);
                    fragmentTransaction.addToBackStack("2");
                    fragmentTransaction.commit();
                    continuar.setText("Adicionar");
                } else if (status == 3){
                    Intent it = new Intent(Adicionar_receita.this, MainActivity.class);
                    startActivity(it);
                }
                status++;
            }

        });

        /*
        ingrediente = (EditText)findViewById(R.id.editText_ingrediente);
        modo_prepato = (EditText)findViewById(R.id.editText_preparo);
        tempo_preparo = (EditText)findViewById(R.id.editText_tempo);
        dificuldade = (EditText)findViewById(R.id.editText_dificuldade);

        //os hints nao aparecem...desisto
        ingrediente.setHint("oi");
        modo_prepato.setHint("tchau"); */
    }

}
