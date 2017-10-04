package com.example.bela.es2017.conversor;

/**
 * Created by brunogata on 28/09/17.
 */

public class Conversor {

    final private double COPO = 200.0; // Copo Americano - 200mL
    final private double XICARA_CHA = 240.0; // Xicara de Cha - 240mL
    //final private double XICARA_CAFE = ;
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
}





















