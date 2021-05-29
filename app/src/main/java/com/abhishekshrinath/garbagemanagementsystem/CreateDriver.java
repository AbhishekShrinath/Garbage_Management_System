package com.abhishekshrinath.garbagemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class CreateDriver extends AppCompatActivity {

    private EditText name,email,password,number,area,aadharno;
    private Button d_btn;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar bar;
    private Toolbar toolbar;

//    @Override
//    public void onBackPressed() {
//        finish();
//        databaseReference.onDisconnect();
//        name.setText("");
//        email.setText("");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_driver);
        getSupportActionBar().hide(); //hide the default actionbar


        bar=findViewById(R.id.pbr);
        name=findViewById(R.id.D_name);
        email=findViewById(R.id.D_Email);
        password=findViewById(R.id.D_password);
        number=findViewById(R.id.D_number);
        area=findViewById(R.id.D_area);
        aadharno=findViewById(R.id.D_adhaar);
        d_btn=findViewById(R.id.D_btn);
        toolbar=findViewById(R.id.toolbar2);

        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String pass=password.getText().toString();

                Pattern validpassword=Pattern.compile("^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                        // "(?=.*[a-zA-Z])" +      //any letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{8,}" +               //at least 8 characters
                        "$");



                if(Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                {
                    email.setTextColor(Color.BLACK);
                    if(!validpassword.matcher(pass).matches())
                    {
                        password.setTextColor(Color.RED);
                        password.setError("password is to weak");
                    }
                    else
                    {
                        password.setTextColor(Color.BLACK);
                    }
                }
                else
                {
                    email.setTextColor(Color.RED);
                    number.setError("Invalid Number");
                    email.setError("invalid Email");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        d_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    bar.setVisibility(View.VISIBLE);
                    String Name = name.getText().toString();
                    String Email = email.getText().toString();
                    String Password = password.getText().toString();
                    String Number = number.getText().toString();
                    String Area = area.getText().toString();
                    String Aadhar = aadharno.getText().toString();

                    driver_class d2=new driver_class(Name,Email,Password,Number,Aadhar);
                    if(!isConnected(CreateDriver.this))
                    {
                        bar.setVisibility(View.GONE);
                        ShowCustomDialog();
                    }
                    else if (TextUtils.isEmpty(Name)) {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(CreateDriver.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(Email)) {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(CreateDriver.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(Password)) {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(CreateDriver.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(Number)) {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(CreateDriver.this, "Please Enter Number", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(Area)) {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(CreateDriver.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(Aadhar)) {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(CreateDriver.this, "Please Enter Aadhar Card Number", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                        firebaseDatabase = FirebaseDatabase.getInstance("https://garbage-management-syste-4d66b-default-rtdb.firebaseio.com/");
                        databaseReference = firebaseDatabase.getReference("Drivers_Detail");

                        databaseReference.orderByChild("email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        bar.setVisibility(View.GONE);
                                        TSnackbar.make(toolbar,"Email ID Already Exist try Another Email ID", TSnackbar.LENGTH_LONG).show();
                                    }
                                    else {
                                        bar.setVisibility(View.GONE);
                                        databaseReference.push().setValue(d2);
                                        name.setText("");
                                        email.setText("");
                                        password.setText("");
                                        number.setText("");
                                        area.setText("");
                                        aadharno.setText("");
                                        Toast.makeText(CreateDriver.this,"Driver Registor", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(CreateDriver.this, "Fill All Driver Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void ShowCustomDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(CreateDriver.this);
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

    private boolean isConnected(CreateDriver createDriver)
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