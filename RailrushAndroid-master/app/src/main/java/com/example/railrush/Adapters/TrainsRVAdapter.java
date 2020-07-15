package com.example.railrush.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railrush.Models.Count;
import com.example.railrush.Models.Train;
import com.example.railrush.R;
import com.example.railrush.Services.CrowdInterface;
import com.example.railrush.Services.RailrushClient;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainsRVAdapter extends RecyclerView.Adapter<TrainViewHolder> {
    ArrayList<Train> trainsList;
    Context context;
    public TrainsRVAdapter(ArrayList<Train> trainsList) {
        this.trainsList = trainsList;
    }

    @NonNull
    @Override
    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new TrainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_train,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TrainViewHolder holder, final int position) {
        holder.start.setText(trainsList.get(position).getStart());
        holder.dest.setText(trainsList.get(position).getDest());
        holder.time.setText(trainsList.get(position).getTime());
        CrowdInterface crowdService = RailrushClient.getClient().create(CrowdInterface.class);
        Call<Count> call = crowdService.getCrowdCount(trainsList.get(position).getTrainNo());
        Log.w("Error to fetc count",trainsList.get(position).getTrainNo());

        call.enqueue(new Callback<Count>() {
            @Override
            public void onResponse(Call<Count> call, Response<Count> response) {
                holder.lastStation.setText("("+String.valueOf(response.body().getLastStation())+")");
                holder.count.setText(String.valueOf((int)response.body().getCrowdCount()));
                if(response.body().getCrowdCount() < 80)
                    holder.trainsRVItem.setBackground(context.getDrawable(R.drawable.round_corners_green));
                else
                    holder.trainsRVItem.setBackground(context.getDrawable(R.drawable.round_corners_red));
            }

            @Override
            public void onFailure(Call<Count> call, Throwable t) {
                Log.e("Error to fetc count", t.toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return trainsList.size();
    }
}
class TrainViewHolder extends RecyclerView.ViewHolder{
    TextView start,dest,time,count,lastStation;
    ConstraintLayout trainsRVItem;
    public TrainViewHolder(@NonNull View itemView) {
        super(itemView);
        trainsRVItem = itemView.findViewById(R.id.trainsRVItem);
        start = itemView.findViewById(R.id.start);
        dest = itemView.findViewById(R.id.dest);
        time = itemView.findViewById(R.id.time);
        count = itemView.findViewById(R.id.count);
        lastStation = itemView.findViewById(R.id.lastStation);
    }
}
