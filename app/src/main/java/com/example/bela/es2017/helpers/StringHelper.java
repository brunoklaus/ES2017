package com.example.bela.es2017.helpers;

import com.example.bela.es2017.firebase.db.model.InstIngrediente;

import java.util.List;

/**
 * Classe que contem alguns metodos para criar strings desejadas.
 * Created by klaus on 15/09/17.
 */

public class StringHelper {

    /**
     * Código que coloca newlines na string para não sair da linha quando estiver dando
     * display no botao
     *
     * @param str string de entrada
     * @return mesma string mas com newlines
     */
    public static String adjustStr(String str, int MAX_LEN) {
        String[] words = str.trim().split(" ");
        if (words.length == 0) return "";
        String res = words[0];
        int caracterLinha = 0;
        for (int i = 1; i < words.length; i++) {
            String nxt = " " + words[i];
            caracterLinha += nxt.length();
            if (caracterLinha > MAX_LEN) {
                res += "\n" + nxt;
                caracterLinha = nxt.length();
            } else {
                res += nxt;
            }
        }
        return res;
    }

    /**
     * Código que cria string para ser mostrada a partir de uma lista de ingredientes usados,
     * colocando newline para  não sair da linha quando estiver dando display no botao
     *
     * @param ingredientesUsados lista de ingredientes usados na receita
     * @return string com os ingredientes listados um a um, com newlines
     */
    public static String getIngredientStr(List<InstIngrediente> ingredientesUsados, int MAX_LEN) {
        String str = "";
        int caracterLinha = 0;
        for (int i = 0; i < ingredientesUsados.size(); i++) {
            String nxt = ingredientesUsados.get(i).nome;
            nxt += (i != ingredientesUsados.size() - 1) ? ", " : "";
            caracterLinha += nxt.length();
            if (caracterLinha > MAX_LEN) {
                str += "\n" + nxt;
                caracterLinha = nxt.length();
            } else {
                str += nxt;
            }
        }
        return str;
    }
}
