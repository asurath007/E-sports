package com.art.myknot_gaming.AfterPayment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.art.myknot_gaming.R;
import com.art.myknot_gaming.Util.MapList;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AfterPaymentAdapter extends RecyclerView.Adapter<AfterPaymentAdapter.ViewHolder> {
    private Context context;
    private List<MapList> mapList;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private String mTag;

    public AfterPaymentAdapter(Context context, List mapList){
        this.context = context;
        this.mapList = mapList;
//        this.matchDB = matchDB;

    }
    public AfterPaymentAdapter(List<MapList> getMatchList, Context applicationContext, FirebaseFirestore firestoreDB) {
        this.mapList = getMatchList;
        this.context = applicationContext;
        this.firestoreDB = firestoreDB;
    }

    public AfterPaymentAdapter(List<MapList> getMatchList, Context applicationContext, FirebaseFirestore firestoreDB, String Tag) {
        this.mapList = getMatchList;
        this.context = applicationContext;
        this.firestoreDB = firestoreDB;
        this.mTag = Tag;
    }

    @NonNull
    @Override
    public AfterPaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.maps_list_row, parent, false);
        return new AfterPaymentAdapter.ViewHolder(view, context); //view from the ViewHolder @end is passed
    }

    @Override
    public void onBindViewHolder(@NonNull AfterPaymentAdapter.ViewHolder holder, int position) {
        MapList map = mapList.get(position);

        holder.name.setText(map.getName()); holder.map.setText(map.getMap());
        holder.mode.setText(map.getMode()); holder.date.setText(map.getDate());
        holder.time.setText(map.getTime()); holder.prizeMoney.setText(map.getPrizeMoney());
        holder.moneyBreakUp.setText(map.getMoneyBreakUp()); holder.entryFee.setText(map.getEntryFee());
        holder.match_status.setProgress(map.getProgress());
        //holder.type.setText(map.getType());
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public TextView map;
        public TextView mode;
        public TextView date;
        public TextView time;
        public TextView prizeMoney;
        public TextView moneyBreakUp;
        public TextView entryFee;
        public ProgressBar match_status;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
//            mTag = tag;
            context = ctx;
            itemView.setOnClickListener(this);

            name = itemView.findViewById(R.id.tv_match_title);
            map = itemView.findViewById(R.id.tv_map);
            date = itemView.findViewById(R.id.tv_date);
            time = itemView.findViewById(R.id.tv_time);
            mode = itemView.findViewById(R.id.tv_mode);
            prizeMoney =itemView.findViewById(R.id.tv_prizeMoney);
            moneyBreakUp = itemView.findViewById(R.id.tv_moneyBreakUp);
            entryFee = itemView.findViewById(R.id.tv_entryFee);
            match_status = itemView.findViewById(R.id.match_status);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            MapList list = mapList.get(position);

        }
    }

}
