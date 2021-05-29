package com.abhishekshrinath.garbagemanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserControl extends AppCompatActivity {

    private Button registorcomplaintbtn,profilebtn,mycomplaintbtn;

    //For Closeing App
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserControl.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent=new Intent(UserControl.this,HomePage.class);
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user_control);
        getSupportActionBar().hide(); //hide the default actionbar
        registorcomplaintbtn=findViewById(R.id.registor_complaint_btn);
        profilebtn=findViewById(R.id.user_Profile_btn);
        mycomplaintbtn=findViewById(R.id.user_complaint_btn);

        registorcomplaintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserControl.this,RegistorComplaint_Activity.class);
                startActivity(intent);
                Toast.makeText(UserControl.this, "Registor Complaint", Toast.LENGTH_SHORT).show();
            }
        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserControl.this,Myprofile.class);
                startActivity(intent);
                Toast.makeText(UserControl.this, "Profile", Toast.LENGTH_SHORT).show();

            }
        });
        mycomplaintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserControl.this,User_Complaint_Activity.class);
                startActivity(intent);
                Toast.makeText(UserControl.this, "My Complaint", Toast.LENGTH_SHORT).show();

            }
        });
    }
}