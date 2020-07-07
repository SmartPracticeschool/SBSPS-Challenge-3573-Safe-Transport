package com.example.safetransportbus;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class busStopListAdapter extends ArrayAdapter{
    //to reference the Activity
    private final Activity context;

    //stores image id for showing status of each bus stop(whether bus will stop there or not)
    private final Integer[] imageIDarray;

    private final Integer[] backgroundArray;

    //to store the list of bus stops
    private final String[] nameArray;

    //stores number no of passengers getting off at a certain destination(bus stop).
    private final String[] passengerDestination;

    public busStopListAdapter(Activity context, String[] nameArrayParam, String[] passengerDestinationParam, Integer[] imageIDArrayParam, Integer[] backgroundArray){

        super(context,R.layout.listview_row , nameArrayParam);

        this.context=context;
        this.imageIDarray = imageIDArrayParam;
        this.nameArray = nameArrayParam;
        this.passengerDestination = passengerDestinationParam;
        this.backgroundArray = backgroundArray;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null, true);

        //Get references to objects in the listview_row.xml file
        TextView nameTextField = rowView.findViewById(R.id.busStopName);
        TextView infoTextField = rowView.findViewById(R.id.pplStatus);
        ImageView imageView = rowView.findViewById(R.id.busStopStatusImg);

        //Set the values of the objects to values from the arrays
        nameTextField.setText(nameArray[position]);
        infoTextField.setText(passengerDestination[position]);
        imageView.setImageResource(imageIDarray[position]);

        if (backgroundArray[position] == 1){
            rowView.setBackgroundColor(Color.rgb(63,81,181));
        }

        return rowView;

    };
}
