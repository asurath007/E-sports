package com.art.myknot_gaming.MapList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.art.myknot_gaming.R;
import com.art.myknot_gaming.Registration.Duo;
import com.art.myknot_gaming.Registration.Solo;
import com.art.myknot_gaming.Registration.Squad;
import com.art.myknot_gaming.Util.MapList;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {
    private Context context;
    private List<MapList> mapList;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private String mTag,docID="";

    public MapAdapter(Context context, List mapList){
        this.context = context;
        this.mapList = mapList;
//        this.matchDB = matchDB;

    }
    public MapAdapter(List<MapList> getMatchList, Context applicationContext, FirebaseFirestore firestoreDB) {
        this.mapList = getMatchList;
        this.context = applicationContext;
        this.firestoreDB = firestoreDB;
    }

    public MapAdapter(List<MapList> getMatchList, Context applicationContext, FirebaseFirestore firestoreDB, String Tag) {
        this.mapList = getMatchList;
        this.context = applicationContext;
        this.firestoreDB = firestoreDB;
        this.mTag = Tag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.maps_list_row, parent, false);
        return new ViewHolder(view, mTag); //view from the ViewHolder @end is passed
    }

    @Override
    public void onBindViewHolder(@NonNull MapAdapter.ViewHolder holder, int position) {
        MapList map = mapList.get(position);
//        docID = mapList.get(position).getId();
        holder.name.setText(map.getName()); holder.map.setText(map.getMap());
        holder.mode.setText(map.getMode()); holder.date.setText(map.getDate());
        holder.time.setText(map.getTime()); holder.prizeMoney.setText(map.getPrizeMoney());
        holder.moneyBreakUp.setText(map.getMoneyBreakUp()); holder.entryFee.setText(map.getEntryFee());
        holder.match_status.setProgress(map.getProgress());
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


        public ViewHolder(@NonNull View itemView, String tag) {
            super(itemView);
            mTag = tag;
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
            docID = mapList.get(position).getId();
            SharedPreferences sharedPref = context.getSharedPreferences("matchID", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("value", docID);
            editor.apply();
            //get to registration page
            switch (mTag){
                case "SOLO":
                    Intent intentSolo = new Intent(context, Solo.class);
                    intentSolo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentSolo.putExtra("name",list.getName());
                    intentSolo.putExtra("ID",docID);
                    intentSolo.putExtra("map",list.getMap());
                    intentSolo.putExtra("date",list.getDate());
                    intentSolo.putExtra("time",list.getTime());
                    intentSolo.putExtra("pm",list.getPrizeMoney());
                    intentSolo.putExtra("ef",list.getEntryFee());
                    intentSolo.putExtra("mbu",list.getMoneyBreakUp());
                    intentSolo.putExtra("mode",list.getMode());
                    context.startActivity(intentSolo);
                    break;
                case "DUO":
                    Intent intentDuo = new Intent(context, Duo.class);
                    intentDuo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentDuo.putExtra("mode",list.getMode());
                    intentDuo.putExtra("name",list.getName());
                    intentDuo.putExtra("map",list.getMap());
                    intentDuo.putExtra("date",list.getDate());
                    intentDuo.putExtra("time",list.getTime());
                    intentDuo.putExtra("pm",list.getPrizeMoney());
                    intentDuo.putExtra("ef",list.getEntryFee());
                    intentDuo.putExtra("mbu",list.getMoneyBreakUp());
                    context.startActivity(intentDuo);
                    break;
                case "SQUAD":
                    Intent intentSquad = new Intent(context, Squad.class);
                    intentSquad.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentSquad.putExtra("mode",list.getMode());
                    intentSquad.putExtra("name",list.getName());
                    intentSquad.putExtra("map",list.getMap());
                    intentSquad.putExtra("date",list.getDate());
                    intentSquad.putExtra("time",list.getTime());
                    intentSquad.putExtra("pm",list.getPrizeMoney());
                    intentSquad.putExtra("ef",list.getEntryFee());
                    intentSquad.putExtra("mbu",list.getMoneyBreakUp());
                    context.startActivity(intentSquad);
                    break;
            }

        }
    }

}
