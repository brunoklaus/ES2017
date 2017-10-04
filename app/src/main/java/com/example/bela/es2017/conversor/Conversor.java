package com.example.bela.es2017.conversor;

/**
 * Created by brunogata on 28/09/17.
 */

public class Conversor {

    final private double COPO = 200.0;
    final private double XICARA_CHA = 240.0;
    //final private double XICARA_CAFE = ;
    final private double COLHER_SOPA = 15.0;
    final private double COLHER_CHA = 5.0;
    final private double COLHER_CAFE = 2.5;

    final private double[] MEDIDAS = {COPO, XICARA_CHA, COLHER_SOPA, COLHER_CHA, COLHER_CAFE};

    public double converteMedidas(int selecao_medida1, int quantidade_medida1, int selecao_medida2){
        return ((quantidade_medida1 * MEDIDAS[selecao_medida1]) / MEDIDAS[selecao_medida2]);
    }
}





















