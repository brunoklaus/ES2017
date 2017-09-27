package com.example.bela.es2017.timer;

import android.content.Context;
import java.util.Calendar;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by brunogata on 23/09/17.
 */

public class MyCountdownTimer extends CountDownTimer {

    private Context context;
    private long tempo;
    private TextView tvAux;

    public MyCountdownTimer (Context context, long tempo, long intervalo, TextView tv){
        super(tempo, intervalo);
        this.context = context;
        this.tvAux = tv;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        tempo = millisUntilFinished;
        tvAux.setText(getCorrectTimer(true, millisUntilFinished) + ":" + getCorrectTimer(false, millisUntilFinished));
    }

    @Override
    public void onFinish() {
        tempo -= 1000;
        tvAux.setText(getCorrectTimer(true, tempo) + ":" + getCorrectTimer(false, tempo));
        ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        Vibrator vibra = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long t_mili = 500;
        
        vibra.vibrate(t_mili);
        beep.startTone(ToneGenerator.TONE_CDMA_PIP, 300);
    }

    private String getCorrectTimer(boolean isMinute, long millisUntilFinished){
        String aux;
        Calendar cAux = Calendar.getInstance();
        int constCalendar = isMinute ? Calendar.MINUTE : Calendar.SECOND;
        cAux.setTimeInMillis(millisUntilFinished);

        aux = cAux.get(constCalendar) < 10 ? "0" + cAux.get(constCalendar) : ""+ cAux.get(constCalendar);
        return aux;
    }
}
