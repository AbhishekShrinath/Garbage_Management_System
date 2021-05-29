package com.abhishekshrinath.garbagemanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.androidadvance.topsnackbar.TSnackbar;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverLogin extends AppCompatActivity {
    private EditText driver_email,driver_password;
    private Button driverloginbtn;
    private DatabaseReference databaseReference;
    private ProgressBar bar;
    private Toolbar toolbar;

    @Override
    public void onBackPressed() {

        SharedPreferences preferences =getSharedPreferences("driverKey",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        driver_email.setText("");
        driver_password.setText("");
        DriverLogin.this.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.axtivity_driver_login);
        getSupportActionBar().hide(); //hide the default actionbar

        driver_email=findViewById(R.id.driver_email);
        driver_password=findViewById(R.id.driver_pass);
        driverloginbtn=findViewById(R.id.driver_login_btn);
        bar=findViewById(R.id.prograss_bar9);
        toolbar=findViewById(R.id.toolbar2);

        Sprite foldingCube = new FoldingCube();
        bar.setIndeterminateDrawable(foldingCube);

        driverloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                String Email=driver_email.getText().toString();
                String Pass=driver_password.getText().toString();

                if(!isConnected(DriverLogin.this))
                {
                    ShowCustomDialog();
                }
                else if(TextUtils.isEmpty(Email))
                {
                    bar.setVisibility(View.GONE);
                    Toast.makeText(DriverLogin.this, "Please Enter your Email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(Pass))
                {
                    bar.setVisibility(View.GONE);
                    Toast.makeText(DriverLogin.this, "Please Enter your password", Toast.LENGTH_SHORT).show();
                }
                else if(!isConnected(DriverLogin.this))
                {
                    bar.setVisibility(View.GONE);
                    ShowCustomDialog();
                }
                else
                {
                    databaseReference= FirebaseDatabase.getInstance("https://garbage-management-syste-4d66b-default-rtdb.firebaseio.com/").getReference("Drivers_Detail");
                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                            DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
//                            String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`

                            String emailAddress =snapshot.child("email").getValue(String.class);
                            String password=snapshot.child("password").getValue(String.class);

                            if(emailAddress.equals(Email) && password.equals(Pass))
                            {
                                bar.setVisibility(View.GONE);
                                SharedPreferences sharedPref = getSharedPreferences("driverKey", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("Driver_email", Email);
                                editor.apply();
                                Intent intent=new Intent(DriverLogin.this,DriverControl_Activity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                bar.setVisibility(View.GONE);
                                TSnackbar.make(toolbar,"Email ID does not Match to password or user Does not Exist", TSnackbar.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


    }

    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(DriverLogin.this);
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

    private boolean isConnected(DriverLogin driverLogin)
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