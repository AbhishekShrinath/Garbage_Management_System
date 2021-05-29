package com.abhishekshrinath.garbagemanagementsystem;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class dialogbox_status_update extends AppCompatDialogFragment {
    private TextView textView_id;
    private Spinner spinner_status;
    private DatabaseReference dref;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.dialogbox_status_update,null,true);

        textView_id=view.findViewById(R.id.Recive_id);
        spinner_status=view.findViewById(R.id.Up_work_detail);
        String array[]={"pending","Completed","Rejected","on Process"};


        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, array);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(Adapter);

        Bundle bundle = getActivity().getIntent().getExtras();
        String id = bundle.getString("id");
        textView_id.setText(id);
        dref= FirebaseDatabase.getInstance().getReference("Create Bin");

        builder.setView(view).setTitle("status").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dref.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String savecurrentdate, savecurrenttime;
                        Calendar calendarfordate = Calendar.getInstance();
                        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
                        savecurrentdate = currentdate.format(calendarfordate.getTime());
                        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
                        savecurrenttime = currenttime.format(calendarfordate.getTime());

                        DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("status", spinner_status.getSelectedItem().toString());
                        result.put("workcompletetime",savecurrentdate+" & "+savecurrenttime);
                        dref.child(key).updateChildren(result);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return builder.create();
    }
}
