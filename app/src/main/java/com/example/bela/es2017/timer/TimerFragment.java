package com.example.bela.es2017.timer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bela.es2017.R;


public class TimerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private EditText tempo_ipt;

    private MyCountdownTimer timer;
    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.activity_timer, container, false);
        tempo_ipt = (EditText) layout.findViewById(R.id.tempoIpt);
        Button play_btn = (Button) layout.findViewById(R.id.playTimer);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimerFragment.this.getActivity(), Timer2Activity.class);

                if(tempo_ipt == null){return;}
                String aux = tempo_ipt.getText().toString();

                intent.putExtra("time", aux);
                startActivity(intent);
            }
        });
        return layout;
    }

}
