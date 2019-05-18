package com.example.sunblide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SunblideRecyclerViewAdapter extends RecyclerView.Adapter<SunblideRecyclerViewAdapter.MyViewHolder> {

    private List<Sunblide> sunblideList = new ArrayList<>();
    private OnItemClickListener clickListener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Sunblide currentSunblide = sunblideList.get(position);
        holder.nameTextView.setText(currentSunblide.getName());
    }

    @Override
    public int getItemCount() {
        return sunblideList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.sunblide_name);

            //3. creating click clickListener in our holder
            //next step in MainActivity onCreate method
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {//crash avoiding
                    clickListener.onItemClick(sunblideList.get(position));
                }
            });
        }
    }

    //helper to update sunblideList
    public void setSunblideList (List<Sunblide> sunblideList) {
        this.sunblideList = sunblideList;
        notifyDataSetChanged();
    }

    //1. interface for add clickable items
    public interface OnItemClickListener{
        void onItemClick(Sunblide sunblide);
    }

    //2. create method for clickable notes and chose OnItemClickListener from this package
    //do not forget to create variable clickListener in this class
    public void setOnItemClickListener(OnItemClickListener listener){
        this.clickListener = listener;
    }

    public Sunblide getSunblideAtPosition(int position) {
        return sunblideList.get(position);
    }
}
