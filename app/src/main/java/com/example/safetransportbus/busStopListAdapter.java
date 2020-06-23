package com.example.safetransportbus;
import android.app.Activity;
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

    //to store the animal images
    private final Integer[] imageIDarray;

    //to store the list of countries
    private final String[] nameArray;

    //to store the list of countries
    private final String[] infoArray;

    public busStopListAdapter(Activity context, String[] nameArrayParam, String[] infoArrayParam, Integer[] imageIDArrayParam){

        super(context,R.layout.listview_row , nameArrayParam);

        this.context=context;
        this.imageIDarray = imageIDArrayParam;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.busStopName);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.pplStatus);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.busStopStatusImg);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray[position]);
        infoTextField.setText(infoArray[position]+" passengers will disembark here");
        imageView.setImageResource(imageIDarray[position]);

        return rowView;

    };
}
