package com.example.audiovideoplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
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

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class AudioPlayer extends Fragment {

    Context thisContext;
    private View view;
    MediaPlayer mediaPlayer;
    ListView listView;
    ArrayList<Audios> mySongs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_audio_player, container, false);
        thisContext = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mySongs = new ArrayList<>();
        listView = getActivity().findViewById(R.id.listView);
        Permission();
    }

    // internal storage requesting permission at runtime
    public void Permission(){
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getAudio();
                        CustomAdapter customAdapter = new CustomAdapter(thisContext, mySongs);
                        listView.setAdapter(customAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                String songName = mySongs.get(i).getName();
                                String artistName = mySongs.get(i).getArtist();
                                long id = mySongs.get(i).getId();

                                startActivity(new Intent(thisContext.getApplicationContext(), AudioActivity.class)
                                        .putExtra("_id", id).putExtra("song", songName).putExtra("artist", artistName)
                                        .putExtra("position", i).putExtra("songsList", mySongs));
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public void getAudio(){
        ContentResolver contentResolver = thisContext.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(songUri, null, null, null, null);


        if(cursor != null && cursor.moveToFirst()){
            int songTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songID = cursor.getColumnIndex(MediaStore.Audio.Media._ID);


            do{
                Audios audio = new Audios();

                String title = cursor.getString(songTitle);
                String artist = cursor.getString(songArtist);
                long id = cursor.getLong(songID);

                if(artist.equals("<unknown>")){
                    artist = "Unknown Artist";
                }

                audio.setName(title);
                audio.setArtist(artist);
                audio.setId(id);

                mySongs.add(audio);
            } while(cursor.moveToNext());

        }

    }
}