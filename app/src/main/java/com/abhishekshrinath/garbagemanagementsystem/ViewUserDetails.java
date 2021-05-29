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

public class ViewUserDetails extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ListView listView;
    private ShimmerTextView textView;
    private Shimmer shimmer;
    private List<UserDetails> userDetailsList;
    private ProgressBar bar;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_view_user_details);
        getSupportActionBar().hide(); //hide the default actionbar

        textView=findViewById(R.id.txt2);
        lottieAnimationView=findViewById(R.id.nodataerror2);
        shimmer=new Shimmer();
        shimmer.start(textView);
        listView=findViewById(R.id.User_list_view);
        bar=findViewById(R.id.prograss_bar1);
        userDetailsList=new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance().getReference("UserDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetailsList.clear();
                if(!isConnected(ViewUserDetails.this))
                {
                    bar.setVisibility(View.GONE);
                    ShowCustomDialog();
                }
                else if(snapshot.exists()) {
                    for (DataSnapshot User_details : snapshot.getChildren()) {
                        UserDetails userDetails = User_details.getValue(UserDetails.class);
                        userDetailsList.add(userDetails);

                    }
                }
            else {
                lottieAnimationView.setVisibility(View.VISIBLE);
            }
                UserListAdapter adapter=new UserListAdapter(ViewUserDetails.this,userDetailsList);
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
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(ViewUserDetails.this);
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

    private boolean isConnected(ViewUserDetails viewUserDetails)
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