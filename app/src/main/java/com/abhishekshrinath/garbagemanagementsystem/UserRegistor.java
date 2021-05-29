package com.abhishekshrinath.garbagemanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class UserRegistor extends AppCompatActivity {

    Button btn;
    EditText name,email,number,password;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    ProgressBar bar;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user_registor);
        getSupportActionBar().hide(); //hide the default actionbar

        name=findViewById(R.id.username);
        email=findViewById(R.id.email);
        number=findViewById(R.id.number);
        password=findViewById(R.id.password);
        btn=findViewById(R.id.btn);
        bar=findViewById(R.id.bar);
        toolbar=findViewById(R.id.toolbar);

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

        //  String userid="User-"+Name;
        firebaseDatabase = FirebaseDatabase.getInstance("https://garbage-management-syste-4d66b-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("UserDetails");
        firebaseAuth=FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    bar.setVisibility(View.VISIBLE);
                    String Name = name.getText().toString();

                    String Email = email.getText().toString();
                    String Number = number.getText().toString();
                    String Pass = password.getText().toString();



                    if (TextUtils.isEmpty(Name))
                    {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(UserRegistor.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(Email))
                    {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(UserRegistor.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(Pass))
                    {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(UserRegistor.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(Number))
                    {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(UserRegistor.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                    else if(!isConnected(UserRegistor.this))
                    {
                        bar.setVisibility(View.GONE);
                        ShowCustomDialog();
                    }
                    else
                    {
                    firebaseAuth.fetchSignInMethodsForEmail(Email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            boolean check=!task.getResult().getSignInMethods().isEmpty();
                            if(check){
                                bar.setVisibility(View.GONE);
                                TSnackbar.make(toolbar,"Email ID Already Exist try Another Email ID", TSnackbar.LENGTH_LONG).show();
                            }
                            else
                            {
                                firebaseAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(UserRegistor.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            UserDetails details = new UserDetails(Name, Email, Number);
                                            firebaseDatabase.getInstance().getReference("UserDetails").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    bar.setVisibility(View.GONE);
                                                    Toast.makeText(UserRegistor.this, "Registarstion Complete", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), HomePage.class));
                                                }
                                            });

                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            }
            catch (Exception e)
            {
                bar.setVisibility(View.GONE);
                Toast.makeText(UserRegistor.this, "Fill the Form", Toast.LENGTH_SHORT).show();
            }
        }

    });



    }
    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(UserRegistor.this);
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

    private boolean isConnected(UserRegistor userRegistor)
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