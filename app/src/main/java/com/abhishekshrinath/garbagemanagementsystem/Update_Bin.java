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
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Update_Bin extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ListView listView;
    private List<Update_Bin_Object> updateBinObjectList;
    private TextView textView;
    LottieAnimationView lottieAnimationView;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Update_Bin.this,AdminControl.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_update_bin);
        getSupportActionBar().hide(); //hide the default actionbar

        listView=findViewById(R.id.update_listview);
        textView=findViewById(R.id.txt7);
        lottieAnimationView=findViewById(R.id.nodataerror);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.pb);
        Sprite foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);

        updateBinObjectList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance("https://garbage-management-syste-4d66b-default-rtdb.firebaseio.com/")
                .getReference("Create Bin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateBinObjectList.clear();
              if(!isConnected(Update_Bin.this))
                {
                    progressBar.setVisibility(View.GONE);
                    ShowCustomDialog();
                }
              else if(snapshot.exists()) {
                   for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                       Update_Bin_Object updateBinObject = dataSnapshot.getValue(Update_Bin_Object.class);
                       updateBinObjectList.add(updateBinObject);
                   }
               }
               else {
                   lottieAnimationView.setVisibility(View.VISIBLE);
                   textView.setVisibility(View.VISIBLE);
               }
                progressBar.setVisibility(View.GONE);
                UpdateBinAdapter adapter=new UpdateBinAdapter(Update_Bin.this,updateBinObjectList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(Update_Bin.this);
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

    private boolean isConnected(Update_Bin update_bin)
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