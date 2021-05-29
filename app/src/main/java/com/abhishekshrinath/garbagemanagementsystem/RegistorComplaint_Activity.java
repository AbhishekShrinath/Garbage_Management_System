package com.abhishekshrinath.garbagemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegistorComplaint_Activity extends AppCompatActivity {
    private ListView listView;
    private List<Update_Bin_Object> updateBinObjectList;
    DatabaseReference databaseReference;
    private ProgressBar bar;
    private TextView textView;
    private LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_registor_complaint_);
        getSupportActionBar().hide(); //hide the default actionbar

        listView=findViewById(R.id.registorComplaint_list);
        bar=findViewById(R.id.prograss_bar_regt);
        textView=findViewById(R.id.txt10);
        lottieAnimationView=findViewById(R.id.nodataerror10);

        updateBinObjectList=new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance("https://garbage-management-syste-4d66b-default-rtdb.firebaseio.com/")
                .getReference("Create Bin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateBinObjectList.clear();
                if(!isConnected(RegistorComplaint_Activity.this))
                {
                    bar.setVisibility(View.GONE);
                    ShowCustomDialog();
                }
                else if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Update_Bin_Object updateBinObject = dataSnapshot.getValue(Update_Bin_Object.class);
                        updateBinObjectList.add(updateBinObject);
                    }
                    RegistorAdapter adapter = new RegistorAdapter(RegistorComplaint_Activity.this, updateBinObjectList);
                    listView.setAdapter(adapter);
                    bar.setVisibility(View.GONE);
                }
                else {
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(RegistorComplaint_Activity.this);
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

    private boolean isConnected(RegistorComplaint_Activity registorComplaint_activity)
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