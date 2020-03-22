package com.example.byciclespeedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(500);

                        new LongOperation().execute();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }



    private class LongOperation extends AsyncTask<String, Void, String> {
        String km_per_hour = null;

        @Override
        protected String doInBackground(String... params) {
            try{
                getHtml();
            }catch (Exception err){
                Log.e("Some error", "doInBackground: ", err);
            }


            return null;
        }

        public void getHtml() throws IOException
        {

            URL url = new URL("http://192.168.4.1/input.html");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                byte[] kms_buff = new byte[4];
                if(in.read(kms_buff)>0){
                    km_per_hour = new String(kms_buff);
                }

                Log.d("Data we got", km_per_hour);
            } finally {
                urlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            TextView txt = findViewById(R.id.txt1);
            txt.setText(km_per_hour+" km/h");

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
