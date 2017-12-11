package com.example.bela.es2017.conversor;

import com.example.bela.es2017.firebase.db.model.InstIngrediente;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;

import org.apache.commons.math3.fraction.Fraction;

/**
 * Created by brunogata on 28/09/17.
 */

public class Conversor {

    final private double COPO = 200.0; // Copo Americano - 200mL
    final private double XICARA_CHA = 240.0; // Xicara de Cha - 240mL
    final private double COLHER_SOPA = 15.0; // Xicara de Cafe - 15mL
    final private double COLHER_CHA = 5.0; // Colher de Cha - 5mL
    final private double COLHER_CAFE = 2.5; // Colher de Cafe - 2.5mL

    final private double[] MEDIDAS = {COPO, XICARA_CHA, COLHER_SOPA, COLHER_CHA, COLHER_CAFE};


    /**
     * Converte um valor de uma medida para outra
     * @param selecao_medida1 - Índice da Medida1 no vetor. Medida1 é a medida que será convertida
     * @param quantidade_medida1 - Quantidade da Medida1 a ser convertida
     * @param selecao_medida2 - Índice da Medida2 no vetor. Medida2 é a medida convertida.
     * @return - Valor convertido da Medida1 na Medida2
     */
    public double converteMedidas(int selecao_medida1, int quantidade_medida1, int selecao_medida2){
        return ((quantidade_medida1 * MEDIDAS[selecao_medida1]) / MEDIDAS[selecao_medida2]);
    }

    /**
     * Dadas duas receitas (com unidades possivelmente diferentes), usa-se o Firebase para
     * achar uma conversao das unidades e "somar"
     * @param A receita #1
     * @param B receita #2
     * @param s usado para fazer o callback na classe que procurou a receita
     * @post s recebe NULL sse nao achou conversao, senao recebe uma receita com
     * nome de A e unidade de A, sendo a unidade de B convetida e somada na de A.
     */
    public static void adicionaComFB(InstIngrediente A, InstIngrediente B, final Searcher<InstIngrediente> s) {
        FBUnidadeConversorPreload conv = new FBUnidadeConversorPreload();
        if (!A.nome.equals(B.nome)) {
            B.nome = A.nome;
        }
        final InstIngrediente a = A;
        final InstIngrediente b = B;

        conv.findConv(B.unidade, A.unidade, A.nome, new Searcher<Double>() {
            @Override
            public void onSearchFinished(String query, List<Double> results, QueryRunnable<Double> q, boolean update) {
                if (results.isEmpty()) {
                    s.onSearchFinished("",new ArrayList<InstIngrediente>(),null,true);
                } else {
                    Double qtdeB = results.get(0);
                    Double res = a.qtde + qtdeB * b.qtde;
                    res = round(res,2);
                    s.onSearchFinished("",Arrays.asList(
                            new InstIngrediente(a.nome,res,a.unidade)),null,true);
                }
            }
        });
    }


    /**
     * Dadas duas receitas (com unidades possivelmente diferentes), usa-se o Firebase para
     * achar uma conversao das unidades e "subtrair"
     * @param A receita #1
     * @param B receita #2
     * @param s usado para fazer o callback na classe que procurou a receita
     * @post s recebe NULL sse nao achou conversao, senao recebe uma receita com
     * nome de A e unidade de A, sendo a unidade de B convetida e subtraida  de A.
     */
    public static void subtraiComFB(InstIngrediente A, InstIngrediente B, final Searcher<InstIngrediente> s) {
        FBUnidadeConversorPreload conv = new FBUnidadeConversorPreload();
        if (!A.nome.equals(B.nome)) {
            B.nome = A.nome;
        }
        final InstIngrediente a = A;
        final InstIngrediente b = B;



        conv.findConv(B.unidade, A.unidade, A.nome, new Searcher<Double>() {
            @Override
            public void onSearchFinished(String query, List<Double> results, QueryRunnable<Double> q, boolean update) {
                if (results.isEmpty()) {
                    s.onSearchFinished("",new ArrayList<InstIngrediente>(),null,true);
                } else {
                    Double qtdeB = results.get(0);
                    Double res = a.qtde - qtdeB * b.qtde;
                    if (res < 0) {
                        res = 0.0;
                    }
                    res = round(res,2);
                    s.onSearchFinished("",Arrays.asList(
                            new InstIngrediente(a.nome,res,a.unidade)),null,true);
                }
            }
        });
    }

    /**
     * Metodo utilitario que arrendonda para um numero de casas decimais
     * @param value valor
     * @param places # casas decimais a serem utilizadas
     * @return valor arrendondado para as casas decimais
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Aproxima um Double para uma fracao
     * @param d valor do double
     * @param maxDenominator max denominador usado  na aproximacao
     * @return string da fracao aproximada do valor
     */
    public static String approxDouble(Double d, int maxDenominator){
        return new Fraction(d,maxDenominator).toString();

    }

}





















