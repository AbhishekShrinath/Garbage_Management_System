package com.abhishekshrinath.garbagemanagementsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class ListAdapter extends ArrayAdapter {

    private Activity mcontext;
    List<driver_class> driver_classList;

    public ListAdapter(@NonNull Activity mcontext,List<driver_class> driver_classList) {
        super(mcontext,R.layout.list_of_drivers,driver_classList);
        this.mcontext=mcontext;
        this.driver_classList=driver_classList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=mcontext.getLayoutInflater();
        View listitemview=inflater.inflate(R.layout.list_of_drivers,null,true);
        TextView tvname=listitemview.findViewById(R.id.list_driver_name);
        TextView tvemail=listitemview.findViewById(R.id.list_driver_Email);
        TextView tvnumber=listitemview.findViewById(R.id.list_driver_Number);
        TextView tvaadhar=listitemview.findViewById(R.id.list_driver_Aadhar);
        Button delete=listitemview.findViewById(R.id.removeDriver);
        Button update=listitemview.findViewById(R.id.updateDriver);

        driver_class driverClass=driver_classList.get(position);
        tvname.setText(driverClass.getName());
        tvemail.setText(driverClass.getEmail());
        tvnumber.setText(driverClass.getNumber());
        tvaadhar.setText(driverClass.getAadhar());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mcontext,R.style.CustomAlertDialog);
                ViewGroup viewGroup = listitemview.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(mcontext).inflate(R.layout.deletedriver_dialogbox, viewGroup, false);
                FloatingActionButton remove=dialogView.findViewById(R.id.remove_driver);
                TextView Email=dialogView.findViewById(R.id.email_remove);
                Email.setText(driverClass.getEmail());

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Drivers_Detail");
                        databaseReference.orderByChild("email").equalTo(Email.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                                String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                                databaseReference.child(key).removeValue();
                                Intent intent=new Intent(mcontext,ViewDriver.class);
                                mcontext.startActivity(intent);
                                Toast.makeText(mcontext, "Driver Removed", Toast.LENGTH_SHORT).show();

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }

        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mcontext,R.style.CustomAlertDialog);
                ViewGroup viewGroup = listitemview.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(mcontext).inflate(R.layout.admin_view_dirver_details_dialogbox, viewGroup, false);
                FloatingActionButton cancel=dialogView.findViewById(R.id.back);
                TextView Email=dialogView.findViewById(R.id.txt4_email);
                TextView getname=dialogView.findViewById(R.id.edit4_driver_name);
                TextView getnumber=dialogView.findViewById(R.id.edit4_driver_number);
                TextView getaadhar=dialogView.findViewById(R.id.edit4_driver_Aadhar);

                Email.setText(driverClass.getEmail());
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Drivers_Detail");
                databaseReference.orderByChild("email").equalTo(Email.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot datas: snapshot.getChildren())
                        {
                            String driver_name = datas.child("name").getValue(String.class);
                            String driver_number = datas.child("number").getValue(String.class);
                            String driver_aadhar = datas.child("aadhar").getValue(String.class);

                            getname.setText(driver_name);
                            getnumber.setText(driver_number);
                            getaadhar.setText(driver_aadhar);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(mcontext, "Fails to fetch data", Toast.LENGTH_SHORT).show();
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
        });
        return listitemview;

    }

}
