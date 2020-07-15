package com.example.railrush;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.railrush.Adapters.TrainsRVAdapter;
import com.example.railrush.Models.Count;
import com.example.railrush.Models.Train;
import com.example.railrush.Services.CrowdInterface;
import com.example.railrush.Services.RailrushClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayTrains extends AppCompatActivity {

    private JSONArray currentStationTrains;
    private ArrayList<Train> trainsList;
    private TrainsRVAdapter trainsRVAdapter;
    private RecyclerView trainsRV;
    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_trains);
        trainsList = new ArrayList<>();
        trainsRV = findViewById(R.id.trainsRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DisplayTrains.this, RecyclerView.VERTICAL, false);
        trainsRV.setLayoutManager(linearLayoutManager);
        swipe = findViewById(R.id.trainsRVRefresh);
        updateCount();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                trainsList.clear();
                updateCount();
                trainsRVAdapter.notifyDataSetChanged();
            }
        });
    }

    public void updateCount() {
        String current = getIntent().getStringExtra("station");
        String dest = getIntent().getStringExtra("dest");
        ((TextView)findViewById(R.id.displayTrainsTitle)).setText(current+" - "+dest);
        Log.w("STRAT",current+dest);
        try {
            InputStream fileInputStream = getAssets().open("stations.json");
            ArrayList<String> stationsList = new ArrayList<>();
            int size = fileInputStream.available();
            byte[] buffer = new byte[size];
            fileInputStream.read(buffer);
            String jsonString = new String(buffer);
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject station = jsonArray.getJSONObject(i);
                if (station.getString("name").equals(current)) {
                    currentStationTrains = station.getJSONArray("trainsAndTime");
                }
            }
            for (int i = 0; i < currentStationTrains.length(); i++) {
                final JSONObject train = currentStationTrains.getJSONObject(i);
                if (train.getString("dest").equals(dest)) {
                    trainsList.add(new Train(train.getString("start"), train.getString("dest"), train.getString("time"), "", "",train.getString("trainNo")));
                }
                Collections.sort(trainsList,new Comparator<Train>() {
                    @Override
                    public int compare(Train o1, Train o2) {
                        try {
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                            long d1 = dateFormat.parse(o1.getTime()).getTime();
                            long d2 = dateFormat.parse(o2.getTime()).getTime();
                            return (int) (d1 - d2);
                        } catch (Exception e) {
                            Log.e("DATE_____________", e.toString());
                        }
                        return 0;
                    }
                });
                trainsRVAdapter = new TrainsRVAdapter(trainsList);
                trainsRV.setAdapter(trainsRVAdapter);
                swipe.setRefreshing(false);
            }
        } catch (Exception e) {
            Log.w("Count", "Error Disla" + e.toString());

        }
    }

}

