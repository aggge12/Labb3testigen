package com.example.aggge12.labb3;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.example.aggge12.labb3.R.id.timerPlayer1;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private float mLastMagnetic[];
    private float mLastAccel[];

    public boolean stopCounter = false;
    public int selectedPlayer = 1;
    public long timePlayer1millis = 0;
    public long timePlayer2millis = 0;
    public long start = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView player1 = (TextView) findViewById(R.id.timerPlayer1);
        TextView player2 = (TextView) findViewById(R.id.timerPlayer2);
        selectedPlayer = 1;
        player1.setBackgroundColor(Color.parseColor("#ADD8E6"));
        player1.setText(formatTime(0));
        player2.setText(formatTime(0));
        timePlayer1millis = 0;
        timePlayer2millis = 0;
        start = 0;


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        final SensorEventListener mEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                {
                    mLastAccel = sensorEvent.values;
                }
                else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                {
                    mLastMagnetic = sensorEvent.values;
                }

                if (mLastAccel != null && mLastMagnetic != null)
                {
                    float[] matrixR = new float[9];
                    float[] matrixI = new float[9];
                    float[] vect = new float[3];

                    SensorManager.getRotationMatrix(matrixR, matrixI, mLastAccel,mLastMagnetic);
                    SensorManager.getOrientation(matrixR,vect);

                    if (vect[2] < -1 && selectedPlayer != 1)
                    {
                        switchPlayer(1);
                    }
                    else if (vect[2] > 1 && selectedPlayer != 2)
                    {
                        switchPlayer(2);
                    }

                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        mSensorManager.registerListener(mEventListener,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mEventListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void StopCounting(View view)
    {
        stopCounter = true;
    }

    public void StartCounting(View view)
    {
        stopCounter = false;
        startCounter();
    }

    public void ResetCount(View view)
    {
        stopCounter = true;
        timePlayer1millis = 0;
        timePlayer2millis = 0;
        TextView player1 = (TextView) findViewById(R.id.timerPlayer1);
        TextView player2 = (TextView) findViewById(R.id.timerPlayer2);
        player1.setText(formatTime(0));
        player2.setText(formatTime(0));
    }
    public void switchPlayer(int player)
    {
        TextView player1 = (TextView) findViewById(R.id.timerPlayer1);
        TextView player2 = (TextView) findViewById(R.id.timerPlayer2);
        selectedPlayer = player;
        if (player == 1)
        {
            ResumeCounter(timePlayer1millis);
            player2.setBackgroundResource(0);
            player1.setBackgroundColor(Color.parseColor("#ADD8E6"));
        }
        if (player == 2)
        {
            ResumeCounter(timePlayer2millis);
            player1.setBackgroundResource(0);
            player2.setBackgroundColor(Color.parseColor("#ADD8E6"));
        }
    }

    public void ResumeCounter(long elapsed)
    {
        start = System.currentTimeMillis() - elapsed;
    }

    public void startCounter()
    {
        if (selectedPlayer == 1)
        {
            ResumeCounter(timePlayer1millis);
        }
        else
        {
            ResumeCounter(timePlayer2millis);
        }
        CountDownTimer timer1 =  new CountDownTimer(1000000, 100)
        {

            TextView player1 = (TextView) findViewById(R.id.timerPlayer1);
            TextView player2 = (TextView) findViewById(R.id.timerPlayer2);
            public void onTick(long millisUntilFinished)
            {
                if (stopCounter == true)
                {
                    this.cancel();
                }

                if (!stopCounter && selectedPlayer == 1 )
                {
                    long elapsed = System.currentTimeMillis() - start;
                    timePlayer1millis = elapsed;
                    player1.setText(formatTime(elapsed));
                }
                else if (!stopCounter)
                {
                    long elapsed = System.currentTimeMillis() - start;
                    timePlayer2millis = elapsed;
                    player2.setText(formatTime(elapsed));
                }



            }

            public void onFinish()
            {
                startCounter();
            }
        }.start();

    }

    public String formatTime(long milli)
    {
        Date date = new Date(milli);
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss:S");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC+01:00"));
        String formatted = formatter.format(date);
        return formatted;
    }



}
