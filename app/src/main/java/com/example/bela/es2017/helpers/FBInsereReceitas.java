package com.example.bela.es2017.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.bela.es2017.Add_receita.Adicionar_receita;
import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.model.Unidade;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Exemplos De como adicionar uma receita ao banco de dados firebase. Eh bem simples, basta criar
 * o objeto correspondente em model (nesse caso a Receita). child("receitas") nos leva ao no das
 * receitas, push nos leva a um novo no com id unico (ou seja, teremos uma nova receita mesmo se
 * houver outra com mesmo nome), e setvalue recebe a Receita, converte ela em JSON e transfere
 * como entrada para esse no.
 * Created by klaus on 15/09/17.
 */

public class FBInsereReceitas {


    private static void adicionaHamburger(DatabaseReference mDatabase) {

        InstIngrediente i1 = new InstIngrediente("Carne de vaca picada", 500, "g");
        InstIngrediente i2 = new InstIngrediente(" Cebola picada", 1, "");
        InstIngrediente i3 = new InstIngrediente("Mostarda", 1, "colher de sopa");
        InstIngrediente i4 = new InstIngrediente("Mostarda", 1, "colher de sopa");
        InstIngrediente i5 = new InstIngrediente("Molho inglẽs", 1, "colher de sopa");
        InstIngrediente i6 = new InstIngrediente("Sal", -1, "");
        InstIngrediente i7 = new InstIngrediente("Pimenta", -1, "");
        InstIngrediente i8 = new InstIngrediente("Salsa picada", 1, "raminho");

        List<InstIngrediente> l = Arrays.asList(i1, i2, i3, i4, i5, i6, i7, i8);

        String d1 = "Numa taça misturar todos os ingredientes, excepto o sal.\n" +
                "Retirar porções de carne para formar os hambúrgueres, começando por fazer uma bola e, depois, espalmando com a mão.\n" +
                "Deixar os hambúrgueres a repousar cerca de 15 minutos no frigorífico.\n" +
                "Levar o grelhador ao lume para que aqueça. Temperar os hambúrgueres com sal.\n" +
                "Untar o grelhador com manteiga (poderá ser manteiga com salsa e alho já incorporados) e colocar os hambúrgueres a grelhar.\n" +
                "Servir com salada.";


        Receita hamb = new Receita("Hamburguer de carne", "Como fazer um delicioso hamburguer",
                d1, l, R.drawable.hamburguer);
        insereReceita(mDatabase, hamb, true);
    }

    private static void adicionaBolonhesa(DatabaseReference mDatabase) {

        InstIngrediente i1 = new InstIngrediente("Carne de vaca picada", 500, "g");
        InstIngrediente i2 = new InstIngrediente("Polpa de tomate", 200, "g");
        InstIngrediente i3 = new InstIngrediente("Massa Espaguete", 350, "g");
        InstIngrediente i4 = new InstIngrediente("Dentes de alho", 3, "");
        InstIngrediente i5 = new InstIngrediente("Tomates maduros", 4, "200g");
        InstIngrediente i6 = new InstIngrediente("Cebola", 1, "");
        InstIngrediente i7 = new InstIngrediente("Orégão", -1, "");
        InstIngrediente i8 = new InstIngrediente("Azeite", 1, "");
        InstIngrediente i9 = new InstIngrediente("Sal", 1, "");
        List<InstIngrediente> l = Arrays.asList(i1, i2, i3, i4, i5, i6, i7, i8, i9);
        String d1 = "Num tacho alto, fritar a cebola e o alho picados.\n" +
                "Deitar de seguida o tomate em pedaços e a polpa de tomate.\n" +
                "Deixar refogar.\n" +
                "Juntar a carne picada, temperar com o sal. Deixar cozer.\n" +
                "Levar ao lume um tacho com a água temperada com sal, deixar ferver.\n" +
                "Deitar o esparguete. Deixar cozer, mexer a espaços até ficar al dente.\n" +
                "Juntar o molho ao esparguete.\n" +
                "Temperar com orégãos a gosto.\n" +
                "Servir quente.";
        Receita bol = new Receita("Molho bolonhesa", "Para a melhor macarronada",
                d1, l, R.drawable.molho_bolonhesa);
        insereReceita(mDatabase, bol, true);
    }

