package com.example.bela.es2017.Add_receita;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bela.es2017.MainActivity;
import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.helpers.FBInsereReceitas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Adicionar_receita extends Activity {
    /*
    private EditText ingrediente;
    private EditText modo_prepato;
    private EditText tempo_preparo;
    private EditText dificuldade;*/
    Button continuar;
    int status = 0;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Receita minhaReceita;
    private static String MSG_CONTINUA = "Continuar";
    private static String MSG_FINALIZA = "Finalizar";
    private static String MSG_ESPERA = "Espere...";

    public static boolean PODE_CRIAR_RECEITA_NO_FIREBASE = true;
    boolean isUploading = false;
    Adicionar_receita activityRef = this;

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            status--;
            continuar.setBackgroundColor(ContextCompat.getColor(this,android.R.color.holo_blue_light));
            continuar.setText("Continuar");
        } else {
            super.onBackPressed();
        }

    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_receita);
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        minhaReceita = new Receita();
        minhaReceita.autor = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        minhaReceita.img = -1;


        continuar = (Button)findViewById(R.id.button_continuar);

        //inicia a activity com o fragment1
        Fragment_adicionar_receita1 fragment0 = new Fragment_adicionar_receita1();
        if(null == savedInstanceState) {
            fragmentTransaction.replace(R.id.fragment_container, fragment0);
        }
        fragmentTransaction.commit();
        continuar.setText(MSG_CONTINUA);
        continuar.setBackgroundColor(ContextCompat.getColor(this,android.R.color.holo_blue_light));
        status = 1;

        continuar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                if (status == 1) {

                    Fragment_adicionar_receita1 oldFrag =
                            (Fragment_adicionar_receita1) getFragmentManager().
                                    findFragmentById(R.id.fragment_container);
                    if (!processFrag1Res(oldFrag)) return;

                        Fragment_adicionar_receita2 fragment2 = new Fragment_adicionar_receita2();
                        fragmentTransaction.replace(R.id.fragment_container, fragment2);
                        fragmentTransaction.addToBackStack("0");
                        fragmentTransaction.commit();
                        continuar.setText(MSG_CONTINUA);


                }
                else if(status == 2){
                    Fragment_adicionar_receita2 oldFrag =
                            (Fragment_adicionar_receita2) getFragmentManager().
                                    findFragmentById(R.id.fragment_container);
                    if (!processFrag2Res(oldFrag)) return;

                        Fragment_adicionar_receita3 fragment3 = new Fragment_adicionar_receita3();
                        fragmentTransaction.replace(R.id.fragment_container, fragment3);
                        fragmentTransaction.addToBackStack("1");
                        fragmentTransaction.commit();
                        continuar.setText(MSG_CONTINUA);


                } else if (status == 3) {
                    Fragment_adicionar_receita3 oldFrag =
                            (Fragment_adicionar_receita3) getFragmentManager().
                                    findFragmentById(R.id.fragment_container);
                    if (!processFrag3Res(oldFrag)) return;

                        Fragment_adicionar_receita4 fragment4 = new Fragment_adicionar_receita4();
                        fragmentTransaction.replace(R.id.fragment_container, fragment4);
                        fragmentTransaction.addToBackStack("2");
                        fragmentTransaction.commit();
                        continuar.setText(MSG_CONTINUA);

                } else if (status == 4){
                    Fragment_adicionar_receita4 oldFrag =
                            (Fragment_adicionar_receita4) getFragmentManager().
                                    findFragmentById(R.id.fragment_container);
                    if (!processFrag4Res(oldFrag)) return;

                    continuar.setText(MSG_FINALIZA);
                    continuar.setBackgroundColor(ContextCompat.getColor(Adicionar_receita.super.getApplicationContext(),
                            android.R.color.holo_green_dark));
                    Fragment_adicionar_receita5 fragment5 = new Fragment_adicionar_receita5();
                    fragmentTransaction.replace(R.id.fragment_container, fragment5);
                    fragmentTransaction.addToBackStack("2");
                    fragmentTransaction.commit();
                } else if (status == 5){
                    if(isUploading) return;
                    else isUploading = true;
                    Fragment_adicionar_receita5 oldFrag =
                            (Fragment_adicionar_receita5) getFragmentManager().
                                    findFragmentById(R.id.fragment_container);
                    continuar.setText(MSG_ESPERA);
                    Uri uri= oldFrag.toBeUploaded;
                    if(uri == null)

                    FBInsereReceitas.insereReceita(activityRef,FirebaseDatabase.getInstance().getReference(),
                                minhaReceita,uri ,true);



                    return;
                }


                status = status + 1;

            }

        });
    }

    boolean processFrag1Res(Fragment_adicionar_receita1 f){
        this.minhaReceita.subtitulo = f.getDescricao();
        this.minhaReceita.titulo = f.getNome();
        if (minhaReceita.titulo.isEmpty()) {
            Toast.makeText(this, "Nome da receita nao pode ser vazio", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    boolean processFrag2Res(Fragment_adicionar_receita2 f){
        minhaReceita.ingredientesUsados = f.getIngredientes();
        if (minhaReceita.ingredientesUsados.isEmpty()){
            Toast.makeText(this, "Precisa de ao menos 1 ingrediente", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    boolean processFrag3Res(Fragment_adicionar_receita3 f){
        minhaReceita.passos = f.getPassos();
        if (minhaReceita.passos.isEmpty()){
            Toast.makeText(this, "Precisa de ao menos 1 ingrediente", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    boolean processFrag4Res(Fragment_adicionar_receita4 f){
        int  step = 0;
        try {

            this.minhaReceita.difficulty = f.getDificuldade(); step++;
            this.minhaReceita.servings = f.getPorcoes(); step++;
            this.minhaReceita.tags = f.getTags();step++;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Erro no formato do numero de porcoes", Toast.LENGTH_SHORT).show();
            return false;
        }
        catch (Exception ex) {
            String[] l = {"dificulade","porcoes","tags"};
            Toast.makeText(this, "Erro na entrada do campo " + l[step], Toast.LENGTH_SHORT).show();
            return false;
        }

    return true;
    }

    public void onUploadResult(boolean successful){
        this.isUploading = false;
        if(true) {
            Intent it = new Intent(Adicionar_receita.this, MainActivity.class);
            startActivity(it);
        } else {
            if (continuar.getText().equals(MSG_ESPERA)){
                continuar.setText(MSG_FINALIZA);
            }
        }
    }


}
