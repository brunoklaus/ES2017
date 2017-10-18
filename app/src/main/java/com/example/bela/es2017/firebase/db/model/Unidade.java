package com.example.bela.es2017.firebase.db.model;

import com.example.bela.es2017.conversor.Conversor;

/**
 * Created by klaus on 16/10/17.
 */

public class Unidade {
    public enum uEnum {
        ML, L, COPO, XICARA_CHA, COLHER_SOPA, COLHER_CHA, COLHER_CAFE, MG, G, KG, UNIDADE, VAZIO;

        @Override public String toString(){
            return enumUnidadeStr(new InstIngrediente("",1.0,this));
        }
    }

    public static String enumUnidadeStr(InstIngrediente A){
        switch (A.unidadeEnum) {
            case ML: return "ml";
            case L: return "l";
            case MG: return "mg";
            case G: return "g";
            case KG: return "kg";
            case UNIDADE: if (Conversor.round(A.qtde,2) == 1.0) return "unidade";
            else return "unidades";
            case VAZIO: return "---";
            case COPO:
                if (Conversor.round(A.qtde,2) == 1.0) return "copos";
                else return "copos";
            case XICARA_CHA:
                if (Conversor.round(A.qtde,2) == 1.0) return "xícara de chá";
                else return "xícaras de chá";
            case COLHER_SOPA:
                if (Conversor.round(A.qtde,2) == 1.0) return "colher de sopa";
                else return "colheres de sopa";
            case COLHER_CHA:
                if (Conversor.round(A.qtde,2) == 1.0) return "colher de chá";
                else return "colheres de chá";
            case COLHER_CAFE:
                if (Conversor.round(A.qtde,2) == 1.0) return "colher de café";
                else return "colheres de café";

            default: throw new IllegalArgumentException("Nao achou string pra unidade");

        }
    }



}
