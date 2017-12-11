/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package visualizareceita;


import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bela.es2017.R;
import com.example.bela.es2017.firebase.db.model.Receita;
import com.example.bela.es2017.timer.TimerComBotoes;

import java.util.Locale;

import static com.example.bela.es2017.R.id.status;

/**
 * Fragment que visualiza apenas um passo. Contem informacoes do passo, botao que faz Text-To-Speech
 * do passo, e possivelmente uma contagem regressiva de acordo com o passo
 *
 * <p>Essas classe eh usada por {@link
 * FListaPassos}.</p>
 */
public class FPasso extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    public static final String ARG_DESC = "description";
    public static final String ARG_TIME = "passoTime";



    private String descr;
    private TextToSpeech tts;
    private TextView tvDescr;
    private ImageView soundBtn;
    private TextView countDown;
    private int mPageNumber;
    private int passoTime;
    TimerComBotoes timer = null;

    private boolean isPaused = false;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static FPasso create(Receita r, int pageNumber) {
        FPasso fragment = new FPasso();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putString(ARG_DESC, r.passos.get(pageNumber).descr);
        if (r.passos.get(pageNumber).duration != null) {
            args.putInt(ARG_TIME, r.passos.get(pageNumber).duration);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public FPasso() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        passoTime = getArguments().getInt(ARG_TIME);
        descr = getArguments().getString(ARG_DESC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        // Set the title view to show the page number.
        ((TextView) rootView.findViewById(android.R.id.text1)).setText("Passo " + (1 + mPageNumber));

        soundBtn = (ImageView) rootView.findViewById(R.id.passo_button_read);


        View cView = rootView.findViewById(R.id.passo_include_timer);
        View dView = rootView.findViewById(R.id.passo_include_descr);

        tvDescr = (TextView) dView.findViewById(R.id.passo_descr);


        tvDescr.setText(this.descr);

        //Criar objeto text to speech
        tts = new TextToSpeech(this.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(new Locale("pt","BR"));
                }
            }
        });

        //Criar onClickListeners
        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String falar = tvDescr.getText().toString();
                tts.speak(falar, TextToSpeech.QUEUE_ADD, null);
            }
        });
        timer  = new TimerComBotoes(rootView,getActivity(),passoTime);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.clearTimer();
        }
    }
    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
