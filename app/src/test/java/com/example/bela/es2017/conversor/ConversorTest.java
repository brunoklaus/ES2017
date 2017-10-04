package com.example.bela.es2017.conversor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by brunogata on 04/10/17.
 */
public class ConversorTest {

    private int cont_erros = 0;

    int medida1 = 1, medida2 = 4; // Medida1 = Copo americano --- Medida2 = Colher de Cha
    int quantidade_medida1 = 1;

    @Test
    public void testConvert(){
        Conversor converter = new Conversor();
        double resultado = converter.converteMedidas(medida1, quantidade_medida1, medida2);

        if(resultado != 40.){
            throw new RuntimeException("Resultado ruim: " + resultado);
        }
    }

    public static void main(String... args){

        ConversorTest teste = new ConversorTest();

        try {teste.testConvert();}
        catch (Throwable e){
            teste.cont_erros++;
            e.printStackTrace();
        }

        if(teste.cont_erros > 0){
            throw new RuntimeException("Achou-se " + teste.cont_erros + " erros");
        }
    }
}