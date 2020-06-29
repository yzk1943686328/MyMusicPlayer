package com.example.mymusicplayer;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //声明变量
    Button playbutton;
    Button pausebutton;
    Button restartbutton;
    Button loopbutton;
    SeekBar seekbar;
    TextView currenttime;
    TextView totaltime;

    MediaPlayer mediaplayer;

    Handler handler;
    Runnable runnable;

    //初始化各组件
    public void initviews(){
        playbutton=findViewById(R.id.playbutton);
        pausebutton=findViewById(R.id.pausebutton);
        restartbutton=findViewById(R.id.restartbutton);
        loopbutton=findViewById(R.id.loopbutton);
        seekbar=findViewById(R.id.seekbar);
        currenttime=findViewById(R.id.currenttime);
        totaltime=findViewById(R.id.totaltime);
    }

    //初始化MediaPlayer
    public void initMediaPlayer(){
        mediaplayer=new MediaPlayer();

        try {

            AssetFileDescriptor fileDescriptor=getAssets().openFd("prettything.mp3");
            mediaplayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(), fileDescriptor.getLength());

            mediaplayer.prepare();


        } catch (IOException e) {
            currenttime.setText(e.getMessage());

        }

    }



    //设置总时间
    public void setTotalTime(){

        int totalmsecond=mediaplayer.getDuration();
        int totalsecond=Math.round(totalmsecond/1000);
        String str = String.format("%02d:%02d", totalsecond / 60, totalsecond % 60);

        totaltime.setText(str);

        seekbar.setMax(totalmsecond);

    }

    //建立新线程设置进度条随音乐播放而改变
    public void setSeekbarListener(){

        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {

                int currentmsecond=mediaplayer.getCurrentPosition();
                int currendsecond=Math.round(currentmsecond/1000);

                String str=String.format("当前时间 %02d:%02d",currendsecond/60,currendsecond%60);
                currenttime.setText(str);

                seekbar.setProgress(currentmsecond);

                handler.postDelayed(runnable,1000);
            }
        };

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initviews();
        initMediaPlayer();

        setTotalTime();
        setSeekbarListener();

        //设置事件监听器
        //播放
        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.start();
                handler.post(runnable);
            }
        });

        //暂停
        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.pause();
            }
        });

        //重新开始
        restartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.seekTo(0);
            }
        });

        //循环播放
        loopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.setLooping(true);
            }
        });

        //为进度条设置进度条改变监听器
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaplayer.seekTo(seekBar.getProgress());

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}