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
import android.text.TextUtils;
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

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {
    private EditText user_email,user_password;
    private Button userloginbtn;
    private FirebaseAuth firebaseAuth;
    private TextView forgetpassword;
    private SpinKitView bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user_login);
        getSupportActionBar().hide(); //hide the default actionbar

        bar=findViewById(R.id.prograss_bar1q);
        user_email=findViewById(R.id.user_email);
        user_password=findViewById(R.id.user_pass);
        userloginbtn=findViewById(R.id.user_login_btn);
        forgetpassword=findViewById(R.id.forgetpassword_id);

        Sprite foldingCube = new FoldingCube();
        bar.setIndeterminateDrawable(foldingCube);

        firebaseAuth=FirebaseAuth.getInstance();

        userloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);

                String Email=user_email.getText().toString().trim();
                String Pass=user_password.getText().toString().trim();
                if(TextUtils.isEmpty(Email))
                {
                    bar.setVisibility(View.GONE);
                    Toast.makeText(UserLogin.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(Pass))
                {
                    bar.setVisibility(View.GONE);
                    Toast.makeText(UserLogin.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                }
                else if(!isConnected(UserLogin.this))
                {
                    bar.setVisibility(View.GONE);
                    ShowCustomDialog();
                }
                else
                {
                    firebaseAuth.signInWithEmailAndPassword(Email, Pass)
                    .addOnCompleteListener(UserLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                bar.setVisibility(View.GONE);
                                SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("value", Email);
                                editor.apply();
                                Toast.makeText(UserLogin.this, "LoginSuccessfull", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(UserLogin.this,UserControl.class);
                                startActivity(intent);

                            } else {
                                bar.setVisibility(View.GONE);
                                Toast.makeText(UserLogin.this, "email and password does not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UserLogin.this,R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.forgetpassword_customview, viewGroup, false);
                Button send=dialogView.findViewById(R.id.sendLinkBtn);
                EditText inputEmail=dialogView.findViewById(R.id.text_email);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bar.setVisibility(View.VISIBLE);

                        firebaseAuth=FirebaseAuth.getInstance();
                        String email = inputEmail.getText().toString().trim();

                        if (TextUtils.isEmpty(email))
                        {
                            bar.setVisibility(View.GONE);
                            Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        bar.setVisibility(View.GONE);
                                        Toast.makeText(UserLogin.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        bar.setVisibility(View.GONE);
                                        Toast.makeText(UserLogin.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void regidtorbtn(View view) {
        Intent intent=new Intent(UserLogin.this,UserRegistor.class);
        startActivity(intent);
    }
    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(UserLogin.this);
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

    private boolean isConnected(UserLogin userLogin)
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