package com.example.bela.es2017.conversor;

import com.example.bela.es2017.firebase.db.model.ConversorEdge;
import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.runnable.QueryRunnable;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Um conjunto de metodos designados para ajudar com a tarefa de conversao
 * Created by klaus on 24/10/17.
 */

public class ConversorHelper {


    /** Retira acentos diacriticos da string.
     *  @param str a string
     */
    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
    /** Adiciona arestas nas duas direcoes no grafo do FB
     *  @param edge A aresta de conversao
     *  @param safeInsert se verdadeiro adiciona aresta apenas se nao existir
     * */
    public static void insereEdge(ConversorEdge edge, final boolean safeInsert) {
        insereEdgeDir(FirebaseDatabase.getInstance().getReference(), edge, safeInsert);
        insereEdgeDir(FirebaseDatabase.getInstance().getReference(), new ConversorEdge(edge,true), safeInsert);

    }

    /** Adiciona aresta (uma direcao) no grafo do FB
     *  @param mDatabase referencia do banco de dados
     *  @param edge A aresta de conversao
     *  @param safeInsert se verdadeiro adiciona aresta apenas se nao existir
     * */
    private static void insereEdgeDir(DatabaseReference mDatabase, ConversorEdge edge, final boolean safeInsert) {
        final DatabaseReference db = mDatabase;
        final ConversorEdge e = edge;
        if (safeInsert) {
            mDatabase.child("unidadeGrafo").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                if (unidadeEquals(child.child("n2").getValue(String.class),(e.n2))
                                        && unidadeEquals(child.child("n1").getValue(String.class),(e.n1))
                                        ) {
                                    return;
                                }
                            }
                            ConversorEdge ecopy = new ConversorEdge(e,false);

                            DatabaseReference dr =
                                    db.child("unidadeGrafo").push();
                            ecopy.id = dr.getKey();
                            dr.setValue(ecopy);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }

            );
        } else {
            ConversorEdge ecopy = new ConversorEdge(e,false);

            DatabaseReference dr =
                    db.child("unidadeGrafo").push();
            ecopy.id = dr.getKey();
            dr.setValue(ecopy);
        }

    }

    /**
     * Calcula se deve-se tratar duas strings de unidades como "iguais". Eh possivel que duas strings sejam consideradas
     * a mesma unidade mesmo que nao sejam identicas. Por exemplo, podem ter acentos diacriticos
     * diferentes, caixa alta ou nao, etc.
     * @param n1 a primeira string a ser comparada
     * @param n2 a segunda string a ser comparada
     * @return verdadeiro somente se as duas strings sao consideradas a mesma unidade
     */
    public static boolean unidadeEquals(String n1, String n2) {
        n1 = n1.replaceAll(" ","");
        n2 = n2.replaceAll(" ","");

        n1 = deAccent(n1).toLowerCase();
        n2 = deAccent(n2).toLowerCase();

        n1 = n1.replaceAll("[^A-Za-z]+", "");
        n2 = n2.replaceAll("[^A-Za-z]+", "");

        n1 = n1.replaceAll(" ","");
        n2 = n2.replaceAll(" ","");

        n1 = n1.replaceAll("eres","er");
        n1 = n1.replaceAll("etes","ete");
        n1 = n1.replaceAll("s","");

        n2 = n2.replaceAll("eres","er");
        n2 = n2.replaceAll("etes","ete");
        n2 = n2.replaceAll("s","");

        return n1.compareTo(n2) == 0;
    }

    /**
     * Verifica se uma label esta dentro de outra
     * @param queryLabel a label que possivelmente contem a outra
     * @param foundLabel a label que possivelmente estah dentro da outra
     * @return verdadeiro sse foundLabel faz parte de queryLabel
     */
    public static boolean labelMatches(String queryLabel, String foundLabel) {
        if (queryLabel == null || foundLabel == null || foundLabel.isEmpty()) return true;
        // Create a Pattern object
        Pattern r = Pattern.compile(foundLabel);
        // Now create matcher object.
        Matcher m = r.matcher(queryLabel);
        return m.find();
    }

    /**
     * Adiciona as arestas iniciais do grafo de conversoes
     */
    public static void adicionaArestasIniciais(){
        List<ConversorEdge> edges = Arrays.asList(
                new ConversorEdge("kg","kilo",1.0),
                new ConversorEdge("kg","kilograma",1.0),
                new ConversorEdge("g","grama",1.0),
                new ConversorEdge("mg","miligrama",1.0),
                new ConversorEdge("g","mg",1000.0),
                new ConversorEdge("kg","g",1000.0),
                new ConversorEdge("ton","kg",1000.0),
                new ConversorEdge("tonelada","ton",1.0),
                new ConversorEdge("dg","decagrama",1.0),
                new ConversorEdge("dg","g",10.0),


                new ConversorEdge("mililitro","ml",1.0),

                new ConversorEdge("dl","ml",10.0),
                new ConversorEdge("dl","decilitro",1.0),

                new ConversorEdge("l","litro",1.0),
                new ConversorEdge("l","ml",1000.0),

                new ConversorEdge("dal","l",10.0),
                new ConversorEdge("dal","decalitro",1.0),

                new ConversorEdge("xicara","ml",240.0),
                new ConversorEdge("copo","ml",200.0),
                new ConversorEdge("copo de requeijao","ml",200.0),
                new ConversorEdge("copo americano","ml",240.0),

                new ConversorEdge("colher de sopa","ml",15.0),
                new ConversorEdge("colher de cha","ml",5.0),
                new ConversorEdge("colher de sobremesa","ml",10.0),
                new ConversorEdge("colher de cafe","ml",2.5),

                new ConversorEdge("xicara","g",160.0,"açucar"),
                new ConversorEdge("xicara","g",160.0,"acucar"),

                new ConversorEdge("xicara","g",120.0,"farinha"),
                new ConversorEdge("xicara","g",120.0,"fuba"),

                new ConversorEdge("xicara","g",300.0,"mel"),
                new ConversorEdge("xicara","g",90.0,"achocolatado"),

                new ConversorEdge("xicara","g",140.0,"amend"),
                new ConversorEdge("xicara","g",140.0,"noz"),
                new ConversorEdge("xicara","g",140.0,"castanha"),

                new ConversorEdge("xicara","g",140.0,"amido"),
                new ConversorEdge("xicara","g",140.0,"milho"),
                new ConversorEdge("xicara","g",140.0,"polvilho"),
                new ConversorEdge("xicara","g",140.0,"maizena"),


                new ConversorEdge("xicara","g",80.0,"queijo"),
                new ConversorEdge("xicara","g",80.0,"aveia"),
                new ConversorEdge("xicara","g",80.0,"coco"),

                new ConversorEdge("tablete","g",15.0,"fermento"),

                new ConversorEdge("colher de cha","g",10.0,"fermento"),
                new ConversorEdge("colher de cafe","g",3.0,"sal")

                );
        for (ConversorEdge e: edges) {
            insereEdge(e,true);
        }


    }

}
