package com.example.bela.es2017.helpers;

import com.example.bela.es2017.conversor.Conversor;
import com.example.bela.es2017.conversor.ConversorHelper;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;


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
    public static String getIngredientStr(List<InstIngrediente> ingredientesUsados, boolean includeUnits) {
        if ( ingredientesUsados == null) {
            return "";
        }
        String str = "";
        for (int i = 0; i < ingredientesUsados.size(); i++) {
            String nxt = ingredientesUsados.get(i).nome;
            if (includeUnits){
                nxt += " " + interpretQtde(ingredientesUsados.get(i),13,true);
            }
            nxt += (i != ingredientesUsados.size() - 1) ? ", " : "";
            str += nxt;
        }
        return str;
    }
    public static String readFromJsonFile(Context c){

        Writer writer = null;
        InputStream is = null;
        try {
            AssetManager am = c.getAssets();
            is  = am.open("quotes.json");
            writer = new StringWriter();
            char[] buffer = new char[1024];
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        String jsonString = writer.toString();
        return jsonString;
    }

    public static String findLongestMatch(String regex, String s) {
        Pattern pattern = Pattern.compile("(" + regex + ")$");
        Matcher matcher = pattern.matcher(s);
        String longest = null;
        int longestLength = -1;
        for (int i = s.length(); i > longestLength; i--) {
            matcher.region(0, i);
            if (matcher.find() && longestLength < matcher.end() - matcher.start()) {
                longest = matcher.group();
                longestLength = longest.length();
            }
        }
        return longest;
    }

    public static double parseQtde(String str) {
        if (str.contains("/")) {
            int start, end;
            start = end = str.indexOf('/');
            while(start > 0 && Character.isDigit(str.charAt(start-1))) {
                start--;
            }
            while(end + 1 < str.length() && Character.isDigit(str.charAt(end+1))){
                end++;
            }

            String fracStr = str.substring(start,end+1);
            String inteiroStr = str.substring(0,start).trim();
            FractionFormat ff = new FractionFormat();
            Fraction f = ff.parse(fracStr);
            if (f.getNumerator() > 4 || f.getNumerator() == 0 ||
                    f.getDenominator() == 0
                    || f.getDenominator()  > 4) {
                throw new NumberFormatException("Numerador e denominador precisar estar" +
                        " entre 1 e 4");
            }
            double res = f.doubleValue();
            if (!inteiroStr.isEmpty()){
                if (Integer.parseInt(inteiroStr) == 0) {
                    throw new NumberFormatException("0 nao pode fazer parte");
                }
                res+= (double) Integer.parseInt(inteiroStr);
            }
            return res;
        } else {

            if (Double.parseDouble(str) == 0.0) {
                throw new NumberFormatException("nao pode ser 0");
            }
            return Double.parseDouble(str);
        }
    }

    public static String interpretQtde(InstIngrediente ingr, int maxDenom, boolean includeUnit) {
        List<String> formalUnits =
                Arrays.asList("mg","miligrama","dg","decigrama",
                        "g","grama","dag","decagrama","centigrama",
                        "cg","kg","kilograma","ton","tonelada",
                        "ml","mililitro","dl","decilitro","l","litro",
                        "dal","decalitro"
                        );

        if (ingr.unidade == null || ingr.unidade.isEmpty()) {
            return "";
        }

        if (ingr.unidade.trim().compareTo("xicara") == 0) {
            int a =0;
        }

        for (String str:formalUnits) {
            if (ConversorHelper.unidadeEquals(ingr.unidade,str)) {
                //Apresentar unidade com ponto decimal
                return includeUnit ? Double.toString(Conversor.round(ingr.qtde,2)) +
                        " " + ingr.unidade : Double.toString(Conversor.round(ingr.qtde,2));
            }
        }


        //Nossa unidade nao eh uma dessass, logo apresentamos ela como fracao
        Fraction f = new Fraction(ingr.qtde,maxDenom);
        if (f.getDenominator() == 0) return "";
        int parteInteira = f.getNumerator() / f.getDenominator();
        f = f.subtract(parteInteira);
        if (parteInteira > 0 && f.getNumerator() > 0) {
            return (includeUnit) ? Integer.toString(parteInteira) + " " + f.toString().replaceAll(" ","")
                    + " " + ingr.unidade : Integer.toString(parteInteira) + " " +
                    f.toString().replaceAll(" ","");
        } else if (parteInteira > 0){
            return includeUnit ? Integer.toString(parteInteira) + " " + ingr.unidade :
                    Integer.toString(parteInteira);
        } else if (f.getNumerator() > 0) {
            return includeUnit ? f.toString().replaceAll(" ","") + " " + ingr.unidade :
                    f.toString().replaceAll(" ","");
        } else {
            return "";
        }
    }




}
