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

    enum CountDownState {UNINITIALIZED,RUNNING,PAUSED,FINISHED};

    CountDownState state = CountDownState.UNINITIALIZED;
    private static final int PLAY_IMG = android.R.drawable.ic_media_play;
    private static final int PAUSE_IMG = android.R.drawable.ic_media_pause;
    private static final int DEFAULT_COLOR = android.R.color.holo_blue_dark;
    private static final int FINISHED_COLOR = android.R.color.holo_red_dark;


    private int mPageNumber;
    private int passoTime;
    private long milisLeft;
    private String descr;
    private TextToSpeech tts;
    private TextView tvDescr;
    private ImageView soundBtn;
    private TextView countDown;
    private FloatingActionButton startStopBtn,resetBtn;
    CountDownTimer cTimer;
    CountDownTimer alertTimer;

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

    //BEGIN - metodos para controlar o estado da contagem regressiva
    private void changeState(CountDownState nxt) {

        state = nxt;
        switch (state) {
            case UNINITIALIZED: onChangeToUninitialized();break;
            case RUNNING: onChangeToRunning();break;
            case PAUSED: onChangeToPaused();break;
            case FINISHED: onChangeToFinished();break;
            default:break;

        }

    }
    private void onChangeToUninitialized(){
        if (alertTimer != null) {
            alertTimer.cancel();
            alertTimer = null;
        }

        startStopBtn.setImageResource(PLAY_IMG);
        countDown.setText(getTimeLeft(passoTime));
        countDown.setTextColor(getResources().getColor(DEFAULT_COLOR));
        milisLeft = passoTime * 1000;
        cTimer = new CountDownTimer(passoTime * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                milisLeft = millisUntilFinished;
                countDown.setText(getTimeLeft(millisUntilFinished/1000));
            }

            public void onFinish() {
                milisLeft = 0;
                countDown.setText("Pronto !");
                changeState(CountDownState.FINISHED);
            }
        };
    }
    private void onChangeToFinished(){
        countDown.setTextColor(getResources().getColor(FINISHED_COLOR));
        alertTimer = new CountDownTimer(30*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                Vibrator vibra = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                long t_mili = 500;
                ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                vibra.vibrate(t_mili);
                beep.startTone(ToneGenerator.TONE_CDMA_PIP, 300);
            }

            public void onFinish() {
                changeState(CountDownState.UNINITIALIZED);
            }
        };
        alertTimer.start();
    }
    void onChangeToRunning() {
        cTimer.start();
        startStopBtn.setImageResource(PAUSE_IMG);
    }
    void onChangeToPaused() {
        cTimer.cancel();

        startStopBtn.setImageResource(PLAY_IMG);
        cTimer = new CountDownTimer(milisLeft, 1000) {

            public void onTick(long millisUntilFinished) {
                milisLeft = millisUntilFinished;
                countDown.setText(getTimeLeft(millisUntilFinished));
            }

            public void onFinish() {
                milisLeft = 0;
                countDown.setText("Pronto !");
                changeState(CountDownState.FINISHED);
            }
        };
    }
    String getTimeLeft(long sec) {
        String timeLeftStr =
                String.format("%02d:%02d:%02d", sec / 3600,
                        (sec % 3600) / 60, (sec % 60));
        return timeLeftStr;
    }
    //END - metodos para controlar o estado da contagem regressiva
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

        countDown = (TextView) cView.findViewById(R.id.passo_chrono);
        startStopBtn = (FloatingActionButton) cView.findViewById(R.id.passo_startStop);
        resetBtn = (FloatingActionButton) cView.findViewById(R.id.passo_reset);


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

        changeState(CountDownState.UNINITIALIZED);
        if (passoTime != 0) {
            cView.setVisibility(View.VISIBLE);


            //Adicionar os elementos relevantes para contagem regressiva
            startStopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (state) {
                        case UNINITIALIZED:changeState(CountDownState.RUNNING);break;
                        case RUNNING:changeState(CountDownState.PAUSED);break;
                        case PAUSED:changeState(CountDownState.RUNNING);break;
                        case FINISHED:changeState(CountDownState.UNINITIALIZED);break;
                        default:break;
                    }
                }
            });
            resetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeState(CountDownState.UNINITIALIZED);
                }
            });
        } else {
            cView.setVisibility(View.GONE);
        }








        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
