package com.abhishekshrinath.garbagemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.List;

public class ViewDriver extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private ListView listView;
    private ShimmerTextView textView;
    private Shimmer shimmer;
    private List<driver_class> driverClasseslist;
    private ProgressBar bar;

    private TextView text;
    private LottieAnimationView lottieAnimationView;

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(ViewDriver.this,AdminControl.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_view_driver);
        getSupportActionBar().hide(); //hide the default actionbar

        textView=findViewById(R.id.txt);
        shimmer=new Shimmer();
        shimmer.start(textView);
        listView=findViewById(R.id.list_view);
        bar=findViewById(R.id.prograss_bar);

        text=findViewById(R.id.txt13);
        lottieAnimationView=findViewById(R.id.nodataerror13);

        driverClasseslist=new ArrayList<>();

        databaseReference=FirebaseDatabase.getInstance().getReference("Drivers_Detail");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                driverClasseslist.clear();
                if(!isConnected(ViewDriver.this))
                {
                    bar.setVisibility(View.GONE);
                    ShowCustomDialog();
                }
                else if(snapshot.exists()){
                    for (DataSnapshot ddetails : snapshot.getChildren()) {
                        driver_class driverClass = ddetails.getValue(driver_class.class);
                        driverClasseslist.add(driverClass);
                    }
                    ListAdapter adapter = new ListAdapter(ViewDriver.this, driverClasseslist);
                    listView.setAdapter(adapter);
                    bar.setVisibility(View.GONE);
                }
                else {
                    bar.setVisibility(View.GONE);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(ViewDriver.this);
        builder.setMessage("Please Connect to Internet To Proceed Further").setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }

        });
        builder.show();
    }

    private boolean isConnected(ViewDriver viewDriver)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiinfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo Mobileinfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifiinfo !=null && wifiinfo.isConnected()) || (Mobileinfo !=null && Mobileinfo.isConnected()))
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}