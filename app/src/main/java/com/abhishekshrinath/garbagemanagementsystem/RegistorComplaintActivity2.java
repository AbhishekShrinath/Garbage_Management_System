package com.abhishekshrinath.garbagemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistorComplaintActivity2 extends AppCompatActivity {
        private EditText area,locality,city,email,complaint;
        private TextView id;
        private Button submitbtn;
        private DatabaseReference databaseReference;
        private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor_complaint2);

        String get_id=getIntent().getExtras().getString("id");
        String get_area=getIntent().getExtras().getString("area");
        String get_locality=getIntent().getExtras().getString("Locality");
        String get_city=getIntent().getExtras().getString("City");

        id=findViewById(R.id.recive_id);
        area=findViewById(R.id.recive_area);
        area=findViewById(R.id.recive_area);
        locality=findViewById(R.id.recive_locality);
        city=findViewById(R.id.recive_city);
        email=findViewById(R.id.res_EMail);
        complaint=findViewById(R.id.post_complaint);
        submitbtn=findViewById(R.id.submit_complaint_btn);
        bar=findViewById(R.id.prograss_bar2);

        id.setText(get_id);
        area.setText(get_area);
        locality.setText(get_locality);
        city.setText(get_city);

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String value = sharedPreferences.getString("value","");
        email.setText(value);

        String status="pending";


            submitbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bar.setVisibility(View.VISIBLE);
                    usercomplaint user = new usercomplaint(id.getText().toString(), area.getText().toString(), locality.getText().toString(),
                            city.getText().toString(), email.getText().toString(), complaint.getText().toString(), status);
                    if(TextUtils.isEmpty(complaint.getText().toString()))
                    {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(RegistorComplaintActivity2.this, "Please Enter your Complaint", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                            databaseReference = FirebaseDatabase.getInstance().getReference("UserComplaint");
                            databaseReference.push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    bar.setVisibility(View.GONE);
                                    Toast.makeText(RegistorComplaintActivity2.this, "complaint Registor", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistorComplaintActivity2.this, UserControl.class);
                                    startActivity(intent);
                                }
                            });
                        }
                }
            });

    }
}