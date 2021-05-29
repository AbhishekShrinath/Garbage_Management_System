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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriverControl_Activity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ListView listView;
    private List<Update_Bin_Object> updateBinObjectList;
    private ProgressBar bar;

    private TextView text;
    private LottieAnimationView lottieAnimationView;

    //For Closeing App
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverControl_Activity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);

        builder.setMessage("Do you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent=new Intent(DriverControl_Activity.this,HomePage.class);
                        startActivity(intent);

                        SharedPreferences preferences =getSharedPreferences("driverKey",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide the default actionbar

        if(!isConnected(DriverControl_Activity.this))
        {
            ShowCustomDialog();
        }
        setContentView(R.layout.driver_control_activity);
        listView=findViewById(R.id.driver_control_list);
        bar=findViewById(R.id.prograss_bar8);

        text=findViewById(R.id.txt70);
        lottieAnimationView=findViewById(R.id.nodataerror70);

        Sprite cubeGrid = new CubeGrid();
        bar.setIndeterminateDrawable(cubeGrid);

        SharedPreferences sharedPreferences = getSharedPreferences("driverKey", MODE_PRIVATE);
        String Driver_email = sharedPreferences.getString("Driver_email","D_email");

        updateBinObjectList=new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance("https://garbage-management-syste-4d66b-default-rtdb.firebaseio.com/")
                .getReference("Create Bin");
        databaseReference.orderByChild("driver").equalTo(Driver_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateBinObjectList.clear();
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Update_Bin_Object updateBinObject = dataSnapshot.getValue(Update_Bin_Object.class);
                        updateBinObjectList.add(updateBinObject);
                    }
                    bar.setVisibility(View.GONE);
                    Driver_list_Adapter adapter = new Driver_list_Adapter(DriverControl_Activity.this, updateBinObjectList);
                    listView.setAdapter(adapter);
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

    public void driverprofilebtn(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DriverControl_Activity.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.driver_details_update_dialogbox, viewGroup, false);
        Button save=dialogView.findViewById(R.id.driver_details_savebtn);
        Button cancel=dialogView.findViewById(R.id.driver_details_cancelbtn);
        TextView Email=dialogView.findViewById(R.id.txt3_email);
        EditText getname=dialogView.findViewById(R.id.edit_driver_name);
        EditText getnumber=dialogView.findViewById(R.id.edit_driver_number);
        EditText getaadhar=dialogView.findViewById(R.id.edit_driver_Aadhar);
        EditText getpassword=dialogView.findViewById(R.id.edit_driver_password);

        SharedPreferences sharedPreferences = getSharedPreferences("driverKey", MODE_PRIVATE);
        String Driver_email = sharedPreferences.getString("Driver_email","D_email");
        Email.setText(Driver_email);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        databaseReference= FirebaseDatabase.getInstance().getReference("Drivers_Detail");
        databaseReference.orderByChild("email").equalTo(Email.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot datas: snapshot.getChildren())
                {
                    String driver_name = datas.child("name").getValue(String.class);
                    String driver_number = datas.child("number").getValue(String.class);
                    String driver_aadhar = datas.child("aadhar").getValue(String.class);
                    String driver_password = datas.child("password").getValue(String.class);

                    getname.setText(driver_name);
                    getnumber.setText(driver_number);
                    getaadhar.setText(driver_aadhar);
                    getpassword.setText(driver_password);
                }
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverControl_Activity.this, "Fails to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference= FirebaseDatabase.getInstance().getReference("Drivers_Detail");
                databaseReference.orderByChild("email").equalTo(Email.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("name", getname.getText().toString());
                        result.put("number", getnumber.getText().toString());
                        result.put("aadhar", getaadhar.getText().toString());
                        result.put("password", getpassword.getText().toString());
                        databaseReference.child(key).updateChildren(result);
                        Toast.makeText(DriverControl_Activity.this, "profile Updated", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(DriverControl_Activity.this);
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

    private boolean isConnected(DriverControl_Activity driverControl_activity)
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