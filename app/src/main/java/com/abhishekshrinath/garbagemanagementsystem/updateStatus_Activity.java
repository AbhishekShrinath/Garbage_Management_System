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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class updateStatus_Activity extends AppCompatActivity {

    private TextView email,complaint;
    private Spinner spinner;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_status_activity);

        email=findViewById(R.id.up_email);
        complaint=findViewById(R.id.up_complaint);
        spinner=findViewById(R.id.Update_Staus_spinner);

        String array[]={"pending","Completed","Rejected","on Process"};
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(updateStatus_Activity.this, android.R.layout.simple_spinner_item, array);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(Adapter);

        String get_email=getIntent().getExtras().getString("email");
        String get_complaint=getIntent().getExtras().getString("complaint");

        email.setText(get_email);
        complaint.setText(get_complaint);


//        databaseReference=FirebaseDatabase.getInstance().getReference("Drivers_Detail");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                final List<String> list=new ArrayList<>();
//                for(DataSnapshot emailsnapshot: snapshot.getChildren())
//                {
//                    String Email_sp = emailsnapshot.child("email").getValue(String.class);
//                    list.add(Email_sp);
//                }
//                ArrayAdapter<String> Adapter = new ArrayAdapter<String>(updateStatus_Activity.this, android.R.layout.simple_spinner_item, list);
//                Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(Adapter);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void savebtn(View view) {
        String Email=email.getText().toString();
        String value=spinner.getSelectedItem().toString();

        databaseReference= FirebaseDatabase.getInstance("https://garbage-management-syste-4d66b-default-rtdb.firebaseio.com/")
                .getReference("UserComplaint");
        databaseReference.orderByChild("email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!isConnected(updateStatus_Activity.this))
                {
                    ShowCustomDialog();
                }
               else {
                    DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                    String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("status", value);
                    databaseReference.child(key).updateChildren(result);
                    Toast.makeText(updateStatus_Activity.this, "Status Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(updateStatus_Activity.this, AdminControl.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(updateStatus_Activity.this);
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

    private boolean isConnected(updateStatus_Activity updateStatus_activity)
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