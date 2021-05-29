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
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CreateBin_Activity extends AppCompatActivity {

    private EditText area,locality,Uplandmark,city,Best_route;
    private Spinner load_type,cleaning_peroid,email_spinner;
    private Button btn;
    private DatabaseReference databaseReference;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_bin_);
        getSupportActionBar().hide(); //hide the default actionbar

        String load[]={"low","High","Medium"};
        String cpd[]={"Daily","Weekly","Monthly"};

        int y = random.nextInt(1000);


        area=findViewById(R.id.Bin_Area);
        locality=findViewById(R.id.locality);
        Uplandmark=findViewById(R.id.Ulandmark);
        city=findViewById(R.id.Bin_City);
        Best_route=findViewById(R.id.Route);

        load_type=findViewById(R.id.load_type);
        cleaning_peroid=findViewById(R.id.cleaning_period);
        email_spinner=findViewById(R.id.spinner);
        if(!isConnected(CreateBin_Activity.this))
        {
            ShowCustomDialog();
        }
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, load);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        load_type.setAdapter(adp1);

        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, cpd);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cleaning_peroid.setAdapter(adp2);

        btn=findViewById(R.id.bin_btn);

        databaseReference=FirebaseDatabase.getInstance().getReference("Drivers_Detail");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> list=new ArrayList<>();
                for(DataSnapshot emailsnapshot: snapshot.getChildren())
                {
                    String Email_sp = emailsnapshot.child("email").getValue(String.class);
                    list.add(Email_sp);

                }
                ArrayAdapter<String> Adapter = new ArrayAdapter<String>(CreateBin_Activity.this, android.R.layout.simple_spinner_item, list);
                Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                email_spinner.setAdapter(Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Area=area.getText().toString();
                String Locality=locality.getText().toString();
                String uLandmark=Uplandmark.getText().toString();
                String City=city.getText().toString();
                String A_Email=email_spinner.getSelectedItem().toString();
                String B_Route=Best_route.getText().toString();

                String LoadVlaue=load_type.getSelectedItem().toString();
                String CleaningValue=cleaning_peroid.getSelectedItem().toString();

                if(TextUtils.isEmpty(Area))
                {
                    Toast.makeText(CreateBin_Activity.this, "Please Enter Area", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(Locality))
                {
                    Toast.makeText(CreateBin_Activity.this, "Please Enter locality", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(uLandmark))
                {
                    Toast.makeText(CreateBin_Activity.this, "Please Enter Landmark", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(City))
                {
                    Toast.makeText(CreateBin_Activity.this, "Please Enter City", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(A_Email))
                {
                    Toast.makeText(CreateBin_Activity.this, "Please Enter Driver Email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(B_Route))
                {
                    Toast.makeText(CreateBin_Activity.this, "Enter Route to work", Toast.LENGTH_SHORT).show();
                }
                else if(!isConnected(CreateBin_Activity.this))
                {
                    ShowCustomDialog();
                }
                else {
                    databaseReference= FirebaseDatabase.getInstance("https://garbage-management-syste-4d66b-default-rtdb.firebaseio.com/")
                            .getReference("Create Bin");
//
//                    final HashMap<String, Object> cartmap = new HashMap<>();
//                    cartmap.put("id",y);
//                    cartmap.put("area",Area);
//                    cartmap.put("locality",Locality);
//                    cartmap.put("UlandMark",uLandmark);
//                    cartmap.put("city",City);
//                    cartmap.put("Driver",A_Email);
//                    cartmap.put("load",LoadVlaue);
//                    cartmap.put("cleaning",CleaningValue);
//                    cartmap.put("route",B_Route);
//                    cartmap.put("status","pending");
                    Update_Bin_Object updateBinObject=new Update_Bin_Object(String.valueOf(y),Area,Locality,uLandmark,City,LoadVlaue,A_Email,CleaningValue,B_Route,"","","pending","not Completed");
                    databaseReference.push().setValue(updateBinObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(CreateBin_Activity.this, "Bin is Created", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        }
                    });
                }
            }
        });
    }
    private void ShowCustomDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(CreateBin_Activity.this);
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

    private boolean isConnected(CreateBin_Activity createBin_activity)
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