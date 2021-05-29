package com.abhishekshrinath.garbagemanagementsystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class deletecomplaint_dialogbox extends AppCompatDialogFragment {
    private TextView textView_id;
    private DatabaseReference dref;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.deletecomplaint_dialogbox,null,true);
        textView_id=view.findViewById(R.id.delete_ids);
        Bundle bundle = getActivity().getIntent().getExtras();
        String id = bundle.getString("id");
        textView_id.setText(id);
        dref= FirebaseDatabase.getInstance().getReference("UserComplaint");
        builder.setView(view).setTitle("Did You Realy Want to Delete Complaint?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dref.orderByChild("id").equalTo(textView_id.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        dref.child(key).removeValue();
                        Intent intent =new Intent(getActivity(),WorkReport_Activity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();
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
