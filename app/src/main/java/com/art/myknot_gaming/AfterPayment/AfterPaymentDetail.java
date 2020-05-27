package com.art.myknot_gaming.AfterPayment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.art.myknot_gaming.R;

import java.util.Arrays;

public class AfterPaymentDetail extends AppCompatActivity {
    private TextView name,map,mode,date,time,type,id,pass;
    private android.widget.TextView prizeMoney,moneyBreakUp,entryFee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_payment_details);
        name = findViewById(R.id.tv_afd_solo_match_title);
        map =findViewById(R.id.tv_afd_solo_map);
        mode= findViewById(R.id.tv_afd_solo_mode);
        date= findViewById(R.id.tv_afd_solo_date);
        time= findViewById(R.id.tv_afd_solo_time);
        prizeMoney= findViewById(R.id.tv_afd_solo_prizeMoney);
        moneyBreakUp = findViewById(R.id.tv_afd_solo_moneyBreakUp);
        entryFee=findViewById(R.id.tv_afd_solo_entryFee);
        id = findViewById(R.id.tv_solo_afd_room_id);
        pass = findViewById(R.id.tv_solo__afd_password);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name.setText(bundle.getString("name"));
            map.setText(bundle.getString("map"));
            mode.setText(bundle.getString("mode"));
            date.setText(bundle.getString("date"));
            time.setText(bundle.getString("time"));
            entryFee.setText(bundle.getString("ef"));
            prizeMoney.setText(bundle.getString("pm"));
            moneyBreakUp.setText(bundle.getString("mbu"));
        }
    }


}
