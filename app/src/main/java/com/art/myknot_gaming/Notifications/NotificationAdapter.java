package com.art.myknot_gaming.Notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.art.myknot_gaming.R;
import com.art.myknot_gaming.Util.NotificationList;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<NotificationList> not_list;

    public NotificationAdapter(Context context, List not_list){
        this.context = context;
        this.not_list = not_list;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_notifications,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationList item = not_list.get(position) ;
        holder.title.setText(item.getTitle());
        holder.details.setText(item.getDetails());
    }

    @Override
    public int getItemCount() {
        return not_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView title,details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title  = itemView.findViewById(R.id.not_title);
            details = itemView.findViewById(R.id.not_details);
        }

    }
}
