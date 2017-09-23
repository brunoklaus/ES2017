package com.example.bela.es2017.timer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bela.es2017.R;

public class Timer2Activity extends AppCompatActivity {

    private MyCountdownTimer timer;
    String tempo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer2);
        tempo = getIntent().getExtras().getString("time");
    }

    @Override
    public void onResume(){
        super.onResume();
        long t;
        try {
            t = Long.parseLong(tempo);
        } catch (NumberFormatException e) {
            t = 1;
        }
        TextView contador = (TextView) findViewById(R.id.cronometro2);
        timer = new MyCountdownTimer(this, t * 1000, 1000, contador);
        timer.start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(timer != null){
            timer.cancel();
        }
    }
}
