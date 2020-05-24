package com.art.myknot_gaming.Notifications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.art.myknot_gaming.DashBoard.DashBoard;
import com.art.myknot_gaming.HomeActivity;
import com.art.myknot_gaming.Profile;
import com.art.myknot_gaming.R;
import com.art.myknot_gaming.Util.NotificationList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {
    private static final String TAG = "NotActivity";
    private String title,body;
    private TextView not_display;
    private BottomNavigationView nav_bar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<NotificationList> not_list;
    private ListenerRegistration firestoreListener;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference notificationRef = firestoreDB.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        nav_bar = findViewById(R.id.not_nav_bar);
        nav_bar.setSelectedItemId(R.id.navigation_notifications);
        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_home:
                        startActivity(new Intent(Notifications.this, HomeActivity.class));
                    case R.id.navigation_dashboard:
                        startActivity(new Intent(Notifications.this, DashBoard.class));
                        break;
                    case R.id.navigation_notifications:
//                        Toast.makeText(Notifications.this,"Notifications Page", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(Notifications.this, Profile.class));
                        break;
                }
                return false;
            }
        });
        not_display = findViewById(R.id.not_display);
        recyclerView = findViewById(R.id.not_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        firestoreListener = notificationRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    Log.e(TAG, "Listen failed!", e);
//                    return;
//                }
//                List<NotificationList> getNotList = new ArrayList<>();
//
//                assert  queryDocumentSnapshots!=null;
//                for (DocumentSnapshot doc:queryDocumentSnapshots){
//                    NotificationList notificationList = doc.toObject(NotificationList.class);
//                    assert  notificationList !=null;
//                    getNotList.add(notificationList);
//                }
//            }
//        });

        SharedPreferences sp = getSharedPreferences("pushTitle",MODE_PRIVATE);
        SharedPreferences sp1 = getSharedPreferences("pushBody",MODE_PRIVATE);
        title =sp.getString("title","");
        body = sp1.getString("body","");
        Log.d(TAG, "MSG:"+title+ "-+-" + body);

        if ((title !=null)&&(body!=null)){
            recyclerView.setVisibility(View.VISIBLE);
            not_display.setVisibility(View.GONE);
        }


        not_list = new ArrayList<>();
//        Bundle bundle = getIntent().getExtras();
//        if (bundle!=null){
//             title =bundle.getString("push_title");
//             body = bundle.getString("push_body");
//             recyclerView.setVisibility(View.VISIBLE);
//             not_display.setVisibility(View.GONE);
//        }
        NotificationList item1 = new NotificationList(title,body);

        not_list.add(item1);

        adapter = new NotificationAdapter(this, not_list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
//    protected void onDestroy() {
//        super.onDestroy();
//
//        firestoreListener.remove();
//    }

}
