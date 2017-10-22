package com.example.bela.es2017.Add_receita;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.Passo;

import java.util.ArrayList;
import java.util.List;

import com.example.bela.es2017.R;

public class Fragment_adicionar_receita2 extends Fragment  implements View.OnClickListener {

    public EditText preparo;
    public LinearLayout add_container_preparo;
    public Button adicionarPrep;
    public int passo = 0;
    protected CheckBox clockCheckBox;
    protected EditText entradaSegundos;
    protected ArrayList<Passo> passosGuardados;
    private View lastPassoView;

    public Fragment_adicionar_receita2() {
        // Required empty public constructor
    }

    private String getNumPassoStr() {
        return Integer.toString(passo) + ". ";
    }
    private String getVisualizaPassoStr(Passo p){
        String res = p.descr;
        if(p.duration != null) res +=  ". Duração : " + p.duration + " segundos";
        return res;
    }

    public List<Passo> getPassos(){
        return passosGuardados;
    }
    private void criaEntrada(boolean updatePasso) {
        if(updatePasso) passo++;
        preparo.setText(getNumPassoStr() + " ");
        preparo.setSelection(preparo.getText().length());
        if (clockCheckBox.isChecked()) clockCheckBox.performClick();
    }

    private synchronized void corrigeRange(){
        String passoStr = getNumPassoStr();
        int start = preparo.getSelectionStart();
        int end = preparo.getSelectionEnd();
        if (passoStr.length() > start) start = passoStr.length();
        if (passoStr.length()  > end ) end = passoStr.length();

        preparo.setSelection(start, end);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_adicionar_receitas2, container, false);
        //Verificar se eh a primeira vez que abrimos esse fragment

            //declarando views do layout do fragment2
            preparo = (EditText) view.findViewById(R.id.editText_preparo);
            passosGuardados = new ArrayList<>();
            add_container_preparo = (LinearLayout) view.findViewById(R.id.add_container_preparo);

            //declarando botao
            adicionarPrep = (Button) view.findViewById(R.id.item_adicionar_preparo);
            adicionarPrep.setOnClickListener(this);

            //Obter checkbox e entrada em seg
            clockCheckBox = (CheckBox) view.findViewById(R.id.ingr_clockcheckbox);
            entradaSegundos = (EditText) view.findViewById(R.id.editText_addclock);

            //Remover entrada de numero de segundos inicialmente
            entradaSegundos.setVisibility(View.GONE);
            //Adicionar listener no checkbox
            clockCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkBox = (CheckBox) view;
                    if (!checkBox.isChecked()) {
                        entradaSegundos.setText("");
                        entradaSegundos.setVisibility(View.GONE);
                    } else {
                        entradaSegundos.setVisibility(View.VISIBLE);
                    }
                }
            });
            /** BEGIN - Adicionar varios listeners para que o prefixo nao possa ser selecionado
             * ou mudaddo
             */
            final SpanWatcher watcher = new SpanWatcher() {
                @Override
                public void onSpanAdded(final Spannable text, final Object what,
                                        final int start, final int end) {
                    // Nothing here.
                }

                @Override
                public void onSpanRemoved(final Spannable text, final Object what,
                                          final int start, final int end) {
                    // Nothing here.
                }

                @Override
                public void onSpanChanged(final Spannable text, final Object what,
                                          final int ostart, final int oend, final int nstart, final int nend) {
                       corrigeRange();
                }
            };
            preparo.getText().setSpan(watcher,0,0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            preparo.addTextChangedListener(new TextWatcher() {
                CharSequence oldText;
                boolean flag = false;
                @Override
                public void afterTextChanged(Editable s) {
                    if (flag == true) return;
                    String passoStr = getNumPassoStr();
                    if (s.length() >= passoStr.length() &&
                            passoStr.equals(s.subSequence(0, passoStr.length()).toString())) {

                    } else {
                        flag = true;
                        synchronized(this){
                            preparo.setText(oldText.toString().toCharArray(), 0, oldText.length());
                        }
                            flag = false;
                        corrigeRange();

                    }
                    preparo.getText().setSpan(watcher,0,0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                    oldText = preparo.getText().toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                }
            });
            ;
            /** END - Adicionar varios listeners para que o prefixo nao possa ser selecionado
             * ou mudaddo
             */
        criaEntrada(passo == 0);
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
        final View addView_item = layoutInflater.inflate(R.layout.item_adicionar_receita_preparo, add_container_preparo, false);


        //BEGIN-\GUARDA O PASSO ADICIONADO
        try{

            Integer duration = null;
            if (clockCheckBox.isChecked()) {
                duration =  Integer.parseInt(entradaSegundos.getText().toString());
            }
            passosGuardados.add(new Passo(preparo.getText().toString(),
                   duration));
        } catch (Exception e) {
            Toast.makeText(getContext(),"Erro: passo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView prep = (TextView) addView_item.findViewById(R.id.editText_preparo_preenchido);
        Passo passoAdicionado = passosGuardados.get(passosGuardados.size() - 1);
        prep.setText(getVisualizaPassoStr(passoAdicionado));
        add_container_preparo.addView(addView_item);

        criaEntrada(true);


    }

}
