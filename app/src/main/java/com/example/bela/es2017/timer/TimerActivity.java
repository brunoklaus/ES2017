package com.example.bela.es2017.timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bela.es2017.R;
import com.example.bela.es2017.SideBarActivity;
import com.example.bela.es2017.SideBarInfo;

public class TimerActivity extends SideBarActivity {

    private EditText tempo_ipt;

    private MyCountdownTimer timer;

    @Override
    protected SideBarInfo getInfo(){
        return new SideBarInfo("EasyFeed - Timer",R.layout.activity_timer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
