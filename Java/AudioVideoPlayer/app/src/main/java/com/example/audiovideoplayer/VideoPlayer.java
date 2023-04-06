package com.example.audiovideoplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class VideoPlayer extends Fragment {

    Context thisContext;
    ArrayList<Videos> myVideos;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisContext = container.getContext();
        return inflater.inflate(R.layout.fragment_video_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myVideos = new ArrayList<>();
        getVideos();

        listView = view.findViewById(R.id.videoListView);

        VideoAdapter videoAdapter = new VideoAdapter(thisContext, myVideos);
        listView.setAdapter(videoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(thisContext.getApplicationContext(), VideoActivity.class);
                intent.putExtra("id", myVideos.get(position).getId());
                startActivity(intent);
            }
        });

    }

    public void getVideos(){
        ContentResolver contentResolver = thisContext.getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);


        if(cursor != null && cursor.moveToFirst()){
            int videoTitle = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            int videoId = cursor.getColumnIndex(MediaStore.Video.Media._ID);


            do{
                Videos video = new Videos();
                String title = cursor.getString(videoTitle);
                long id = cursor.getLong(videoId);

                video.setName(title);
                video.setId(id);

                myVideos.add(video);

            } while(cursor.moveToNext());

        }

    }

}