package com.example.bela.es2017.texttospeech;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bela.es2017.R;

import java.util.Locale;

import static com.example.bela.es2017.R.id.status;

/**
 * Created by klaus on 14/10/17.
 */

public class TTSActivity  extends AppCompatActivity {

    TextToSpeech tts;
    Button btnFalar;
    EditText entrada;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);

        entrada = (EditText) findViewById(R.id.tts_editText);
        btnFalar = (Button) findViewById(R.id.tts_button);
        context = this.getApplicationContext();
        tts = new TextToSpeech(this.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(new Locale("pt","BR"));
                }
            }
        });
        btnFalar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Falou", Toast.LENGTH_LONG).show();
                String falar = entrada.getText().toString();
                tts.speak(falar, TextToSpeech.QUEUE_ADD, null);
            }
        });
    }
}
