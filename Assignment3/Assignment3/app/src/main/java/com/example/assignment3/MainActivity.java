package com.example.assignment3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.app.DownloadManager;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {
    final int Permission_request_code = 12345;

    int result = PackageManager.PERMISSION_DENIED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadManager dm=(DownloadManager) getSystemService(this.DOWNLOAD_SERVICE);
            AskForPermission();
            String json = "";
            InputStream is = getResources().openRawResource(R.raw.data);
            try {
                byte[] data = new byte[is.available()];
                while (is.read(data) != -1) {
//nothing needs to be done.
                }
                json = new String(data);
            } catch (IOException e) {

                e.printStackTrace();
            }
            JSONObject j = null;
            try {
                j = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jarray = null;
            try {
                jarray = j.getJSONArray("books");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jarray != null) {
                String titles[] = new String[jarray.length()];
                String subtitles[] = new String[jarray.length()];
                ;
                String description[] = new String[jarray.length()];
                ;

                String imageurls[] = new String[jarray.length()];
                String book_download_urls[] = new String[jarray.length()];

                for (int i = 0; i < jarray.length(); i++) {
                    try {
                        titles[i] = jarray.getJSONObject(i).getString("title");
                        subtitles[i] = jarray.getJSONObject(i).getString("level");
                        description[i] = jarray.getJSONObject(i).getString("info");
                        imageurls[i] = jarray.getJSONObject(i).getString("cover");
                        book_download_urls[i] = jarray.getJSONObject(i).getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                RecyclerView rv;
                rv = findViewById(R.id.rv);
                LinearLayoutManager llm = new LinearLayoutManager(this);
                rv.setLayoutManager(llm);
                RvAdapter rvAdapter = new RvAdapter(this, titles, subtitles, description, imageurls, book_download_urls,dm);
                rv.setAdapter(rvAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case Permission_request_code:
                    Toast.makeText(this,"Read/Write Permission"+ " has been " + (grantResults[0] == PackageManager.PERMISSION_GRANTED ? "Granted" : "Denied"), Toast.LENGTH_SHORT).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
public void AskForPermission(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permission_request_code);

    } else {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permission_request_code);

    }
}
    public boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }
}


