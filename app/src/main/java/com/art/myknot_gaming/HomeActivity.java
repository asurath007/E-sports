package com.art.myknot_gaming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.art.myknot_gaming.DashBoard.DashBoard;
import com.art.myknot_gaming.Notifications.Demo;
import com.art.myknot_gaming.Notifications.Notifications;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.onesignal.OneSignal;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements PaymentResultListener {

    private BottomNavigationView nav_bar;
    private Button btn_discord,btn_whatsapp,btn_instagram,btn_website,btn_donate;
    private ImageView iv1,iv2;
    private Context mContext;
    private Vibrator vibrator;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        nav_bar = findViewById(R.id.home_nav_bar);
        btn_discord = findViewById(R.id.btn_home_discord);
        btn_whatsapp = findViewById(R.id.btn_home_whatsapp);
        btn_website = findViewById(R.id.btn_home_website);
        btn_donate = findViewById(R.id.btn_home_donate);
        btn_instagram = findViewById(R.id.btn_home_instagram);
        iv1 = findViewById(R.id.iv_pubg);
        iv2= findViewById(R.id.iv_cod);

        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_home:
//                        Toast.makeText(HomeActivity.this,"Home Page", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_dashboard:
                        startActivity(new Intent(HomeActivity.this, DashBoard.class));finish();
                        break;
                    case R.id.navigation_notifications:
                        startActivity(new Intent(HomeActivity.this, Notifications.class));finish();
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(HomeActivity.this, Profile.class));finish();
                        break;
                }
                return false;
            }
        });

        btn_discord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrations(0);
                Intent intentDiscord = new Intent(Intent.ACTION_VIEW);
                String url = "https://discord.gg/5VFDyHZ";
                intentDiscord.setData(Uri.parse(url));
                intentDiscord.setPackage("com.discord");
                if (isIntentAvailable(mContext, intentDiscord)){
                    startActivity(intentDiscord);
                } else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/rkfmJD")));
                }
            }
        });

        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrations(0);
                Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                String url = "https://chat.whatsapp.com/Ga8LZnS6yHAJ0cO7KlZML0";
                intentWhatsapp.setData(Uri.parse(url));
                intentWhatsapp.setPackage("com.whatsapp");
                if (isIntentAvailable(mContext, intentWhatsapp)){
                    startActivity(intentWhatsapp);
                } else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.whatsapp.com/Ga8LZnS6yHAJ0cO7KlZML0")));
                }
            }
        });

        btn_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrations(0);
                Intent insta = new Intent(Intent.ACTION_VIEW);
                String url = "https://instagram.com/myknot_gaming?igshid=wuf0ppebeze1";
                insta.setData(Uri.parse(url));
                insta.setPackage("com.instagram.android");

                if (isIntentAvailable(mContext, insta)){
                    startActivity(insta);
                } else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/myknot_gaming?igshid=wuf0ppebeze1")));
                }
            }
        });

        btn_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrations(0);
                Intent web = new Intent(Intent.ACTION_VIEW);
                String url = "https://myknot-pubggaming.online/";
                web.setData(Uri.parse(url));
                web.setPackage("com.android.chrome");

                if (isIntentAvailable(mContext, web)){
                    startActivity(web);
                } else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://myknot-pubggaming.online/")));
                }
            }
        });
        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrations(0);
                startPayment();
            }
        });

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrations(0);
                Intent intent = new Intent(HomeActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrations(0);
                Toast.makeText(HomeActivity.this,"No COD matches found", Toast.LENGTH_LONG).show();
            }
        });
    }

    //check if app installed
    //if app not installed open in browser
    private boolean isIntentAvailable(Context context, Intent intent) {
        packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    private void startPayment() {
        final Activity activity = HomeActivity.this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "MyKnot Gaming");
            options.put("description", "Donation Fee");
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");

//            String payment = editTextPayment.getText().toString();
            String payment = "50";
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
//            preFill.put("email", "axe.nexas@gmail.com");
//            preFill.put("contact", "9853837232");
//            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(HomeActivity.this, "Payment successfully done! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(HomeActivity.this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
    public void vibrations(int level){
        switch (level){
            case 0:
                assert vibrator != null;
                vibrator.vibrate(15);
                break;
            case 1:
                assert vibrator != null;
                vibrator.vibrate(50);
                break;
        }
    }
}
