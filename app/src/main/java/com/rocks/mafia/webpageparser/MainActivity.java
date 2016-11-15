package com.rocks.mafia.webpageparser;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    public String data = "";
    public boolean isButtonPressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            isButtonPressed = (boolean) savedInstanceState.getSerializable("isButtonPressed");
            data = (String) savedInstanceState.getSerializable("data");
            if (isButtonPressed && data.isEmpty()) {
                FetchWebpage fetchWebpage = new FetchWebpage(MainActivity.this);
                fetchWebpage.execute();
            }

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(data);
        }


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isButtonPressed = true;
                FetchWebpage fetchWebpage = new FetchWebpage(MainActivity.this);
                fetchWebpage.execute();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("isButtonPressed", isButtonPressed);
        outState.putSerializable("data", data);
        super.onSaveInstanceState(outState);
    }

    public class FetchWebpage extends AsyncTask<Object, Object, String> {
        String Content;
        private final Context Asyntaskcontext;

        public FetchWebpage(Context context){
            Asyntaskcontext = context;
        }
        @Override
        protected void onPostExecute(String str) {
            //Log.e("GAGAGA", Content);
            MainActivity mainactivity = (MainActivity) Asyntaskcontext;
            mainactivity.data = Content;
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(data);
            super.onPostExecute(Content);
        }

        @Override
        protected String doInBackground(Object... voids) {
            HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
            HttpGet httpget = new HttpGet("https://www.iiitd.ac.in/about"); // Set the action you want to do
            HttpResponse response = null; // Executeit
            String resString = ""; // Result is here

            try {
                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent(); // Create an InputStream with the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                int n = 10;
                while ((line = reader.readLine()) != null || n-- > 0) // Read line by line
                    sb.append(line + "\n");
                is.close(); // Close the stream
                resString = sb.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }
            Content = resString;
            return null;
        }

    }
}
