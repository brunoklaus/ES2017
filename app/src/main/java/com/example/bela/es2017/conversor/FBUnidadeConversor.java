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
 * Created by klaus on 24/10/17.
 */

public class FBUnidadeConversor implements Searcher<ConversorEdge>  {


    private Set<String> found = new TreeSet<>();
    private HashMap<String, String> parentMap = new HashMap<>();
    private HashMap<String, Double> dblMap = new HashMap<>();
    private double DIST_MAX = 5;
    int dist = 0;
    String ingrName;
    private String search_to;
    private String search_from;
    Searcher<Double> searcher;
    Queue<String> Q = new LinkedList<>();

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
    public void findConv(String from, String to, String ingrName, Searcher<Double> searcher){
        this.search_to = to;
        this.search_from = from;
        this.searcher = searcher;
        this.ingrName = ingrName;
        found.add(from);
        if (unidadeEquals(from,to)) searcher.onSearchFinished(from,Arrays.asList(1.0),null,true);
        encontraNomesAdj(from, this,true);
    }

    public static void encontraNomesAdj(String node, final Searcher searcher, boolean isFirstQuery) {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final Searcher<InstIngrediente> s = searcher;
        final String from = node;
        if (isFirstQuery) {
            db.child("unidadeGrafo").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<ConversorEdge> l = new ArrayList<ConversorEdge>();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                ConversorEdge e = child.getValue(ConversorEdge.class);
                                if (unidadeEquals(e.n1,from)) {
                                    l.add(((ConversorEdge) child.getValue(ConversorEdge.class)));
                                }
                            }
                            searcher.onSearchFinished(from,l,null,true);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }

            );
        }else {
            db.child("unidadeGrafo").orderByChild("n1").equalTo(from).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<ConversorEdge> l = new ArrayList<ConversorEdge>();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                l.add(((ConversorEdge) child.getValue(ConversorEdge.class)));

                            }
                            searcher.onSearchFinished(from,l,null,true);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }

            );
        }

    }

    public static void insereEdge(ConversorEdge edge, final boolean safeInsert) {
        insereEdgeDir(FirebaseDatabase.getInstance().getReference(), edge, safeInsert);
        insereEdgeDir(FirebaseDatabase.getInstance().getReference(), new ConversorEdge(edge,true), safeInsert);

    }

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

    private void sendPathResult(String last){
        Double d = 1.0;
        String ptr = last;
        while (!unidadeEquals(ptr,search_from)) {
            if (parentMap.get(ptr) == null) {
                throw new IllegalStateException("Did not find parent of" +
                        " " + ptr);
            } else if (dblMap.get(ptr) == null){
                throw new IllegalStateException("Did not find double of" +
                        " " + ptr);
            }
            d *= dblMap.get(ptr);
            ptr = parentMap.get(ptr);

        }
        if (Double.compare(0,d) == 0) {
            this.searcher.onSearchFinished(search_to,new ArrayList<Double>(),null,true);
        } else {
            ArrayList<Double> l = new ArrayList<>();
            l.add(new Double(1/d));
            this.searcher.onSearchFinished(search_to,l,null,true);
        }
    }

    @Override
    public void onSearchFinished(String query, List<ConversorEdge> results, QueryRunnable<ConversorEdge> q, boolean update) {
        dist++;
        if (dist > DIST_MAX) {
            this.searcher.onSearchFinished(search_to,new ArrayList<Double>(),null,true);
            return;
        }
        for (ConversorEdge e : results) {
            if (!this.found.contains(e.n2) && labelMatches(this.ingrName,e.label)) {
                found.add(e.n2);
                Q.add(e.n2);
                parentMap.put(e.n2,query);
                double ww  = e.w == 0.0 ? 0.0 : 1/e.w;
                dblMap.put(e.n2,ww);
                if (unidadeEquals(e.n2,this.search_to)) {
                    sendPathResult(e.n2);
                    return;
                }
            }

        }
        if (Q.isEmpty()){
            this.searcher.onSearchFinished(search_to,new ArrayList<Double>(),null,true);
        } else {
            String from = Q.poll();
            encontraNomesAdj(from,this, false);
        }
    }

    public static boolean labelMatches(String queryLabel, String foundLabel) {
        if (queryLabel == null || foundLabel == null || foundLabel.isEmpty()) return true;
        // Create a Pattern object
        Pattern r = Pattern.compile(foundLabel);
        // Now create matcher object.
        Matcher m = r.matcher(queryLabel);
        return m.find();
    }

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
