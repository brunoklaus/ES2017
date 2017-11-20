package com.example.bela.es2017.Estoque;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.bela.es2017.R;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Estoque extends AppCompatActivity {

    private FrameLayout frameLayout;
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fab_cod_barras;
    private FloatingActionButton fab_selecao_itens;

    //variaveis para o recycler dos ingredientes do estoque
    private RecyclerView recyclerViewEstoque;
    private AdapterEstoque adapterEstoque;
    private List<Ingrediente> ingredientes;
    public FileOutputStream outputStream;
    public InputStream fileInputStream;
    public String filename;
    public File file;
    public BufferedReader buffer;
    //public ArrayList<String> nomesArq;
    //public String passedItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        recyclerViewEstoque = (RecyclerView) findViewById(R.id.recycler_estoque);
        ingredientes = new ArrayList<Ingrediente>();

        //criar um arquivo para salvar os ingredientes
        filename = "ingredienteEstoquePessoal.txt";
        try {
            criarArquivo(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //lendo o arquivo direto no construtor do adapter pq retorna uma lista de ingredientes

        //passando os dados para o adapter
        adapterEstoque = new AdapterEstoque( lerArquivo(filename), Estoque.this);
        recyclerViewEstoque.setAdapter(adapterEstoque);

        //adiciona uma linha entre os itens
        recyclerViewEstoque.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        RecyclerView.LayoutManager layoutEs = new LinearLayoutManager(Estoque.this,LinearLayoutManager.VERTICAL, false);
        recyclerViewEstoque.setLayoutManager(layoutEs);


        //fabmenu comeca aqui
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

                IntentIntegrator integrator = new IntentIntegrator(Estoque.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setOrientationLocked(false);
                integrator.setPrompt("Escaneie o código de barras do produto");
                integrator.setCameraId(0);  // Selecionar camera do dispositivo
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        fab_selecao_itens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it2 = new Intent(Estoque.this, AddIngredientes.class);
                Bundle b = new Bundle();
                it2.putExtras(b);
                startActivityForResult(it2,2);
            }
        });
    }

    private void criarArquivo(String filename) throws FileNotFoundException {
        outputStream = openFileOutput(filename, Context.MODE_APPEND);
        file = getFilesDir();
    }

    public String getFilename () {
        return filename;
    }

    public List<Ingrediente> lerArquivo(String filename) {
        try {
            fileInputStream = this.openFileInput(filename);

            //se existe algo no arquivo
            if(fileInputStream != null) {
                InputStreamReader is = new InputStreamReader(fileInputStream);
                buffer = new BufferedReader(is);
                String textoRecebido = "";

                while ((textoRecebido = buffer.readLine()) != null) {
                    Ingrediente ing = new Ingrediente(textoRecebido, FALSE);
                    ingredientes.add(ing);
                }
                fileInputStream.close();
                return ingredientes;
            } else { //se não existe nada no arquivo
                Toast.makeText(this,"O estoque está vazio.", Toast.LENGTH_SHORT)
                        .show();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void escreverArquivo(ArrayList<String> item_recebido) {

        String nome;
        try {
            //escreve um nome por linha no arquivo
            for(int i = 0; i < item_recebido.size(); i++){
                nome = item_recebido.get(i) + "\r\n";
                outputStream.write(nome.getBytes());
            }
            Toast.makeText(this, "Saved \n" + "Path --" + file + "\t", Toast.LENGTH_SHORT).show();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateArquivo () throws FileNotFoundException {

        deleteFile(filename);
        outputStream = openFileOutput(filename, Context.MODE_APPEND);

        String nome;
        try {
            //escreve um nome por linha no arquivo
            for(int i = 0; i < ingredientes.size(); i++){
                nome = ingredientes.get(i).getNomeIngrediente() + "\r\n";
                outputStream.write(nome.getBytes());
            }
            Toast.makeText(this, "Saved \n" + "Path --" + file + "\t", Toast.LENGTH_SHORT).show();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //adicionando ao estoque o que foi selecionado
        if (requestCode == 2 && data != null) {

           ArrayList<String> item_recebido = new ArrayList<String>();
           item_recebido = data.getStringArrayListExtra("nome_ingrediente");

           //addicionando todos os ingredientes selecionados que estao no array item_recebido
           for(int i = 0; i < item_recebido.size(); i++){
               Ingrediente ing = new Ingrediente(item_recebido.get(i), TRUE);
               ingredientes.add(ing);
           }

           //salvar ingredientees recebidos na memoria do celular
            escreverArquivo(item_recebido);

            adapterEstoque = new AdapterEstoque( ingredientes , Estoque.this);
            recyclerViewEstoque.setAdapter(adapterEstoque);
            //adiciona uma linha entre os itens
            recyclerViewEstoque.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        } else
            {
                //leitor de codigo de barras
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if(result != null) {
                    if(result.getContents() == null) {
                        Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                    } else {
                        // chamar tela de inserção
                        Toast.makeText(this, "Resultado: " + result.getContents(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
        }
    }

}
