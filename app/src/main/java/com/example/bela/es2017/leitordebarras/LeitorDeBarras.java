package com.example.bela.es2017.leitordebarras;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.example.bela.es2017.helpers.FBInsereReceitas;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class LeitorDeBarras extends AppCompatActivity implements Searcher<InstIngrediente>{

    String codigodeBarrasStr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitor_de_barras);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Escaneie o código de barras do produto");
        integrator.setCameraId(0);  // Selecionar camera do dispositivo
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            } else {
                codigodeBarrasStr = result.getContents();

                //Procura no Banco de dados
                FBInsereReceitas.encontraCodigoBarras(FirebaseDatabase.getInstance().getReference(),
                        codigodeBarrasStr,this);

                // chamar tela de inserção
                Toast.makeText(this, "Resultado: " + codigodeBarrasStr, Toast.LENGTH_LONG).show();

            }
        } else {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.setOrientationLocked(false);
            integrator.setPrompt("Escaneie o código de barras do produto");
            integrator.setCameraId(0);  // Selecionar camera do dispositivo
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onSearchFinished(String query, List<InstIngrediente> results, QueryRunnable<InstIngrediente> q, boolean update) {
        Intent it3 = new Intent(LeitorDeBarras.this, BarrasEntradaPopup.class);
        it3.putExtra("barras",codigodeBarrasStr);
        if (!results.isEmpty()) {
            InstIngrediente found = results.get(0);
            it3.putExtra("found", "TRUE");
            it3.putExtra("nome", found.nome);
            it3.putExtra("qtde", Double.toString(found.qtde));
            it3.putExtra("unidade", found.unidade.toString());
        } else {
            it3.putExtra("found", "FALSE");
        }
        startActivity(it3);
    }
}
