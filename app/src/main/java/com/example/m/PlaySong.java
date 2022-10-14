package com.example.m;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.start();
        updateSeek.interrupt();
    }
TextView textView;
ImageView pause,next,previous;
SeekBar seekBar;
ArrayList<File> songs;
MediaPlayer mediaPlayer;
String textContent;
Thread updateSeek;
int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView=findViewById(R.id.textView);
        seekBar=findViewById(R.id.seekBar);
        pause=findViewById(R.id.pause);
        next=findViewById(R.id.next);
        previous=findViewById(R.id.previous);
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        bundle.getParcelableArrayList("mySongs");
        textContent=i.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position=i.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
          mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
     updateSeek=new Thread(){

         @Override
         public void run() {
             super.run();
             int currentPosition=0;
             try{
                 while (currentPosition< mediaPlayer.getDuration()){
                     currentPosition=mediaPlayer.getCurrentPosition();
                     seekBar.setProgress(currentPosition);
                     sleep(800);
                 }
             }
             catch (Exception e){
                 e.printStackTrace();
             }
             updateSeek.start();
         }
     };
   pause.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           if(mediaPlayer.isPlaying()){
               mediaPlayer.pause();
               pause.setImageResource(R.drawable.play);
           }
       }
   });
   previous.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           mediaPlayer.stop();
           mediaPlayer.release();
           if(position!=0){
               position=position-1;
           }
           else{
               position= songs.size()-1;
           }
           Uri uri=Uri.parse(songs.get(position).toString());
           mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
           mediaPlayer.start();
           seekBar.setMax(mediaPlayer.getDuration());
           textContent=songs.get(position).getName();
           textView.setText(textContent);
       }
   });
   next.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           mediaPlayer.stop();
           mediaPlayer.release();
           if(position!=songs.size()-1){
               position=position+1;
           }
           else{
               position=0;
           }
           Uri uri=Uri.parse(songs.get(position).toString());
           mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
           mediaPlayer.start();
           seekBar.setMax(mediaPlayer.getDuration());
           textContent=songs.get(position).getName();
           textView.setText(textContent);
       }
   });


    }

}