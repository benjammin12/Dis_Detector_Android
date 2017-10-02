package com.example.benjaminxerri.dis_detector;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.benjaminxerri.dis_detector.model.DisDetector;
import com.example.benjaminxerri.dis_detector.model.GmailClient;
import com.example.benjaminxerri.dis_detector.utilities.Network;

import java.net.URL;

import static com.example.benjaminxerri.dis_detector.utilities.Network.buildURL;
import static com.example.benjaminxerri.dis_detector.utilities.Network.getResponseFromHttpUrl;

/**
 * Created by benjaminxerri on 10/1/17.
 */

public class GmailActivity extends AppCompatActivity {

    private EditText inputUserName;
    private EditText inputUserPassword;
    private Button searchButton;
    private TextView resultsText;
    private String userEmail;
    private String userPassword;
    ProgressBar progressBar;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gmail_activity);

        inputUserName = (EditText) findViewById(R.id.username);
        inputUserPassword = (EditText) findViewById(R.id.password);
        searchButton = (Button) findViewById(R.id.search_button);
        resultsText = (TextView) findViewById(R.id.results_text);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        searchButton.setOnClickListener(checkEmail);


    }

    public class SearchGmail extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            GmailClient gmail = new GmailClient(userEmail, userPassword);
            String lastEmail = "";
            try {
                if(gmail.getEmail().equals("") || gmail.getPassword().equals("")){
                    return null;
                }else {
                    lastEmail = gmail.readMail();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lastEmail;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);

            if(s != null && !s.equals("")) {
                //build another url
                URL url = Network.buildURL(s);

                //execute this async class
                Log.d("URL is ", url.toString());
                new SearchDis().execute(url);
            } else {
                resultsText.setText("Error Building Sentiment Analysis Text");
            }
        }


    }

    public class SearchDis extends AsyncTask<URL, Void, String> {
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

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            if(s != null && !s.equals("")){
                s = new DisDetector().parseJson(s);
                resultsText.setText("Your last email was evaluated Successfully!" +"\n\n" + s);
            }
            else {
                resultsText.setText("Error Building Sentiment Analysis Text");
            }
        }

        //anonymous function for search button


    }

    private View.OnClickListener checkEmail = new View.OnClickListener() {
        public void onClick(View v) {

            userEmail = inputUserName.getText().toString();
            userPassword = inputUserPassword.getText().toString();
            new SearchGmail().execute();
        }
    };
}


