package com.example.bela.es2017.firebase.db.runnable;

import com.example.bela.es2017.firebase.db.model.InstIngrediente;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.firebase.searcher.Searcher;
import com.example.bela.es2017.helpers.StringHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by klaus on 15/10/17.
 */

public class AQTEstoque extends AQT<InstIngrediente> {

    public AQTEstoque(Searcher adapter, DatabaseReference mDatabase, String str, Class myClass) {
        super(adapter,mDatabase,str,myClass);
    }
    @Override
    Query startingCandidates(String input){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return  mDatabase.child("users").child(uid).child("estoque");
    }
    @Override
    boolean isMatch(String input, DataSnapshot snap) {
        String snapStr = snap.child("nome").getValue(String.class);
        return snapStr.toUpperCase().startsWith(input);
    }

    @Override
    List<String> getRelevantStrings(InstIngrediente possibleMatch){
        String matchWords = possibleMatch.nome.toUpperCase();
        List<String> rs = new ArrayList<>();
        rs.add(matchWords);
        return rs;
    }
    @Override
    int calculateScore(String inputword, List<String> relStrings){
        // Create a Pattern object
        Pattern r = Pattern.compile(inputword);
        // Now create matcher object.
        Matcher mNome = r.matcher(relStrings.get(0));

        int score = 0;
        boolean foundIngr = mNome.find();
        if (foundIngr) {
            score += (mNome.start() == 0) ? 2 : 1;
        }
        return score;
    }


}
