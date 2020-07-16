package com.example.assignment3;
import android.Manifest;
import android.app.DownloadManager;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;



public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {


    private String baseurl="https://raw.githubusercontent.com/revolunet/PythonBooks/master/";
    private Context context;
    private String[] main;
    private String[] subt;
    private String[] descri;
    private String[] imgurl;
    private String[] download_URLS;
    private DownloadManager downloadManager;



    public RvAdapter(Context context,String[] main,String[] Subtitle,String[] description,String[] imgurl,String[] download_urls,DownloadManager dm){
        this.context=context;
        this.main=main;
        this.subt=Subtitle;
        this.descri=description;
        this.imgurl=imgurl;
        this.download_URLS=download_urls;
        this.downloadManager=dm;



    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View row= LayoutInflater.from(context).inflate(R.layout.list_item,null);    //View row= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        ViewHolder vh=new ViewHolder(row);
        return vh;
    }
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(main[position]);
        holder.subtitle.setText(subt[position]);
        holder.description.setText(descri[position]);
        final String format=download_URLS[position].substring((download_URLS[position].length()-3));
        if(format.equals("pdf") || format.equals("zip")){
            holder.btn.setText("Download");

            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isStoragePermissionGranted()){
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(download_URLS[position]));
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                    request.setAllowedOverRoaming(false);
                    request.setTitle(main[position]);
                    request.setDescription("Downloading " + main[position]);

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/PyhtonBooks/"  + "/" + main[position] +".s"+format);
                    downloadManager.enqueue(request);
                    }else{
                        Toast.makeText(context, "Please Give Permissions to Download", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            holder.btn.setText("Read Online");
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(download_URLS[position]));
                        context.startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No application can handle this request."
                                + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
        }
        Picasso.get().load(baseurl+imgurl[position]).into(holder.img, new Callback() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(context,"Img Failed="+main[position],Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return main.length;
    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView subtitle;
        public TextView description;
        public Button btn;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.title);
            subtitle=itemView.findViewById(R.id.difficulty);
            description=itemView.findViewById(R.id.description);
            btn=itemView.findViewById(R.id.btn);

        }
    }
    public boolean isStoragePermissionGranted() {
        int result=PackageManager.PERMISSION_DENIED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }
}

