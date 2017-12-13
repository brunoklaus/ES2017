/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apriori;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.Item;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
[carnes] => [azedos]
[carnes] => [gordurosos]
[apimentado] => [azedos]
[apimentado] => [gordurosos]
[carnes, gordurosos] => [azedos]
[carnes, azedos] => [gordurosos]
[carnes] => [azedos, gordurosos]
[apimentado, gordurosos] => [azedos]
[apimentado, azedos] => [gordurosos]
[apimentado] => [azedos, gordurosos]
[carnes, apimentado] => [azedos]
[carnes, apimentado] => [gordurosos]
[carnes, apimentado, gordurosos] => [azedos]
[carnes, apimentado, azedos] => [gordurosos]
[carnes, apimentado] => [azedos, gordurosos]
[doces, azedos] => [carnes]
[carnes, doces] => [azedos]
[doces, gordurosos] => [carnes]
[carnes, doces] => [gordurosos]
[doces, gordurosos] => [azedos]
[doces, azedos] => [gordurosos]
[doces, azedos, gordurosos] => [carnes]
[carnes, doces, gordurosos] => [azedos]
[carnes, doces, azedos] => [gordurosos]
[doces, gordurosos] => [carnes, azedos]
[doces, azedos] => [carnes, gordurosos]
[carnes, doces] => [azedos, gordurosos]
 */
public class IA_Receitas {
    
    private String ultima_atualizacao;
    private final Apriori modelo;
    private final Instances dados;
    private HashMap<Integer, String> tabela_tags;
    private final List<RegraAplicavel> tabela_regras;
    
   /* public static void main(String... args) throws Exception{
           IA_Receitas receitas = new IA_Receitas("C:\\Users\\JAIME~1.DES\\AppData\\Local\\Temp\\ARFFdados1895120724066895776.arff");
        //   receitas.atualizaRegras();
         //  String[] transacao = {"carnes", "doces", "azedos"};
         //  System.out.println(Arrays.toString(receitas.aplicaRegras(Arrays.asList(transacao))));
          // Map<String, Integer> mapTeste = new HashMap<>();
   

           
    }*/
    public IA_Receitas(String path) throws Exception{
        
        ConverterUtils.DataSource src = new ConverterUtils.DataSource(path);
       // System.out.println(src.);
       int numRegras = 999;
        Instances data = src.getDataSet();
        dados = data;
        modelo = new Apriori();
        modelo.setNumRules(numRegras);
        tabela_regras = new ArrayList<>();
      
      
             
    }
    
    public String getUltAtt(){
        return ultima_atualizacao;
    }
    
    public void atualizaRegras() throws Exception{
        // atualiza data
        ultima_atualizacao = getDataAtual();
        // constroi modelo
        modelo.buildAssociations(dados);
        // Cria tabela de implicacao
        makeRegras();
    }
    
    
    private void makeRegras(){
        List<AssociationRule> regras = modelo.getAssociationRules().getRules();
        regras.forEach((reg) -> {
            String[] itensPrem = new String[reg.getPremise().size()];
            Item[] premList = reg.getPremise().toArray(new Item[reg.getPremise().size()]);
            for (int i = 0; i < itensPrem.length; i++) {
                itensPrem[i] = premList[i].getAttribute().name();
            }
            String[] itensCons = new String[reg.getConsequence().size()];
            Item[] consList = reg.getConsequence().toArray(new Item[reg.getConsequence().size()]);
            for (int i = 0; i < itensCons.length; i++) {
                itensCons[i] = consList[i].getAttribute().name();
            }
            
            this.tabela_regras.add(new RegraAplicavel(itensPrem, itensCons));
          /*  Collection<Item> consequences = reg.getConsequence();
            Collection<Item> premises = reg.getPremise();
            String cons = "";
            cons = consequences.stream().map((item) -> item.getAttribute().name() + " ").reduce(cons, String::concat);
            String prem = "";
            prem = premises.stream().map((item) -> item.getAttribute().name() + " ").reduce(prem, String::concat);
            
            System.out.println(prem.trim() + " => " + cons.trim());*/
        }); 

    }
    
    //Aplica a regra à transação e devolve uma Lista recomendada
    public String[] aplicaRegras(List<String> transacao){
        TreeSet<String> consequencia = new TreeSet<>();
        this.tabela_regras.stream().filter((regra) -> (regra.aplicaRegra(transacao))).forEachOrdered((regra) -> {
            consequencia.addAll(regra.getConsequencia());
        });
        return consequencia.toArray(new String[consequencia.size()]);
    } 

    
    public void setTabela(Object tabela) throws Exception{
        // Atualiza tabela para atualizacao do modelo
        /*this.dados = constroiTabelaArff(tabela);*/
    }
    
    public void setTabelaTags(HashMap<Integer, String> tabela_tags){
        // Atualiza tabela de tags
        this.tabela_tags = tabela_tags;
    }
    
    private /*Instances*/ void constroiTabelaArff(/*ArrayList[]*/ Object transicao) throws Exception{
        // Converte tabela de transicao ATRIBUTE-RELATION (ARFF). Tabela estilo WEKA
    }
    
    private void converteTabelaTags_TAG_INT(){
        // Converte as transicoes escritas em tags para numeros inteiros
    }
    
    private void conveteTabelaTags_INT_TAG(){
        // Converte as transicoes escritas em numeros inteiros para tags
    }
    
    private String getDataAtual(){
        
        DateFormat data_formato = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        
        return(data_formato.format(date));
    }
    
    private static class RegraAplicavel{
        private final List<String> premissa;
        private final List<String> consequencia;
        public RegraAplicavel(String[] itensPremissa, String[] itensConsequencia){
            this.premissa = Arrays.asList(itensPremissa);
            this.consequencia = Arrays.asList(itensConsequencia);
        }
        
        public boolean aplicaRegra(List<String> itens){
            return premissa.stream().noneMatch((item) -> (!itens.contains(item)));
        }
        
        public List<String> getConsequencia(){
            return consequencia;
        }
    }
}
