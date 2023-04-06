package com.example.audiovideoplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class VideoAdapter extends BaseAdapter {

    Context context;
    ArrayList<Videos> videos;
    LayoutInflater inflater;

    public VideoAdapter(Context context, ArrayList<Videos> videos) {
        this.context = context;
        this.videos = videos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.video_row, null);

        TextView TV = convertView.findViewById(R.id.videoName);
        TV.setText(videos.get(position).getName());

        return convertView;
    }
}
