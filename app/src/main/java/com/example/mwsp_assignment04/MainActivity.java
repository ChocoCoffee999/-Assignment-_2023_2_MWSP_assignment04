package com.example.mwsp_assignment04;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;



public class MainActivity extends AppCompatActivity {

    ImageView imgView;
    String imageUrl = "http://kiokahn.synology.me:30000/uploads/-/system/appearance/logo/1/Gazzi_Labs_CI_type_B_-_big_logo.png";
    Bitmap bmImg = null;
    /*CLoadImage task;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView) findViewById(R.id.imgView);
        System.out.println("HIHIHIHIHIHIHIHI");
        Log.i("info", "asdfasdfasdf'");

        //task = new CLoadImage();
    }

    public void onClickForLoad(View v) {
        //task.execute(imageUrl+"uploads/-/system/appearance/logo/1/Gazzi_Labs_CI_type_B_-_big_logo.png");

        loadBitmapFromUrl(imageUrl)
                .subscribe(
                        bitmap -> {
                            bmImg = bitmap;
                            System.out.print("Download_file : ");
                            System.out.println(bitmap);
                            imgView.setImageBitmap(bmImg);

                        },
                        error -> {
                            System.out.println("A");
                            // 에러 처리
                            error.printStackTrace();
                        }
                );
        if (bmImg!=null) {
            imgView.setImageBitmap(bmImg);
            System.out.println("C");
        }
        else {
            System.out.println("D");
            //imgView.setImageResource(R.drawable.result2);
        }

        Toast.makeText(getApplicationContext(), "Load", Toast.LENGTH_LONG).show();
    }

    public void onClickForSave(View v) {
        if (bmImg!=null) {
            saveBitmapToJpeg(bmImg, "DCIM", "image");
        }
        else {
            Log.i("error", "bming == null");
        }

        Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_LONG).show();
    }

/*    private class CLoadImage extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String[] urls) {
            try {
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return bmImg;
        }

        protected void onPostExecute(Bitmap img) {
            imgView.setImageBitmap(bmImg);
        }
    }*/

    public static void saveBitmapToJpeg(Bitmap bitmap, String folder, String name) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder_name = "/" + folder + "/";
        String file_name = name + ".jpg";
        String string_path = ex_storage + folder_name;
        Log.d("경로", string_path);

        File file_path;
        file_path = new File(string_path);

        if (!file_path.exists()) {
            file_path.mkdirs();
        }

        try {
            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
    }

    public static Observable<Bitmap> loadBitmapFromUrl(String imageUrl) {
        return Observable.fromCallable(() -> {
            System.out.println(imageUrl);
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            System.out.println("Success");
            // 이미지 스트림을 읽어옵니다.
            InputStream input = connection.getInputStream();
            System.out.println(connection.getInputStream());
            // 스트림에서 비트맵을 디코딩합니다.
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            if (bitmap == null) {
                System.out.println("B1");
                Log.i("null","bitmap is null");
            }
            else {
                System.out.println("B2");
                Log.i("null","bitmap is null");
            }

            //input.close();

            return bitmap;
        }).subscribeOn(Schedulers.io());
    }

}