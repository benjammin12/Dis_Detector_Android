package com.example.benjaminxerri.dis_detector;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.benjaminxerri.dis_detector.model.DisDetector;
import com.example.benjaminxerri.dis_detector.utilities.Network;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.benjaminxerri.dis_detector.utilities.Network.getResponseFromHttpUrl;


public class MainActivity  extends AppCompatActivity {
    private final int SPEECH_RECOGNITION_CODE = 1;
    private EditText inputValue;
    private TextView textView;
    private Button searchButton;
    private ProgressBar progressBar;
    private ImageButton microphone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputValue = (EditText) findViewById(R.id.text_to_analyze);
        textView = (TextView) findViewById(R.id.results_text);
        searchButton = (Button) findViewById(R.id.search_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        microphone = (ImageButton) findViewById(R.id.microphone);

        microphone.setOnClickListener(recordAudio);
        searchButton.setOnClickListener(searchDis);

    }

    public class SearchDis extends AsyncTask<URL,Void,String> {
        @Override
        protected String doInBackground(URL... urls) {
            String toReturn = "DID NOT WORK";
            try {
                Log.v("Logging url:", urls[0].toString());
                toReturn = getResponseFromHttpUrl(urls[0]);

            } catch (Exception e) {
                Log.d("Error", "exception on get Response from HTTP call" + e.getMessage());
                return null;
            }

            return toReturn;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        //what do in background returns,
        // onPostExecute must take in,
        // because doInBackground automatically calls onPostExecute

        @Override
        protected void onPostExecute (String s){
            progressBar.setVisibility(View.INVISIBLE);
            if(s != null && !s.equals("")){
                s = new DisDetector().parseJson(s);
                textView.setText(s);
            }
            else {
                textView.setText("Error Building Sentiment Analysis Text");
            }
        }

    }

    private void SpeachToText(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    inputValue.setText(text);
                }
                break;
            }
        }
    }



    //anonymous function for search button
    private View.OnClickListener searchDis = new View.OnClickListener() {
        public void onClick(View v) {
            String apiParameter = inputValue.getText().toString();
            new SearchDis().execute(buildUri(apiParameter));
        }
    };

    private View.OnClickListener recordAudio = new View.OnClickListener() {
        public void onClick(View v){
            SpeachToText();
        }
    };


    public static URL buildUri(String s){
        URL url =  Network.buildURL(s);
        return url;
    }




}

