package com.example.klaus.es2017;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunogata on 09/09/17.
 */

public class IngredientesDAO extends SQLiteOpenHelper {
    private static final String TABELA = "Ingredientes";
    private static final String DATABASE = "Estoque";

    public IngredientesDAO(Context context){
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String ddl = "CREATE TABLE " + TABELA
                + "(id INTEGER PRIMARY KEY,"
                + "nome TEXT NOT NULL,"
                + "quantidade TEXT NOT NULL,"
                + "marca TEXT);";
        db.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }

    public void addIngrediente(Ingrediente ingrediente){
        ContentValues values = new ContentValues();

        values.put("nome", ingrediente.getNome());
        values.put("quantidade", ingrediente.getQuantidade());
        values.put("marca", ingrediente.getMarca());

        getWritableDatabase().insert(TABELA, null, values);
    }

    public void rmvIngrediente(Ingrediente ingrediente){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {ingrediente.getId().toString()};
        db.delete("ingredientes", "id=?", args);
    }

    public void editIngrediente(Ingrediente ingrediente){
        ContentValues values = new ContentValues();

        values.put("nome", ingrediente.getNome());
        values.put("quantidade", ingrediente.getQuantidade());
        values.put("marca", ingrediente.getMarca());

        String[] id_alterar = {ingrediente.getId().toString()};
        getWritableDatabase().update(TABELA, values, "id=?", id_alterar);
    }

    public List<Ingrediente> getLista(){
        List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + ";", null);

        while (cursor.moveToNext()){
            Ingrediente ingrediente = new Ingrediente();
            ingrediente.setId(cursor.getLong(cursor.getColumnIndex("id")));
            ingrediente.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            ingrediente.setQuantidade(cursor.getString(cursor.getColumnIndex("quantidade")));
            ingrediente.setMarca(cursor.getString(cursor.getColumnIndex("marca")));
        }
        cursor.close();

        return ingredientes;
    }
}
