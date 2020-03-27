package com.example.sunblind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SunblindRecyclerViewAdapter extends RecyclerView.Adapter<SunblindRecyclerViewAdapter.MyViewHolder> {

    private List<Sunblind> sunblindList = new ArrayList<>();
    private OnItemLongClickListener longClickListener;
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
        Context context = holder.nameTextView.getContext();


        holder.nameTextView.setTextColor(sunblindList.get(position).isOnline() ?
                ContextCompat.getColor(context, R.color.colorStatusOn) : ContextCompat.getColor(context, R.color.colorStatusOff));
        holder.checkImageView.setImageResource(sunblindList.get(position).isSelected() ? R.color.colorStatusOn : R.color.colorPrimaryDark);
    }

    @Override
    public int getItemCount() {
        return sunblindList.size();
    }

    /*
    sets status icons
     */
    public void setStatusIcons(String status) {
        //firstly set all sunblide status off
//        for (Sunblind sunblind : sunblindList) {
//            sunblind.setOnline(false);
//        }
        if (status.contains("-")) {
            String[] parts;
            parts = status.split("-");
            String ipFromStatus = parts[0];
            String statusFromStatus = parts[1];
            String sunblideIp;
            for (Sunblind sunblind : sunblindList) {
                sunblideIp = sunblind.getAddress();
                if (ipFromStatus.equals(sunblideIp)) {
                    if (statusFromStatus.equals(sunblideIp)){
                        sunblind.setOnline(true);
                    }
                    else {
                        sunblind.setOnline(false);
                    }
                    notifyDataSetChanged();
                }
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView nameTextView;
        final ImageView checkImageView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.sunblind_name);
            checkImageView = itemView.findViewById(R.id.imageView_check);

            //3. creating click clickListener in our holder
            //next step in MainActivity onCreate method
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (longClickListener != null && position != RecyclerView.NO_POSITION) {//crash avoiding
                    longClickListener.onItemLongClick(sunblindList.get(position));
                }
                return true;
            });
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();

                sunblindList.get(position).setSelected(!sunblindList.get(position).isSelected());

                //nameTextView.setTextColor(sunblindList.get(position).isSelected() ? Color.GREEN : Color.WHITE);
                checkImageView.setImageResource(sunblindList.get(position).isSelected() ? R.color.colorStatusOn : R.color.colorPrimaryDark);

                if (clickListener != null && position != RecyclerView.NO_POSITION) {
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
    public interface OnItemLongClickListener {
        void onItemLongClick(Sunblind sunblind);
    }
    interface OnItemClickListener {
        void onItemClick(Sunblind sunblind);
    }

    //2. create method for clickable items and chose OnItemClickListener from this package
    //do not forget to create variable clickListener in this class
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.longClickListener = listener;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
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

    public String[] getSelectedIp() {
        List<String> allSelectedIp = new ArrayList<>();
        for (int i = 0; i < sunblindList.size(); i++) {
            if (sunblindList.get(i).isSelected()) {
                allSelectedIp.add(sunblindList.get(i).getAddress());
            }
        }
        String[] simpleArray = new String[allSelectedIp.size()];
        return allSelectedIp.toArray(simpleArray);
    }

    public int getSelectedMaxRunningTime() {
        List<Integer> runningTimes = new ArrayList<>();
        for (Sunblind sunblind : sunblindList) {
            if (sunblind.isSelected()) {
                runningTimes.add(sunblind.getRunningTime());
            }
        }
        if (runningTimes.isEmpty()) {
            return 0;
        }
        else return Collections.max(runningTimes);
    }
}
