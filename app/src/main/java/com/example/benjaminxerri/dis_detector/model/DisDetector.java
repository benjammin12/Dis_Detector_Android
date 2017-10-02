package com.example.benjaminxerri.dis_detector.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by benjaminxerri on 9/29/17.
 */

public class DisDetector {
    private String disText;

    public DisDetector(){
        disText = "";
    }

    public DisDetector(String disText){
        setDisText(disText);
    }

    public String getDisText() {
        return disText;
    }

    public void setDisText(String disText) {
        this.disText = disText;
    }

    public String parseJson(String jsonText) {
        //display the "score_tag", the "confidence" and the "irony" tag along
        Log.v("Parsing json object" , jsonText);
        String results;
        try {
            JSONObject meaningResponse = new JSONObject(jsonText);
            String score_tag = meaningResponse.getString("score_tag");
            String confidence = meaningResponse.getString("confidence");
            String irony = meaningResponse.getString("irony");

            score_tag = parseScoreTag(score_tag);

            results = "Score tag: "+score_tag + "\n" +
                             "Confidence: " + confidence + "\n" +
                              "Irony: " + irony;

            return results;
        }catch (JSONException e){
            e.printStackTrace();
            results = "Error parsing string";
            return results;
        }
    }

    private String parseScoreTag(String s){
        String results = "";
        switch (s){
            case "P+":
                results = "Strong Positive";
                break;
            case "P":
                results = "Positive";
                break;
            case "NEU":
                results = "Neutral";
                break;
            case "N":
                results = "Strong Positive";
                break;
            case "N+":
                results = "Strong Negative";
                break;
            case "NONE":
                results = "Without Sentiment.";
                break;
            default:
                results = "Error parsing results";
                break;

        }
        return results;
    }

}
