package com.example.bela.es2017.helpers;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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


    public static void adicionaEstoqueExemplo(FirebaseUser user, DatabaseReference mDatabase){
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
        for (InstIngrediente inst : l) {
            inserenoEstoque(user,mDatabase,inst ,true);
        }
    }

    public static void adicionaReceitasIniciais(DatabaseReference mDatabase) {
        adicionaBolonhesa(mDatabase);
        adicionaHamburger(mDatabase);

    }


}
