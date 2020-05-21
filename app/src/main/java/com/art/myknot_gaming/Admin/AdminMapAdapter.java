package com.art.myknot_gaming.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.art.myknot_gaming.Util.MapList;
import com.art.myknot_gaming.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdminMapAdapter extends RecyclerView.Adapter<AdminMapAdapter.ViewHolder> {

    private Context context;
    private List<MapList> mapList;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference matchRef = firestoreDB.collection("Match List");
    private DocumentReference matchDetail = firestoreDB.document("Match List/Match id");
    String id = "";

    public AdminMapAdapter(Context context, List mapList){
        this.context = context;
        this.mapList = mapList;
    }

    public AdminMapAdapter(List<MapList> getMatchList, Context applicationContext, FirebaseFirestore firestoreDB) {
        this.mapList = getMatchList;
        this.context = applicationContext;
        this.firestoreDB = firestoreDB;
    }

    @NonNull
    @Override
    public AdminMapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_add_match_list_row, parent, false);
        return new AdminMapAdapter.ViewHolder(view, context); //view from the ViewHolder @end is passed
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMapAdapter.ViewHolder holder, int position) {
        MapList map = mapList.get(position);

        holder.name.setText(map.getName()); holder.map.setText(map.getMap());
        holder.mode.setText(map.getMode()); holder.date.setText(map.getDate());
        holder.time.setText(map.getTime()); holder.prizeMoney.setText(map.getPrizeMoney());
        holder.moneyBreakUp.setText(map.getMoneyBreakUp()); holder.entryFee.setText(map.getEntryFee());
        holder.match_status.setProgress(map.getProgress()); holder.type.setText(map.getType());
}

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView map,type;
        public TextView mode;
        public TextView date;
        public TextView time;
        public TextView prizeMoney;
        public TextView moneyBreakUp;
        public TextView entryFee;
        public ProgressBar match_status;
        public Button btn_edit;
        public Button btn_delete;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapList editMatch = mapList.get(getAdapterPosition());
                    Intent intent = new Intent(context, AdminAddMatchDetail.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("UId", editMatch.getId());
                    intent.putExtra("UName", editMatch.getName());
                    intent.putExtra("UMap", editMatch.getMap());
                    intent.putExtra("UMode", editMatch.getMode());
                    intent.putExtra("UDate", editMatch.getDate());
                    intent.putExtra("UTime", editMatch.getTime());
                    intent.putExtra("UPrizeMoney", editMatch.getPrizeMoney());
                    intent.putExtra("UEntryFee", editMatch.getEntryFee());
                    intent.putExtra("UType",editMatch.getType());
                    intent.putExtra("UMoneyBreakUp",editMatch.getMoneyBreakUp());

                    context.startActivity(intent);
                }
            });

            name = itemView.findViewById(R.id.tv_admin_match_title);
            map = itemView.findViewById(R.id.tv_admin_edit_map);
            date = itemView.findViewById(R.id.tv_admin_edit_date);
            time = itemView.findViewById(R.id.tv_admin_edit_time);
            mode = itemView.findViewById(R.id.tv_admin_edit_mode);
            type = itemView.findViewById(R.id.tv_admin_type);
            prizeMoney = itemView.findViewById(R.id.tv_admin_edit_prizeMoney);
            moneyBreakUp = itemView.findViewById(R.id.tv_admin_edit_moneyBreakUp);
            entryFee = itemView.findViewById(R.id.tv_admin_edit_entryFee);
            match_status = itemView.findViewById(R.id.admin_statusBar);


        }
    }
}
