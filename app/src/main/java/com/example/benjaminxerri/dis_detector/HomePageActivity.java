package com.example.benjaminxerri.dis_detector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by benjaminxerri on 9/30/17.
 */

public class HomePageActivity extends AppCompatActivity {

    private Button speechAnalyzer;
    private Button emailAnayzer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        speechAnalyzer = (Button) findViewById(R.id.analyze_text);
        emailAnayzer = (Button) findViewById(R.id.analyze_email);

        speechAnalyzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(Intent);}
        });


        emailAnayzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(view.getContext(), GmailActivity.class);
                view.getContext().startActivity(Intent);}
        });
    }
}
