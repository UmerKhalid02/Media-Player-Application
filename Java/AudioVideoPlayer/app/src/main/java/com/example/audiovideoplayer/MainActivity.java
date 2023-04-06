package com.example.audiovideoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    private ImageButton frag1, frag2, frag3;
    boolean _switch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frag1 = findViewById(R.id.audioBtn);
        frag2 = findViewById(R.id.videoBtn);

        replaceFragment(new AudioPlayer());

        frag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_switch) {
                    replaceFragment(new AudioPlayer());
                    _switch = false;
                }
            }
        });

        frag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!_switch){
                    replaceFragment(new VideoPlayer());
                    _switch = true;
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}