package com.example.downloadingimages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;

    public void downloadImage(View view) {
        Log.i("Button Tapped", "Ok");
         ImageDownloader task = new ImageDownloader();
         Bitmap myImage;

         try {
             myImage = task.execute("https://2img.net/h/images4.wikia.nocookie.net/__cb20121019231436/grandchase/pt-br/images/thumb/9/91/SiegNoEffect3.png/428px-SiegNoEffect3.png").get();

             imageView.setImageBitmap(myImage);
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream in = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(in);

                return  myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

}