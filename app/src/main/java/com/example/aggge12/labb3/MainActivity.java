package com.example.aggge12.labb3;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.example.aggge12.labb3.R.id.timerPlayer1;

public class MainActivity extends AppCompatActivity {


    public int selectedPlayer = 1;
    public long timePlayer1millis;
    public long timePlayer2millis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TextView player1 = (TextView) findViewById(R.id.timerPlayer1);
        selectedPlayer = 1;



        startCounter();

    }

    public void startCounter()
    {
        long start;
        CountDownTimer timer1 =  new CountDownTimer(100000, 100)
        {



            public void onSensorChanged(SensorEvent event) {

            }


            long start = System.currentTimeMillis();
            TextView player1 = (TextView) findViewById(R.id.timerPlayer1);
            public void onTick(long millisUntilFinished)
            {
                long elapsed = System.currentTimeMillis() - start;

                if (selectedPlayer == 1 )
                {
                    timePlayer1millis = elapsed;
                    player1.setText(formatTime(elapsed));
                }
                else
                {
                    timePlayer2millis = elapsed;
                    player1.setText(formatTime(elapsed));
                }

            }

            public void onFinish()
            {

            }
        }.start();

    }

    public String formatTime(long milli)
    {
        Date date = new Date(milli);
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss:S");
        String formatted = formatter.format(date);
        return formatted;
    }



}
