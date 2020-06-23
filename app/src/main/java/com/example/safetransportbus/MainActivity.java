package com.example.safetransportbus;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String[] nameArray = {"Andheri","WEH","Airport Road","Sakinaka","Asalpha","Ghatkopar" };

    String[] infoArray = {
            "No",
            "No",
            "2",
            "No",
            "3",
            "No"
    };

    Integer[] imageArray = {R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red,
            R.drawable.red};

    ListView listView;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final busStopListAdapter viewStops = new busStopListAdapter(this, nameArray, infoArray, imageArray);
        listView = (ListView) findViewById(R.id.busStopsListView);
        listView.setAdapter(viewStops);


        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Limit Exceeded!!!");

        final Toast toast = Toast.makeText(getApplicationContext(),
                "This is a message displayed in a Toast",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Add the line which you want to run after 5 sec.
                toast.show();
                playSound(R.raw.siren);
            }
        },5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Add the line which you want to run after 5 sec.
                imageArray[3] = R.drawable.green;
                viewStops.notifyDataSetChanged();
            }
        },5000);

    }

    private void playSound(int resId){
        mp = MediaPlayer.create(MainActivity.this, resId);
        mp.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Add the line which you want to run after 5 sec.
                mp.stop();
            }
        },3000);
    }


}