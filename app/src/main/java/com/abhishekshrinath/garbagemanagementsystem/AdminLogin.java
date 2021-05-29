package com.abhishekshrinath.garbagemanagementsystem;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class AdminLogin extends AppCompatActivity {

    private EditText admin_email,admin_password;
    private Button adminloginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin_login);
        getSupportActionBar().hide(); //hide the default actionbar

        admin_email=findViewById(R.id.admin_email);
        admin_password=findViewById(R.id.admin_pass);
        adminloginbtn=findViewById(R.id.admin_login_btn);
        if(!isConnected(AdminLogin.this))
        {
            ShowCustomDialog();
        }

        adminloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email="admin";
                String password="admin";

                String a_email=admin_email.getText().toString();
                String a_pass=admin_password.getText().toString();
                if(!isConnected(AdminLogin.this))
                {
                    ShowCustomDialog();
                }
               else if(a_email.equals(email) && a_pass.equals(password))
                {
                    Intent intent=new Intent(AdminLogin.this,AdminControl.class);
                    startActivity(intent);
                }
                else
                {
                    Snackbar.make(v,"Invalid Login", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });


    }
    private void ShowCustomDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(AdminLogin.this);
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

    private boolean isConnected(AdminLogin adminLogin)
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