package com.example.bela.es2017.conversor;

import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.Unidade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import com.example.bela.es2017.firebase.db.model.Unidade.*;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;

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

    private static TreeMap<Unidade.uEnum,Double> uMap = null;
    private static TreeMap<Unidade.uEnum,Integer> typeMap = null;

    static TreeMap<Unidade.uEnum,Double> getConversionValueMap() {
        if (uMap != null) return  uMap;
        else {
            uMap = new TreeMap<Unidade.uEnum,Double>();
            uMap.put(Unidade.uEnum.L,1000.0);
            uMap.put(Unidade.uEnum.ML,1.0);
            uMap.put(Unidade.uEnum.COPO,200.0);
            uMap.put(uEnum.XICARA_CHA,240.0);
            uMap.put(uEnum.COLHER_SOPA,15.0);
            uMap.put(uEnum.COLHER_CHA,5.0);
            uMap.put(uEnum.COLHER_CAFE,2.5);
            uMap.put(uEnum.KG,1000.0);
            uMap.put(uEnum.G,1.0);
            uMap.put(uEnum.MG,0.001);
            uMap.put(uEnum.UNIDADE,1.0);
            uMap.put(uEnum.VAZIO, 1.0);
            return uMap;
        }
    }


    static TreeMap<Unidade.uEnum,Integer> getIDMap() {
        if (typeMap != null) return  typeMap;
        else {
            typeMap = new TreeMap<Unidade.uEnum,Integer>();
            typeMap.put(Unidade.uEnum.L,1);
            typeMap.put(Unidade.uEnum.ML,1);
            typeMap.put(Unidade.uEnum.COPO,1);
            typeMap.put(uEnum.XICARA_CHA,1);
            typeMap.put(uEnum.COLHER_SOPA,1);
            typeMap.put(uEnum.COLHER_CHA,1);
            typeMap.put(uEnum.COLHER_CAFE,1);
            typeMap.put(uEnum.MG,2);
            typeMap.put(uEnum.G,2);
            typeMap.put(uEnum.KG,2);
            typeMap.put(uEnum.UNIDADE,3);
            typeMap.put(uEnum.VAZIO, 4);
            return typeMap;
        }
    }


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
     * Adiciona os conteudos do ingr B para o A, convertendo a medida de B para a de A no processo
     * @param A ingrediente
     * @param B ingrediente
     * @throws IllegalStateException se unidade nao foi encontrada
     * @throws IllegalArgumentException se unidades para adicao nao sao compativeis
     * @return
     */
    public static InstIngrediente adiciona(InstIngrediente A, InstIngrediente B) throws IllegalStateException{
        if (!A.nome.equals(B.nome)) {
            throw new IllegalArgumentException("Adicao nao pode ocorrer porque nomes sao distintos");
        }

        TreeMap<Unidade.uEnum,Double> um = getConversionValueMap();
        TreeMap<Unidade.uEnum,Integer> idm = getIDMap();

        if (!idm.containsKey(A.unidadeEnum) || !idm.containsKey(B.unidadeEnum)) {
            throw new IllegalStateException("Nao achou a unidade entre as registradas");
        }
        if (idm.get(A.unidadeEnum) != idm.get(B.unidadeEnum)) {
           throw new IllegalArgumentException("Unidades para adicao sao diferentes");
        }
        Double qtdeBconv = (B.unidadeEnum.equals(uEnum.VAZIO)) ? 0 :  B.qtde * um.get(B.unidadeEnum) / um.get(A.unidadeEnum);
        InstIngrediente res = new InstIngrediente(A.nome,round(A.qtde + qtdeBconv,2),A.unidadeEnum);
        return res;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static String approxDouble(Double d, int maxDenominator){
        return new Fraction(d,maxDenominator).toString();

    }

}





















