package com.example.audiovideoplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Audios> songs;
    LayoutInflater inflater;

    public CustomAdapter(Context context, ArrayList<Audios> songs) {
        this.context = context;
        this.songs = songs;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.row, null);

        TextView TV = view.findViewById(R.id.songName);
        TextView ATV = view.findViewById(R.id.artistName);

        TV.setText(songs.get(i).getName());
        ATV.setText(songs.get(i).getArtist());

        return view;
    }
}
