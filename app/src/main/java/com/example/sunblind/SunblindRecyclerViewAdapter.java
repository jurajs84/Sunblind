package com.example.sunblind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SunblindRecyclerViewAdapter extends RecyclerView.Adapter<SunblindRecyclerViewAdapter.MyViewHolder> {

    private List<Sunblind> sunblindList = new ArrayList<>();
    private OnItemClickListener clickListener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Sunblind currentSunblind = sunblindList.get(position);
        holder.nameTextView.setText(currentSunblind.getName());
    }

    @Override
    public int getItemCount() {
        return sunblindList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.sunblind_name);

            //3. creating click clickListener in our holder
            //next step in MainActivity onCreate method
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {//crash avoiding
                    clickListener.onItemClick(sunblindList.get(position));
                }
            });
        }
    }

    //helper to update sunblindList
    public void setSunblindList(List<Sunblind> sunblindList) {
        this.sunblindList = sunblindList;
        notifyDataSetChanged();
    }

    //1. interface for add clickable items
    public interface OnItemClickListener{
        void onItemClick(Sunblind sunblind);
    }

    //2. create method for clickable notes and chose OnItemClickListener from this package
    //do not forget to create variable clickListener in this class
    public void setOnItemClickListener(OnItemClickListener listener){
        this.clickListener = listener;
    }

    public Sunblind getSunblindAtPosition(int position) {
        return sunblindList.get(position);
    }

    /**
     * this method is for getting all ip addresses for controlling all blinds at one time
     * @return String array of ip addresses
     */
    public String[] getAllIp() {
        String[] allIp = new String[sunblindList.size()];
        for (int i = 0; i < sunblindList.size(); i++) {
            allIp[i] = sunblindList.get(i).getAddress();
        }
        return allIp;
    }

    public int getMaxRunningTime() {
        List<Integer> runningTimes = new ArrayList<>();
        for (Sunblind sunblind : sunblindList) {
            runningTimes.add(sunblind.getRunningTime());
        }
        return Collections.max(runningTimes); // return max value of all sun blinds
    }
}
