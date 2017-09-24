package com.example.bela.es2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {

    private EditText tempo_ipt;

    private MyCountdownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tempo_ipt = (EditText) findViewById(R.id.tempoIpt);
        Button play_btn = (Button) findViewById(R.id.playTimer);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimerActivity.this, Timer2Activity.class);

                if(tempo_ipt == null){return;}
                String aux = tempo_ipt.getText().toString();

                intent.putExtra("time", aux);
                startActivity(intent);
            }
        });
    }

}
