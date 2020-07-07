package com.example.safetransportbus;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //to store the list of bus stops
    String[] nameArray = {"Andheri","WEH","Airport Road","Sakinaka","Asalpha","Ghatkopar" };

    //stores number no of passengers getting off at a certain destination(bus stop).
    String[] passengerDestination = {
            "0",
            "0",
            "0",
            "0",
            "0",
            "0"
    };

    //stores image id for showing status of each bus stop(whether bus will stop there or not)
    Integer[] imageArray = {R.drawable.redcircle,
            R.drawable.redcircle,
            R.drawable.redcircle,
            R.drawable.redcircle,
            R.drawable.redcircle,
            R.drawable.redcircle};

    Integer[] backgroundArray = {0, 0, 0, 0, 0, 0};

    ListView listView;
    MediaPlayer mp;
    busStopListAdapter viewStops;
    int occupied=0, nextStopCount=0,nextStopIndex=-1, total=6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Populate listview with data from above arrays using the busStopListAdapter class
        viewStops = new busStopListAdapter(this, nameArray, passengerDestination, imageArray, backgroundArray);
        listView = findViewById(R.id.busStopsListView);
        listView.setAdapter(viewStops);


        //Bus is at location 0.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeCurrentStop(0);
            }
        },3000);

        //siren
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                capacityFilledIndicator();
            }
        },4000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                calculateNextStop();
                showSnackBar();
            }
        },17000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeCurrentStop(nextStopIndex);
            }
        },20000);
    }

    //function to calculate availability to be generated at next stop and empty seats rn.
    private void calculateNextStop(){
        int index=0;

        for(int i=0;i<backgroundArray.length;i++){
            if(backgroundArray[i] == 1){
                Log.d("OUTER LOOP "+i," :"+backgroundArray[i]);
                index = i;
                break;
            }
        }

        for(int i=(index+1);i<backgroundArray.length;i++){
            if(!passengerDestination[i].equals("0")){
                Log.d("INNER LOOP "+i," :"+passengerDestination[i]);
                index = i;
                nextStopIndex = i;
                break;
            }
        }
        //nextStopIndex=1;
        nextStopCount = Integer.parseInt(passengerDestination[index]);
    }

    //function to generate and show snackbar
    private void showSnackBar(){

        final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Vacant seats: "+(total - occupied)+"\n, Availability at next stop: "+nextStopCount, 10000);

        snackBar.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your action method here
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }

    //function that shows toast indicating that capacity has been fulfilled and plays siren for 3 secs.
    private void capacityFilledIndicator(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.text);
        text.setText("Bus Limit has been Fulfilled");

        final Toast toast = Toast.makeText(getApplicationContext(),
                "Bus Limit has been Fulfilled",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layout);
        toast.show();
        playSound(R.raw.siren);

    }

    //function that plays siren for 3 secs
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

    //function to increment passengerDestination Count for a particular bus stop
    public void Increment(View view) {
        LinearLayout parentRow = (LinearLayout) view.getParent();

        TextView busStop = parentRow.findViewById(R.id.busStopName);
        String stopName = busStop.getText().toString();


        int position = getPosition(stopName);
        int temp = Integer.parseInt(passengerDestination[position]);
        temp += 1;
        occupied +=1;

        if(temp == 1){
            imageArray[position] = R.drawable.greencircle;
        }
        passengerDestination[position]=String.valueOf(temp);
        viewStops.notifyDataSetChanged();

    }

    //function to decrement passengerDestination Count for a particular bus stop
    public void Decrement(View view) {

        LinearLayout parentRow = (LinearLayout) view.getParent();
        TextView busStop = parentRow.findViewById(R.id.busStopName);
        String stopName = busStop.getText().toString();


        int position = getPosition(stopName);
        int temp = Integer.parseInt(passengerDestination[position]);
        if(temp!=0) {
            temp -= 1;
        }

        if(temp == 0){
            imageArray[position] = R.drawable.redcircle;
        }
        passengerDestination[position]=String.valueOf(temp);
        viewStops.notifyDataSetChanged();

    }

    //indicates which bus stop the bus is currently at.
    private void changeCurrentStop(int index){
       for(int i=0;i<backgroundArray.length;i++){
           backgroundArray[i] = 0;
       }
        backgroundArray[index] = 1;

       for(int i=0;i<index;i++){
           imageArray[i]=R.drawable.redcircle;
           occupied -= Integer.parseInt(passengerDestination[i]);
           passengerDestination[i]=String.valueOf(0);
       }

       viewStops.notifyDataSetChanged();
    }

    //function to return index number for a bus stop name.
    public int getPosition(String name){
        for(int i=0;i<nameArray.length;i++){
            if(nameArray[i].equals(name))
                return i;
        }
        return 0;
    }
}