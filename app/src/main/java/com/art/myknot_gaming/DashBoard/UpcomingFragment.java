package com.art.myknot_gaming.DashBoard;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.myknot_gaming.MapList.MapAdapter;
import com.art.myknot_gaming.R;
import com.art.myknot_gaming.Util.MapList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class UpcomingFragment extends Fragment {
    private static final String TAG = "Upcoming Match";
    private RecyclerView rv;
    private RecyclerView.Adapter rv_adapter;
    private FirebaseFirestore firestoreDB= FirebaseFirestore.getInstance();
    private ListenerRegistration firestoreListener;
    private CollectionReference matchRef = firestoreDB.collection("Match List");
    private SharedPreferences sp2,sp1;
    String id="",sID="",lID="",playerID="";
    private TextView tv;
    private LinearLayout ll;
    @SuppressLint("SimpleDateFormat")
    DateFormat df = new SimpleDateFormat("d-M-yyyy");
    String currentDate = df.format(Calendar.getInstance().getTime());
    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        rv = view.findViewById(R.id.f1_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        ll = view.findViewById(R.id.f1_ll);
        tv = view.findViewById(R.id.f1_tv1);
        sp1 = Objects.requireNonNull(getContext()).getSharedPreferences("signupKey", MODE_PRIVATE);
        sID = sp1.getString("value", "");
        sp2 = getContext().getSharedPreferences("loginKey",MODE_PRIVATE);
        lID = sp2.getString("value","");
        playerID = sID+"-"+lID;
        Log.d("AF","id:"+playerID);
        List<MapList> mapLists = new ArrayList<>();
        Log.d(TAG,"date:"+currentDate);
        loadMatchList();
        firestoreListener = matchRef.whereArrayContains("players", playerID).whereGreaterThan("date",currentDate)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }
                        List<MapList> getMatchList = new ArrayList<>();

                        assert queryDocumentSnapshots != null;
                        for (DocumentSnapshot doc : queryDocumentSnapshots){
                            MapList match = doc.toObject(MapList.class);
                            assert match != null;
                            match.setId(doc.getId());
                            getMatchList.add(match);
                        }

                        rv_adapter = new MapAdapter(getMatchList, getContext(), firestoreDB);
                        rv.setAdapter(rv_adapter);
                        rv_adapter.notifyDataSetChanged();
                        if (rv_adapter.getItemCount()==0){
                            rv.setVisibility(View.GONE);
                            ll.setVisibility(View.VISIBLE);
                        }else{
                            rv.setVisibility(View.VISIBLE);
                            ll.setVisibility(View.GONE);
                        }
                    }
                });
        return view;
    }
    private void loadMatchList() {
        matchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    List<MapList> getMatchList = new ArrayList<>();

                    for (DocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                        MapList match = doc.toObject(MapList.class);
                        assert match != null;
                        match.setId(doc.getId());
                        getMatchList.add(match);
                    }
                } else {
                    Toast.makeText(getContext(), "Error getting Match List", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}