    /**
     * Insere uma receita no banco de dados do Firebase
     *
     * @param mDatabase  referencia do banco de dados
     * @param receita    a receita
     * @param safeInsert se verdadeiro, soh adiciona nao tiver outra com mesmo titulo
     */
    public static void insereReceita(DatabaseReference mDatabase, Receita receita, final boolean safeInsert) {
        final DatabaseReference db = mDatabase;
        final Receita r = receita;
        mDatabase.child("receitas").orderByChild("titulo").equalTo(r.titulo).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0 || !safeInsert) {
                            db.child("receitas").push().setValue(r);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }

        );
    }
    /**
     * Insere uma receita no banco de dados do Firebase com a Imagem correspondente
     *
     * @param mDatabase  referencia do banco de dados
     * @param receita    a receita
     * @param u    referencia a imagem (bitmap)
     * @param safeInsert se verdadeiro, soh adiciona nao tiver outra com mesmo titulo
     */
    public static void insereReceita(Adicionar_receita ad, DatabaseReference mDatabase, Receita receita, Uri u,
                                     final boolean safeInsert) {
        final DatabaseReference db = mDatabase;
        final Receita r = receita;
        final Uri filePath = u;
        final Adicionar_receita c = ad;
        mDatabase.child("receitas").orderByChild("titulo").equalTo(r.titulo).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0 || !safeInsert) {
                            final DatabaseReference dr = db.child("receitas").push();
                            dr.setValue(r);
                            final String id = dr.getKey().toString();
                            //Envia imagem ao FB
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                            if(filePath != null) {

                                StorageReference childRef = storageRef.child(id.toString());

                                //uploading the image
                                UploadTask uploadTask = childRef.putFile(filePath);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Receita r1 = r;
                                        r1.imgStorage = id;
                                        dr.setValue(r1);
                                        c.onUploadResult(true);
                                        Toast.makeText(c, "Upload successful", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        c.onUploadResult(false);
                                    }
                                });
                            } else {
                                dr.setValue(r);
                                c.onUploadResult(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }

        );
    }


    public static void insereImagem(final Context context, Uri filePath, String id){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        if(filePath != null) {

            StorageReference childRef = storageRef.child(id + ".png");

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     * Insere um item no estoque
     *
     * @param mDatabase  referencia do banco de dados
     * @param inst    o item
     * @param safeInsert se verdadeiro, soh adiciona nao tiver outra com mesmo titulo
     */
    public static void inserenoEstoque(FirebaseUser user, DatabaseReference mDatabase, InstIngrediente inst, final boolean safeInsert) {
        final DatabaseReference db = mDatabase;
        final InstIngrediente r = inst;
        final FirebaseUser u = user;
        mDatabase.child("users").child(u.getUid()).child("estoque").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0 || !safeInsert) {
                            db.child("users").child(u.getUid()).child("estoque").child(r.nome).setValue(r);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }

        );
    }
    /**
     * Associa codigo de barras ao ingrediente
     *
     * @param mDatabase  referencia do banco de dados
     * @param inst    o ingrediente
     * @param codigoBarras o codigo de barras
     * @param safeInsert se verdadeiro, soh adiciona nao tiver outra
     */
    public static void insereCodigoBarras(DatabaseReference mDatabase, InstIngrediente inst, String codigoBarras, final boolean safeInsert) {
        final DatabaseReference db = mDatabase;
        final InstIngrediente r = inst;
        final String barras = codigoBarras;
        mDatabase.child("barras").child(barras).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0 || !safeInsert) {
                            db.child("barras").child(barras).setValue(r);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }

        );
    }

    /**
     * Procura pelo dado codigo de barras no banco dados.
     *
     * @param mDatabase  referencia do banco de dados
     * @param codigoBarras o codigo de barras
     */
    public static void encontraCodigoBarras(DatabaseReference mDatabase, String codigoBarras, Searcher<InstIngrediente> searcher) {
        final DatabaseReference db = mDatabase;
        final Searcher<InstIngrediente> s = searcher;
        final String barras = codigoBarras;
        mDatabase.child("barras").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<InstIngrediente> l = new ArrayList<InstIngrediente>();
                        if (dataSnapshot.hasChild(barras)) {
                            InstIngrediente res = (InstIngrediente) dataSnapshot.child(barras).getValue(InstIngrediente.class);
                            l.add(res);
                        }
                        s.onSearchFinished(barras,l,null,true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }

        );
    }






    public static void adicionaEstoqueExemplo(FirebaseUser user, DatabaseReference mDatabase){
        InstIngrediente i1 = new InstIngrediente("Carne de vaca picada", 500, Unidade.uEnum.G);
        InstIngrediente i2 = new InstIngrediente("Polpa de tomate", 200, Unidade.uEnum.G);
        InstIngrediente i3 = new InstIngrediente("Massa Espaguete", 350, Unidade.uEnum.G);
        InstIngrediente i4 = new InstIngrediente("Dentes de alho", 3, Unidade.uEnum.UNIDADE);
        InstIngrediente i5 = new InstIngrediente("Tomates maduros", 4, Unidade.uEnum.UNIDADE);
        InstIngrediente i6 = new InstIngrediente("Cebola", 1, Unidade.uEnum.VAZIO);
        InstIngrediente i7 = new InstIngrediente("Orégão", -1, Unidade.uEnum.VAZIO);
        InstIngrediente i8 = new InstIngrediente("Azeite", 1, Unidade.uEnum.VAZIO);
        InstIngrediente i9 = new InstIngrediente("Sal", 1, Unidade.uEnum.VAZIO);
        List<InstIngrediente> l = Arrays.asList(i1, i2, i3, i4, i5, i6, i7, i8, i9);
        for (InstIngrediente inst : l) {
            inserenoEstoque(user,mDatabase,inst ,true);
        }
    }

    public static void adicionaReceitasIniciais(DatabaseReference mDatabase) {
        adicionaBolonhesa(mDatabase);
        adicionaHamburger(mDatabase);

    }


}
