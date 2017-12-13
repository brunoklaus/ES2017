/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apriori;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Firebase.CompletionListener;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaime
 */
public class FirebaseThread extends Thread {

    @Override
    public void run() {

        String url = "https://esfes2017-2ce5e.firebaseio.com/";
        final Firebase dataRef = new Firebase(url);
        final Firebase dataRef2 = dataRef.child("receitas");
        List<String> receitasID = new ArrayList<>();
        //dataRef.child(".info/connected");
        // dataRef = dataRef.child("Filho1");
        /*  Map<String,Object> testes = new HashMap<>();
       testes.put("Teste7", "Teste7");
       testes.put("Teste8", "Teste8");
       testes.put("Teste9", "Teste9");*/
        

        //   dataRef.updateChildren(testes);
        dataRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dataRef2.removeEventListener(this);
                snapshot.getChildren().forEach((i) -> {
                   receitasID.add(i.getKey());
                });
                synchronized (FirebaseThread.this) {
                    FirebaseThread.this.notifyAll();
                }
            }

            @Override
            public void onCancelled(FirebaseError fe) {
                System.err.println("Listener was cancelled");
                synchronized (FirebaseThread.this) {
                    FirebaseThread.this.notifyAll();
                }
                
            }
        });
        synchronized (this) {
            try {
                this.wait();

            } catch (InterruptedException ex) {
                Logger.getLogger(FirebaseThread.class.getName()).log(Level.SEVERE, null, ex);
                
            }

        }
        Map<String, Integer> map1 = new HashMap<>();
        Map<Integer,String> map2 = new HashMap<>();
        for (int i = 0; i < receitasID.size(); i++) {
            map1.put(receitasID.get(i), i);
            map2.put(i, receitasID.get(i));
        }
        List<Integer> attributes = new ArrayList<>();
        for (int i = 0; i < receitasID.size(); i++) {
            attributes.add(i);
        }
        ARFModel fileModel = new ARFModel(attributes);
        Firebase usersRef;
        usersRef = dataRef.child("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot ds) {
                usersRef.removeEventListener(this);
                ds.getChildren().forEach(i ->{
                    DataSnapshot receitasFeitas = i.child("receitasfeitas");
                    if(receitasFeitas.getChildrenCount() > 0){
                        List<Integer> column = new ArrayList<>();
                        receitasFeitas.getChildren().forEach(child ->{
                            column.add(map1.get(child.getValue()));
                        });
                        fileModel.addColumn(column);
                    }    
                });
                synchronized (FirebaseThread.this) {
                    FirebaseThread.this.notifyAll();
                }
            }

            @Override
            public void onCancelled(FirebaseError fe) {
                System.err.println("Listener was cancelled");
                 synchronized (FirebaseThread.this) {
                    FirebaseThread.this.notifyAll();
                }
            }
        });
        
        synchronized (this) {
            try {
                this.wait();

            } catch (InterruptedException ex) {
                Logger.getLogger(FirebaseThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        File temp = fileModel.createTMPFile();
        
        try {
            IA_Receitas model = new IA_Receitas(temp.getAbsolutePath());
        
        Firebase regraAplica = dataRef.child("users");
            regraAplica.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    regraAplica.removeEventListener(this);
                        
                    ds.getChildren().forEach((DataSnapshot user) -> {
                        List<String> transacoes = new ArrayList<>();
                        user.child("receitasfeitas").getChildren().forEach((receita) -> {
                            transacoes.add("at" + map1.get(receita.getKey()));
                        });
                        model.aplicaRegras(transacoes);
                        Map<String, Object> recomendacoes = new HashMap<>();
                        for(String recomendacao : model.aplicaRegras(transacoes)){
                            String rec = map2.get(Integer.parseInt(recomendacao.replace("at", "")));
                            recomendacoes.put(rec, rec);
                            
                        }
                        dataRef.child("users").child(user.getKey()).child("receitasrecomendadas").setValue(recomendacoes, new CompletionListener(){
                            @Override
                            public void onComplete(FirebaseError fe, Firebase frbs) {
                                synchronized (FirebaseThread.this) {
                                    FirebaseThread.this.notifyAll();
                                }
                            }
                        });
                    });

                    
                }

                @Override
                public void onCancelled(FirebaseError fe) {
                    System.err.println("Listener was cancelled");

                    synchronized (FirebaseThread.this) {
                        FirebaseThread.this.notifyAll();
                    }
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(FirebaseThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        synchronized (this) {
            try {
                this.wait();

            } catch (InterruptedException ex) {
                Logger.getLogger(FirebaseThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        System.exit(0);
    }
}
