package com.abhishekshrinath.garbagemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.HashMap;

public class AdminControl extends AppCompatActivity {

    private DrawerLayout dl;
    ActionBarDrawerToggle abdt;
    private Button createbin,updatebin,createdriver,viewdriver,complaint,workreport,userdetails;

    //For Closeing App
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminControl.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent=new Intent(AdminControl.this,HomePage.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control);

        createbin=findViewById(R.id.create_bin);
        updatebin=findViewById(R.id.update_bin);
        createdriver=findViewById(R.id.Create_driver);
        viewdriver=findViewById(R.id.View_driver);
        complaint=findViewById(R.id.View_complaints);
        workreport=findViewById(R.id.View_Work_report);
        userdetails=findViewById(R.id.User_Details);

        createbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminControl.this,CreateBin_Activity.class);
                startActivity(intent);

            }
        });
        updatebin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminControl.this,Update_Bin.class);
                startActivity(intent);
            }
        });
        createdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminControl.this,CreateDriver.class);
                startActivity(intent);
            }
        });
        viewdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminControl.this,ViewDriver.class);
                startActivity(intent);
            }
        });
        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminControl.this,Admin_view_Complaint.class);
                startActivity(intent);
            }
        });
        workreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminControl.this,WorkReport_Activity.class);
                startActivity(intent);
            }
        });
        userdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminControl.this,ViewUserDetails.class);
                startActivity(intent);

            }
        });

        dl=findViewById(R.id.draw);
        abdt=new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();
                if(id==R.id.about)
                {
                    Snackbar.make(navigationView,"About", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AdminControl.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(navigationView.getContext()).inflate(R.layout.about, viewGroup, false);
                    ShimmerTextView textView=dialogView.findViewById(R.id.shimmer_title);
                    Shimmer shimmer=new Shimmer();
                    shimmer.start(textView);
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(id==R.id.logout)
                {
                    Intent intent=new Intent(AdminControl.this,HomePage.class);
                    startActivity(intent);
                    Snackbar.make(navigationView,"About", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                return true;
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}