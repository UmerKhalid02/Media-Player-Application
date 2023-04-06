package com.example.audiovideoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class AudioActivity extends AppCompatActivity {

    private ImageButton playPause, skipNext, skipPrev;
    private TextView songName, artistName, cTime, eTime;
    private SeekBar seekBar;

    static MediaPlayer mediaPlayer;
    int position;
    Thread updateSeekBar;

    ArrayList<Audios> mySongs;
    String name, artist;
    long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mySongs = new ArrayList<>();

        playPause = findViewById(R.id.playPause);
        skipNext = findViewById(R.id.skipNext);
        skipPrev = findViewById(R.id.skipPrev);

        songName = findViewById(R.id.songName2);
        artistName = findViewById(R.id.artistName2);
        cTime = findViewById(R.id.currentTime);
        eTime = findViewById(R.id.endTime);

        seekBar = findViewById(R.id.seekBar);

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        id = getIntent().getLongExtra("_id", 0);
        name = getIntent().getStringExtra("song");
        artist = getIntent().getStringExtra("artist");
        position = getIntent().getIntExtra("position", 0);
        mySongs = (ArrayList<Audios>) getIntent().getSerializableExtra("songsList");

        SetTextViews(name, artist);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());

        playAudio(id);

        // buttons onClickListener
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seekBar.setMax(mediaPlayer.getDuration());

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playPause.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    playPause.setImageResource(R.drawable.pause);
                }
            }
        });

        skipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mediaPlayer.isPlaying()){
                    playPause.setImageResource(R.drawable.pause);
                }

                mediaPlayer.stop();
                mediaPlayer.release();

                position = (position + 1) % mySongs.size();

                id = mySongs.get(position).getId();
                name = mySongs.get(position).getName();
                artist = mySongs.get(position).getArtist();

                SetTextViews(name, artist);
                playAudio(id);
            }
        });

        skipPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mediaPlayer.isPlaying()){
                    playPause.setImageResource(R.drawable.pause);
                }

                mediaPlayer.stop();
                mediaPlayer.release();

                position = (position - 1) < 0 ? (mySongs.size() - 1) : (position - 1);

                id = mySongs.get(position).getId();
                name = mySongs.get(position).getName();
                artist = mySongs.get(position).getArtist();

                SetTextViews(name, artist);
                playAudio(id);
            }
        });
    }

    private void NewThread(){
        updateSeekBar = new Thread(){
            @Override
            public void run() {
                super.run();

                int totalDuration = mediaPlayer.getDuration();
                int currentPos = 0;

                while(currentPos < totalDuration){
                    try {
                        sleep(500);
                        currentPos = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPos);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void SetTextViews(String name, String artist){
        songName.setText(name);
        artistName.setText(artist);
    }

    private void playAudio(long _id){
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, _id);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), contentUri);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration());

                    NewThread();
                    updateSeekBar.start();

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            mediaPlayer.seekTo(seekBar.getProgress());
                        }
                    });
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if(mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }

                    position = (position + 1) % mySongs.size();

                    id = mySongs.get(position).getId();
                    name = mySongs.get(position).getName();
                    artist = mySongs.get(position).getArtist();

                    SetTextViews(name, artist);
                    playAudio(id);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}