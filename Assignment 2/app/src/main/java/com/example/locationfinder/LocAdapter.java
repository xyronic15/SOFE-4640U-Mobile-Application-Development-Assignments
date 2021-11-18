package com.example.locationfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//adapter to display locations on recyclerview of home
public class LocAdapter extends RecyclerView.Adapter<LocAdapter.MyViewHolder> {

//  vars for adapter
    Context context;
    ArrayList<LocModel> locations;
    onLocListener locOnLocListener;

    public LocAdapter(Context context, ArrayList<LocModel> locations, onLocListener locOnLocListener){
        this.context = context;
        this.locations = locations;
        this.locOnLocListener = locOnLocListener;
    }

    @NonNull
    @Override
    public LocAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.loc_list, parent, false);
        return new MyViewHolder(view, locOnLocListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocAdapter.MyViewHolder holder, int position) {
        LocModel location = locations.get(position);
        holder.address.setText(location.getAddress());
    }

    @Override
    public int getItemCount() {
        return this.locations.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView address;
        onLocListener locListener;

        public MyViewHolder(@NonNull View itemView, onLocListener locListener) {
            super(itemView);

            address = itemView.findViewById(R.id.listLocTitle);
            this.locListener = locListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            locListener.onLocClick(getAdapterPosition());
        }
    }

    public interface onLocListener {
        void onLocClick(int position);
    }

    public void updateAdapter(ArrayList<LocModel> locations){
        this.locations = locations;
        this.notifyDataSetChanged();
    }
}
