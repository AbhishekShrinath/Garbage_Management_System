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

public class Admin_view_Complaint extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ListView listView;
    private List<usercomplaint> usercomplaintList;
    private ProgressBar bar;
    TextView textView;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin_view_complaint);
        getSupportActionBar().hide(); //hide the default actionbar

        listView=findViewById(R.id.Admin_complaint_listView);
        bar=findViewById(R.id.prograss_bar6);
        textView=findViewById(R.id.txtcomplaint);
        lottieAnimationView=findViewById(R.id.nodataerror1);
        usercomplaintList=new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance().getReference("UserComplaint");
        databaseReference.orderByChild("status").equalTo("pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercomplaintList.clear();
                if(!isConnected(Admin_view_Complaint.this))
                {
                    ShowCustomDialog();
                }
                else if(snapshot.exists()) {
                    for (DataSnapshot viewcomplaint : snapshot.getChildren()) {
                        usercomplaint usercomplaint = viewcomplaint.getValue(usercomplaint.class);
                        usercomplaintList.add(usercomplaint);
                    }
                }
                else {
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                AdminView_Complaint_Adapter adapter=
                        new AdminView_Complaint_Adapter(Admin_view_Complaint.this,usercomplaintList);
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
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(Admin_view_Complaint.this);
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

    private boolean isConnected(Admin_view_Complaint admin_view_complaint)
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