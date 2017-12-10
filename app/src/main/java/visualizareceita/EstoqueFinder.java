package visualizareceita;

import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe usada para manter o estoque da usuario
 * Created by klaus on 30/10/17.
 */

public class EstoqueFinder {

    static List<InstIngrediente> estoque;
    static List<Searcher<InstIngrediente> > l;
    DatabaseReference mDatabase;
    static boolean addingToEstoque = false;
    String uid;
    public EstoqueFinder() {
        l = Collections.synchronizedList(new ArrayList<Searcher<InstIngrediente>>());


        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Procura o estoque no usuario no Firebase (se ainda nao tiver)
     * @param adapter
     */
    public void getEstoque(Searcher<InstIngrediente> adapter){

        synchronized (EstoqueFinder.class){
            l.add(adapter);
            if(estoque != null) {
                if (addingToEstoque) {
                    return;
                } else {
                    for (Searcher<InstIngrediente> s : l)
                        s.onSearchFinished(null, estoque, null, true);
                }
            } else {
                addingToEstoque = true;
                estoque = new ArrayList<>();
            }
        }
        mDatabase.child("users").child(uid).
                child("estoque")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            estoque.add(snapshot.getValue(InstIngrediente.class));
                        }
                        setEstoqueBool(false);
                        for (Searcher<InstIngrediente> s : l)
                            s.onSearchFinished(null, new ArrayList<InstIngrediente>(estoque), null, false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        for (Searcher<InstIngrediente> s : l)
                            s.onSearchFinished(null, new ArrayList<InstIngrediente>(estoque), null, false);
                    }

                });
    }
    private void setEstoqueBool(boolean b) {
        synchronized (EstoqueFinder.class){
            addingToEstoque = b;
        }
    }




}
