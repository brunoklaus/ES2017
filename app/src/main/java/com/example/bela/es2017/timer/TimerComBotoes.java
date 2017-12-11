package com.example.bela.es2017.timer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bela.es2017.R;

import java.util.Locale;

import visualizareceita.FPasso;

import static com.example.bela.es2017.R.id.status;

/**
 * Created by klaus on 11/12/17.
 */

public class TimerComBotoes {

    enum CountDownState {UNINITIALIZED,RUNNING,PAUSED,FINISHED};

    CountDownState state = CountDownState.UNINITIALIZED;
    private static final int PLAY_IMG = android.R.drawable.ic_media_play;
    private static final int PAUSE_IMG = android.R.drawable.ic_media_pause;
    private static final int DEFAULT_COLOR = android.R.color.holo_blue_dark;
    private static final int FINISHED_COLOR = android.R.color.holo_red_dark;



    private long milisLeft;
    private FloatingActionButton startStopBtn,resetBtn;
    CountDownTimer cTimer;
    CountDownTimer alertTimer;
    private int mPageNumber;
    private long passoTime;
    ViewGroup c;
    Activity act;
    private TextView countDown;




    public TimerComBotoes(ViewGroup rootView, Activity act, long initTime) {
        this.passoTime = initTime;
        View cView = rootView.findViewById(R.id.passo_include_timer);
        startStopBtn = (FloatingActionButton) cView.findViewById(R.id.passo_startStop);
        resetBtn = (FloatingActionButton) cView.findViewById(R.id.passo_reset);
        countDown = (TextView) cView.findViewById(R.id.passo_chrono);
        startStopBtn = (FloatingActionButton) cView.findViewById(R.id.passo_startStop);
        resetBtn = (FloatingActionButton) cView.findViewById(R.id.passo_reset);
        c = rootView;
        this.act = act;


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


    }

    public void clearTimer(){
        changeState(CountDownState.UNINITIALIZED);
        cTimer = null;
        alertTimer = null;
    }

    //BEGIN - metodos para controlar o estado da contagem regressiva
    private void changeState(TimerComBotoes.CountDownState nxt) {

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
        if (cTimer != null) {
            cTimer.cancel();
        }

        startStopBtn.setImageResource(PLAY_IMG);
        milisLeft = passoTime * 1000;
        countDown.setText(getTimeLeft(milisLeft));

        countDown.setTextColor(c.getResources().getColor(DEFAULT_COLOR));



        cTimer = new CountDownTimer(passoTime * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                milisLeft = millisUntilFinished;
                countDown.setText(getTimeLeft(millisUntilFinished/1000));
            }

            public void onFinish() {
                milisLeft = 0;
                countDown.setText("Pronto !");
                changeState(TimerComBotoes.CountDownState.FINISHED);
            }
        };
    }
    private void onChangeToFinished(){
        countDown.setTextColor(c.getResources().getColor(FINISHED_COLOR));
        alertTimer = new CountDownTimer(30*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                Vibrator vibra = (Vibrator) act.getSystemService(Context.VIBRATOR_SERVICE);
                long t_mili = 500;
                ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                vibra.vibrate(t_mili);
                beep.startTone(ToneGenerator.TONE_CDMA_PIP, 300);
            }

            public void onFinish() {
                changeState(TimerComBotoes.CountDownState.UNINITIALIZED);
            }
        };
        alertTimer.start();
    }
    void onChangeToRunning() {
        if (cTimer != null) cTimer.cancel();
        cTimer  = new CountDownTimer(milisLeft, 1000) {

            public void onTick(long millisUntilFinished) {
                milisLeft = millisUntilFinished;
                countDown.setText(getTimeLeft(millisUntilFinished));
            }

            public void onFinish() {
                milisLeft = 0;
                countDown.setText("Pronto !");
                changeState(TimerComBotoes.CountDownState.FINISHED);
            }
        };


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
                changeState(TimerComBotoes.CountDownState.FINISHED);
            }
        };
    }
    String getTimeLeft(long milis) {
        long sec = milis / 1000;
        String timeLeftStr =
                String.format("%02d:%02d:%02d", sec / 3600,
                        (sec % 3600) / 60, (sec % 60));
        return timeLeftStr;
    }
    //END - metodos para controlar o estado da contagem regressiva
}
