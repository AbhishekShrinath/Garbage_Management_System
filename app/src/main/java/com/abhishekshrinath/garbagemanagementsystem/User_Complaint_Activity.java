package com.abhishekshrinath.garbagemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.MultiplePulse;
import com.github.ybq.android.spinkit.style.MultiplePulseRing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.List;

public class User_Complaint_Activity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ListView listView;
    private List<usercomplaint> usercomplaintList;
    private ProgressBar bar;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user_complaint);
        getSupportActionBar().hide(); //hide the default actionbar

        listView=findViewById(R.id.complaint_listView);
        bar=findViewById(R.id.prograss_bar4);
        textView=findViewById(R.id.txt4);

        Sprite multiplePulseRing = new MultiplePulseRing();
        bar.setIndeterminateDrawable(multiplePulseRing);
        usercomplaintList=new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String value = sharedPreferences.getString("value","");

        databaseReference= FirebaseDatabase.getInstance().getReference("UserComplaint");
        databaseReference.orderByChild("email").equalTo(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercomplaintList.clear();
                if(!isConnected(User_Complaint_Activity.this))
                {
                    bar.setVisibility(View.GONE);
                    ShowCustomDialog();
                }
                else if(snapshot.exists())
                {
                    for (DataSnapshot viewcomplaint : snapshot.getChildren())
                    {
                        usercomplaint usercomplaint = viewcomplaint.getValue(usercomplaint.class);
                        usercomplaintList.add(usercomplaint);
                    }
                }
                else
                {
                    textView.setVisibility(View.VISIBLE);
                }
                bar.setVisibility(View.GONE);
                only_userview_Complaint_Adapter adapter=
                        new only_userview_Complaint_Adapter(User_Complaint_Activity.this,usercomplaintList);
                listView.setAdapter(adapter);
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(User_Complaint_Activity.this);
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

    private boolean isConnected(User_Complaint_Activity user_complaint_activity)
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