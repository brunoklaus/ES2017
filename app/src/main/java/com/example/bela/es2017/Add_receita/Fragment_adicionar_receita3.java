package com.example.bela.es2017.Add_receita;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.Passo;

import java.util.ArrayList;
import java.util.Stack;

public class Fragment_adicionar_receita3 extends Fragment  implements View.OnClickListener {

    public PassoEditText preparo;
    public LinearLayout add_container_preparo;
    public Button adicionarPrep;

    protected CheckBox clockCheckBox;
    protected View entradaTempo;
    protected  EditText entradaTempoHoras;
    protected EditText entradaTempoSegundos;
    protected EditText entradaTempoMinutos;
    protected ArrayList<Passo> passosGuardados;
    Stack<Button> buttons = new Stack<>();



    private View lastPassoView;
    private View v;

    public Fragment_adicionar_receita3() {
        // Required empty public constructor
    }

    private String getVisualizaPassoStr(Passo p){
        String res = p.descr;
        Integer d = p.duration;
        if(p.duration != null) {
            res +=  ". Duração : " ;
            if (d/3600 >= 1) {
                if (d/3600 == 1) {
                    res += "1 hora ";
                } else {
                    res += d/3600 + " horas ";
                }
                d -= 3600*(d/3600);
            }
            if (d/60 >= 1) {
                if (d/60 == 1) {
                    res += "1 minuto ";
                } else {
                    res += d/60 + " minutos ";
                }
                d -= 60*(d/60);
            } if (d >=  1) {
                if (d == 1) {
                    res += "1 segundo";
                } else {
                    res +=  d + " segundos";
                }
            }
        }
        return res;
    }

    public ArrayList<Passo> getPassos(){
        return passosGuardados;
    }
    private void criaEntrada(boolean updatePasso) {
        if(updatePasso) preparo.incrementPasso();
        preparo.setText(preparo.getNumPassoStr() + " ");
        preparo.setSelection(preparo.getText().length());
        if (clockCheckBox.isChecked()) clockCheckBox.performClick();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v != null){
            return v;
        }
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_adicionar_receitas3, container, false);
        v = view;
        //Verificar se eh a primeira vez que abrimos esse fragment

            //declarando views do layout do fragment2
            preparo = (PassoEditText) view.findViewById(R.id.editText_preparo);

            passosGuardados = new ArrayList<>();
            add_container_preparo = (LinearLayout) view.findViewById(R.id.add_container_preparo);

            //declarando botao
            adicionarPrep = (Button) view.findViewById(R.id.item_adicionar_preparo);
            adicionarPrep.setOnClickListener(this);

            //Obter checkbox e entrada em seg
            clockCheckBox = (CheckBox) view.findViewById(R.id.ingr_clockcheckbox);
            entradaTempo = (View) view.findViewById(R.id.entrada_tempo);
            entradaTempo.setVisibility(View.GONE);

            entradaTempoSegundos = (EditText) view.findViewById(R.id.entrada_tempo_etSegudo);
            entradaTempoMinutos = (EditText) view.findViewById(R.id.entrada_tempo_etMinuto);
            entradaTempoHoras = (EditText) view.findViewById(R.id.entrada_tempo_etHora);

            //Mudar o foco pro prox. apos digitar hora ou minuto
            entradaTempoHoras.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId== EditorInfo.IME_ACTION_DONE){
                        entradaTempoMinutos.requestFocus();
                    }
                    return false;
                }
            });
            entradaTempoMinutos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId== EditorInfo.IME_ACTION_DONE){
                        entradaTempoSegundos.requestFocus();
                    }
                    return false;
                }
            });


            //Adicionar listener no checkbox
            clockCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    entradaTempoSegundos.setText("");
                    entradaTempoMinutos.setText("");
                    CheckBox checkBox = (CheckBox) view;
                    if (!checkBox.isChecked()) {
                        entradaTempo.setVisibility(View.GONE);
                    } else {
                        entradaTempo.setVisibility(View.VISIBLE);
                    }
                }
            });
        criaEntrada(preparo.getPasso() == 0);
        if (!this.getPassos().isEmpty()) {

            LayoutInflater layoutInflater =
                    (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView_item = layoutInflater.inflate(R.layout.item_adicionar_receita_preparo, add_container_preparo, false);
            TextView prep = (TextView) addView_item.findViewById(R.id.editText_preparo_preenchido);
            Passo passoAdicionado = passosGuardados.get(passosGuardados.size() - 1);
            prep.setText(getVisualizaPassoStr(passoAdicionado));
            add_container_preparo.addView(addView_item);
        }



        return view;
    }

    @Override
    public void onClick(View view) {
        //no lugar de getActivity estava getBaseContext
        LayoutInflater layoutInflater =
                (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView_item = layoutInflater.inflate(R.layout.item_adicionar_receita_ingrediente, add_container_preparo, false);


        //BEGIN-\GUARDA O PASSO ADICIONADO
        try{

            Integer duration = null;
            if (clockCheckBox.isChecked()) {
                int seg = (entradaTempoSegundos.getText().toString().isEmpty()) ?
                        0 : Integer.parseInt(entradaTempoSegundos.getText().toString());
                int min = (entradaTempoMinutos.getText().toString().isEmpty()) ?
                        0 : Integer.parseInt(entradaTempoMinutos.getText().toString());
                int hor = (entradaTempoHoras.getText().toString().isEmpty()) ?
                        0 : Integer.parseInt(entradaTempoHoras.getText().toString());
                duration =  3600 * hor + 60 * min + seg;
                if (duration == 0) throw new IllegalArgumentException();
            }
            passosGuardados.add(new Passo(preparo.getText().toString(),
                   duration));
        } catch (Exception e) {
            Toast.makeText(getActivity(),"Erro: passo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView prep = (TextView) addView_item.findViewById(R.id.editText_ingredienteEsc);
        Passo passoAdicionado = passosGuardados.get(passosGuardados.size() - 1);
        prep.setText(getVisualizaPassoStr(passoAdicionado));
        //add_container_preparo.removeAllViews();
        add_container_preparo.addView(addView_item);
        criaEntrada(true);

        final Button newButton = (Button) addView_item.findViewById(R.id.ingrediente_cancela);
        if (!buttons.isEmpty()) {
            Button oldBtn = buttons.peek();
            oldBtn.setVisibility(View.GONE);
        }
        buttons.push(newButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passosGuardados.remove(passosGuardados.size()-1);
                Button b = buttons.pop();
                if (b != newButton) throw new IllegalStateException("Stack logic failed");
                add_container_preparo.removeView(addView_item);
                //Atualizar infromacoes
                preparo.decrementPasso();
                criaEntrada(false);


                b.setVisibility(View.GONE);
                if (!buttons.isEmpty()) {
                    buttons.peek().setVisibility(View.VISIBLE);
                }
            }
        });

    }

}
