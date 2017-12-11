package com.example.bela.es2017.timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bela.es2017.R;
import com.example.bela.es2017.SideBarActivity;
import com.example.bela.es2017.SideBarInfo;

public class TimerActivity extends SideBarActivity {

    private EditText tempo_ipt;


    @Override
    protected SideBarInfo getInfo(){
        return new SideBarInfo("EasyFeed - Timer",R.layout.activity_timer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tempo_ipt = (EditText) findViewById(R.id.tempoIpt);
        Button play_btn = (Button) findViewById(R.id.playTimer);
        final ViewGroup rootView = (ViewGroup)findViewById(android.R.id.content);
        final AppCompatActivity act = this;

        if (StaticTimerComBotoes.alertTimer != null) {
            StaticTimerComBotoes.alertTimer.cancel();
            StaticTimerComBotoes.alertTimer = null;
            if (StaticTimerComBotoes.cTimer != null) {
                StaticTimerComBotoes.cTimer.cancel();
                StaticTimerComBotoes.cTimer = null;
            }
            new StaticTimerComBotoes(rootView,act,0);
        } else if (StaticTimerComBotoes.cTimer != null) {
            new StaticTimerComBotoes(rootView,act,0);
        }
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(tempo_ipt == null){return;}
                String aux = tempo_ipt.getText().toString();
                long t;
                try {
                    t = Long.parseLong(aux);
                } catch (NumberFormatException e) {
                    t = 1;
                }
                if (StaticTimerComBotoes.cTimer != null) {
                    StaticTimerComBotoes.cTimer.cancel();
                    StaticTimerComBotoes.cTimer = null;

                }
                if (StaticTimerComBotoes.alertTimer != null) {
                    StaticTimerComBotoes.alertTimer.cancel();
                    StaticTimerComBotoes.alertTimer = null;
                }

                new StaticTimerComBotoes(rootView,act,t);

            }
        });
    }

}
