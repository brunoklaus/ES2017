package com.example.bela.es2017.conversor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bela.es2017.R;

import org.w3c.dom.Text;

public class ConversorActivity extends AppCompatActivity implements OnItemSelectedListener {

    Spinner lista_medida1;
    Spinner lista_medida2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversor);

        lista_medida1 = (Spinner) findViewById(R.id.spinner1);
        lista_medida2 = (Spinner) findViewById(R.id.spinner2);

        TextView medida2_valor = (TextView) findViewById(R.id.medida2);

        String[] itens_medidas = new String[]{"Copo Americano", "Xicara de Cha", "Colher de Sopa",
                                              "Colher de Cha", "Colher de Cafe"};

        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, itens_medidas);

        lista_medida1.setAdapter(spinner_adapter);
        lista_medida2.setAdapter(spinner_adapter);

        lista_medida1.setOnItemSelectedListener(this);
        lista_medida2.setOnItemSelectedListener(this);
        /*Conversor converter = new Conversor();

        double x = converter.converteMedidas(lista_medida1.getSelectedItemPosition(), 5, lista_medida2.getSelectedItemPosition());

        updateTextView(x);
        */
    }

    private void updateTextView(double novovalor){
        TextView novotexto = (TextView) findViewById(R.id.medida2);
        novotexto.setText(String.format("%.2f %s", novovalor, lista_medida2.getSelectedItem()));//String.valueOf(novovalor) + " " + String.valueOf(lista_medida2.getSelectedItem()));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Conversor converter = new Conversor();

        int posicao1 = lista_medida1.getSelectedItemPosition();
        int posicao2 = lista_medida2.getSelectedItemPosition();

        double x = converter.converteMedidas(posicao1, 5, posicao2);
        updateTextView(x);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {/*Faz nada*/}
}
