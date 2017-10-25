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

import org.apache.commons.math3.geometry.spherical.twod.Edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

/**
 * Created by klaus on 24/10/17.
 */

public class FBUnidadeConversorPreload implements Searcher<ConversorEdge> {


    @Override
    public void onSearchFinished(String query, List<ConversorEdge> results, QueryRunnable<ConversorEdge> q, boolean update) {

        List<Vertex> vList = inst;
        HashMap<String, Integer> hMap = vMap;
        if (hMap == null ||  vList == null) {
            throw new IllegalStateException("Terminou de baixar os dados mas os valores continuam" +
                    " null");
        }
        if (FBUnidadeConversor.unidadeEquals(from, to)) {
            searcher.onSearchFinished(from, Arrays.asList(1.0), null, true);
            return;
        }
        boolean[] mark = new boolean[vList.size()];
        for (int i = 0; i < vList.size(); i++) {
            mark[i] = false;
        }

        Q = new LinkedList<>();
        //Achar vertice compativel com from
        Vertex source = null;
        for (Vertex v : vList) {
            if (FBUnidadeConversor.unidadeEquals(v.nome,from)) {
                source = v;
                break;
            }
        }
        if (source == null) {
            //Nao achou esse nome nas unidade
            searcher.onSearchFinished("", new ArrayList<Double>(), null, true);
            return;
        }
        Q.add(source);
        mark[source.id] = true;
        while (!Q.isEmpty()) {
            Vertex v = Q.poll();
            for (ConversorEdge e : v.l) {
                Vertex v2 = findVertex(e.n2, vList, hMap);
                if (!mark[v2.id] &&  FBUnidadeConversor.labelMatches(this.ingrName,e.label)) {
                    mark[v2.id] = true;
                    Q.add(v2);
                    parentMap.put(v2.nome, v.nome);
                    double ww = e.w == 0.0 ? 0.0 : 1 / e.w;
                    dblMap.put(e.n2, ww);
                    if (FBUnidadeConversor.unidadeEquals(to, e.n2)) {
                        sendPathResult(e.n2, from, searcher);
                        return;
                    }
                }
            }
        }
        searcher.onSearchFinished("", new ArrayList<Double>(), null, true);
    }



    class Vertex {
        public String nome;
        public List<ConversorEdge> l;
        public int id;

        public Vertex(String nome, int id) {
            this.id = id;
            this.nome = nome;
            l = new ArrayList<>();
        }
    }

    static List<Vertex> inst;
    static HashMap<String, Integer> vMap;
    Queue<Vertex> Q = new LinkedList<>();
    private HashMap<String, String> parentMap = new HashMap<>();
    private HashMap<String, Double> dblMap = new HashMap<>();
    private final Semaphore sem = new Semaphore(0);

    private String from;
    private String to;
    private Searcher<Double> searcher;
    private String ingrName;

    private void processResult(List<ConversorEdge> edges) {
        if (inst == null || vMap == null) {
            inst = new ArrayList<>();
            vMap = new HashMap<>();
            Set<String> vSet = new TreeSet<String>();
            for (ConversorEdge e : edges) {
                vSet.add(e.n1);
            }
            for (String str : vSet) {
                Vertex v = new Vertex(str, inst.size());
                inst.add(v);
                vMap.put(str, inst.size() - 1);
            }
            for (ConversorEdge e : edges) {
                int id1 = vMap.get(e.n1);
                inst.get(id1).l.add(e);
            }
        }
        onSearchFinished("",null,null,true);
    }

    void getVertex() {
        if (inst == null || vMap == null) {
            //Ler do Firebase todas arestas
            final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            final FBUnidadeConversorPreload r = this;

            db.child("unidadeGrafo").
                    addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<ConversorEdge> l = new ArrayList<ConversorEdge>();
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        ConversorEdge e = child.getValue(ConversorEdge.class);
                                        l.add(((ConversorEdge) child.getValue(ConversorEdge.class)));
                                    }

                                    final Searcher<ConversorEdge> s = new Searcher<ConversorEdge>() {
                                        @Override
                                        public void onSearchFinished(String query, List<ConversorEdge> results, QueryRunnable<ConversorEdge> q, boolean update) {
                                            r.processResult(results);
                                        }
                                    };
                                    s.onSearchFinished("",l,null,true);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
        } else {
            onSearchFinished("",null,null,true);
        }

    }

    void getVertexMap() {
        if (vMap == null) getVertex();
    }

    private void sendPathResult(String last, String from, Searcher<Double> searcher) {
        Double d = 1.0;
        String ptr = last;
        while (!FBUnidadeConversor.unidadeEquals(ptr, from)) {
            if (parentMap.get(ptr) == null) {
                throw new IllegalStateException("Did not find parent of" +
                        " " + ptr);
            } else if (dblMap.get(ptr) == null) {
                throw new IllegalStateException("Did not find double of" +
                        " " + ptr);
            }
            d *= dblMap.get(ptr);
            ptr = parentMap.get(ptr);

        }
        if (Double.compare(0, d) == 0) {
            searcher.onSearchFinished("", new ArrayList<Double>(), null, true);
        } else {
            ArrayList<Double> l = new ArrayList<>();
            l.add(new Double(1 / d));
            searcher.onSearchFinished("", l, null, true);
        }
    }

    private Vertex findVertex(String str, List<Vertex> vList, HashMap<String, Integer> hMap) {
        return vList.get(hMap.get(str));
    }

    public void findConv(String from, String to, String ingrName, Searcher<Double> searcher) {

        this.from = from;
        this.to = to;
        this.ingrName = ingrName;
        this.searcher = searcher;
        getVertex();

    }

}
