package com.abhishekshrinath.garbagemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Myprofile extends AppCompatActivity {

    private TextView name,email,number;
    private DatabaseReference databaseReference;
    private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_myprofile);
        getSupportActionBar().hide(); //hide the default actionbar

        name=findViewById(R.id.User_Name);
        email=findViewById(R.id.User_Email);
        number=findViewById(R.id.User_Number);
        bar=findViewById(R.id.bar5);
        if(!isConnected(Myprofile.this))
        {
            ShowCustomDialog();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String value = sharedPreferences.getString("value","");

        databaseReference= FirebaseDatabase.getInstance().getReference("UserDetails");
        databaseReference.orderByChild("u_email").equalTo(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot datas: snapshot.getChildren())
                {
                    String user_name = datas.child("u_name").getValue(String.class);
                    String user_email = datas.child("u_email").getValue(String.class);
                    String user_number = datas.child("u_number").getValue(String.class);

                    name.setText(user_name);
                    email.setText(user_email);
                    number.setText(user_number);
                }
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Myprofile.this, "Fails to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateProfilebtn(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Myprofile.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.updatedetails_dialogbox, viewGroup, false);
        Button save=dialogView.findViewById(R.id.savebtn);
        TextView Email=dialogView.findViewById(R.id.txt2_email);
        EditText getname=dialogView.findViewById(R.id.edit_name);
        EditText getnumber=dialogView.findViewById(R.id.edit_number);
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String value = sharedPreferences.getString("value","");
        Email.setText(value);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference= FirebaseDatabase.getInstance().getReference("UserDetails");
                databaseReference.orderByChild("u_email").equalTo(Email.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("u_name", getname.getText().toString());
                        result.put("u_number", getnumber.getText().toString());
                        databaseReference.child(key).updateChildren(result);
                        Toast.makeText(Myprofile.this, "profile Updated", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }
    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(Myprofile.this);
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

    private boolean isConnected(Myprofile myprofile)
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