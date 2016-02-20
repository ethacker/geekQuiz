package com.example.group21.homework3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Welcome extends Activity {

    String urlbase;
    Button exitButton;
    Button startButton;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setTitle("Welcome");

        exitButton = (Button) findViewById(R.id.exitButton);
        startButton = (Button) findViewById(R.id.welcomeStartButton);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Loading Questions...");
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(false);

        startButton.setEnabled(false);

        urlbase = new String("http://dev.theappsdr.com/apis/spring_2016/hw3/index.php?qid=");

        if(isConnected()) {
            new GetQuestions().execute(urlbase);
        }else
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG);



    }

    private boolean isConnected(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo nm = cm.getActiveNetworkInfo();

        if(nm != null && nm.isConnected()){

            return true;
        }else


        return false;
    }


    public class GetQuestions extends AsyncTask<String, Void, String[]>{

        HttpURLConnection connection;
        URL url;
        StringBuilder questionBuilder;
        StringBuilder urlBuilder;
        String [] questionsBlock = new String[7];
        Questions [] questions = new Questions[7];


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.show();
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            if(questionsBlock !=null){

                for(int i = 0;i<7;i++){

                   questions[i] = new Questions(questionsBlock[i]);

                }
            }
            loadingDialog.dismiss();
            startButton.setEnabled(true);
        }

        @Override
        protected String[] doInBackground(String... params) {


            for(int i=0;i<7;i++){

                urlBuilder = new StringBuilder(params[0]);
                urlBuilder.append(String.valueOf(i));
                try {
                    url = new URL(urlBuilder.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    questionBuilder = new StringBuilder();
                    String line = "";

                    while((line = reader.readLine())!=null){

                        questionBuilder.append(line);
                    }

                    reader.close();
                    questionsBlock[i] = questionBuilder.toString();


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }





            return questionsBlock;
        }
    }


}
