package com.example.bela.es2017.Add_receita;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Created by klaus on 22/10/17.
 */

public class PassoEditText extends AppCompatEditText {
    private int passo = 0;

    public String getNumPassoStr() {
        return Integer.toString(passo) + ". ";
    }

    public void incrementPasso(){
        passo++;
    }
    public void decrementPasso(){
        passo--;
    }


    public int getPasso(){return passo;}
    public void setPasso(int p){this.passo = p;}


    private void corrigeRange(){
        String passoStr = this.getNumPassoStr();
        int start = this.getSelectionStart();
        int end = this.getSelectionEnd();
        if (passoStr.length() > start) start = passoStr.length();
        if (passoStr.length()  > end ) end = passoStr.length();

        this.setSelection(start, end);
    }


    public PassoEditText(Context context) {
        super(context);
        init();
    }

    public PassoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PassoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        /** BEGIN - Adicionar varios listeners para que o prefixo nao possa ser selecionado
         * ou mudaddo
         */
        final SpanWatcher watcher = new SpanWatcher() {
            @Override
            public void onSpanAdded(final Spannable text, final Object what,
                                    final int start, final int end) {
                corrigeRange();
            }

            @Override
            public void onSpanRemoved(final Spannable text, final Object what,
                                      final int start, final int end) {
                corrigeRange();
            }

            @Override
            public void onSpanChanged(final Spannable text, final Object what,
                                      final int ostart, final int oend, final int nstart, final int nend) {
                corrigeRange();
            }
        };
        this.getText().setSpan(watcher,0,0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        this.addTextChangedListener(new TextWatcher() {
            CharSequence oldText;
            boolean flag = false;
            @Override
            public void afterTextChanged(Editable s) {
                if (flag == true) return;
                String passoStr = getNumPassoStr();
                if (s.length() >= passoStr.length() &&
                        passoStr.equals(s.subSequence(0, passoStr.length()).toString())) {

                } else {
                    flag = true;

                    PassoEditText.super.setText(oldText.toString().toCharArray(), 0, oldText.length());

                    flag = false;
                    corrigeRange();

                }
                PassoEditText.super.getText().setSpan(watcher,0,0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                oldText = PassoEditText.super.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        ;
        /** END - Adicionar varios listeners para que o prefixo nao possa ser selecionado
         * ou mudaddo
         */
    }
}
