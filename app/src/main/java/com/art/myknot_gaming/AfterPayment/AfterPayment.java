package com.art.myknot_gaming.AfterPayment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AfterPayment extends AppCompatActivity {
    private static final String TAG = "After Payment";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mapAdapter;
    private List<MapList> mapLists;
    String id="",sID="",lID="",playerID="";
    private SharedPreferences sp1,sp2;

    private ListenerRegistration firestoreListener;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference matchRef = firestoreDB.collection("Match List");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_payment);
        recyclerView = findViewById(R.id.rv_map_af_solo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mapLists = new ArrayList<>();
        loadMatchList();

        sp1 = getSharedPreferences("signupKey", MODE_PRIVATE);
        sID = sp1.getString("value", "");
        sp2 = getSharedPreferences("loginKey",MODE_PRIVATE);
        lID = sp2.getString("value","");
        playerID = sID+"-"+lID;

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
//        id = bundle.getString("id");
        Log.d("AF","id:"+id);
        }

        firestoreListener = matchRef.whereArrayContains("players", playerID)
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
                        mapAdapter = new AfterPaymentAdapter(getMatchList, getApplicationContext(), firestoreDB);
                        recyclerView.setAdapter(mapAdapter);
                        mapAdapter.notifyDataSetChanged();
                    }
                });
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
                    Toast.makeText(AfterPayment.this, "Error getting Match List", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();

        firestoreListener.remove();
    }

}