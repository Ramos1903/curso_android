package com.example.baixarlistanet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebURLs = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();

    ImageView imageView;
    Button button0;
    Button button1;
    Button button2;
    Button button3;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data =reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;

                    data = reader.read();
                }

                return  result;

            } catch (Exception e) {
                e.printStackTrace();
                return "Falied";
            }

        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {


            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button0);
        button2 = findViewById(R.id.button1);
        button3 = findViewById(R.id.button2);

        DownloadTask task = new DownloadTask();
        DownloadImage image = new DownloadImage();

        String result = null;

        try {
            result = task.execute("https://www.boxymo.ie/news/most-expensive-cars-in-the-world.aspx/").get();

            String[] splitResult = result.split("<head>");

            Pattern p = Pattern.compile("<title>(.*?)</");
            Matcher m = p.matcher(splitResult[0]);

            while (m.find()) {
                celebNames.add(m.group(1));
                System.out.println(m.group(1));
                Log.i("m.group", m.group(1));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Result", result);
        System.out.println("HELLO");
        System.out.println("har");

    }
}