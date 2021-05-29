package com.abhishekshrinath.garbagemanagementsystem;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdminView_Complaint_Adapter extends ArrayAdapter {
    Activity mcontext;
    List<usercomplaint> usercomplaintList;

    public AdminView_Complaint_Adapter(@NonNull Activity mcontext, List<usercomplaint> usercomplaintList) {
        super(mcontext, R.layout.admin_view_complaint_list, usercomplaintList);
        this.mcontext = mcontext;
        this.usercomplaintList = usercomplaintList;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mcontext.getLayoutInflater();
        View listitemview = inflater.inflate(R.layout.admin_view_complaint_list, null, true);

        TextView email = listitemview.findViewById(R.id.admin_view_email);
        TextView area = listitemview.findViewById(R.id.admin_view_Comlaint_Area);
        TextView status = listitemview.findViewById(R.id.admin_view_status);
        TextView complaint = listitemview.findViewById(R.id.admin_view_view_complaint);
        Button updatestatusbtn=listitemview.findViewById(R.id.updatestatus_btn);

        usercomplaint usercomplaint = usercomplaintList.get(position);
        email.setText(usercomplaint.getEmail());
        area.setText(usercomplaint.getArea());
        status.setText(usercomplaint.getStatus());
        complaint.setText(usercomplaint.getComplaint());
        updatestatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,updateStatus_Activity.class);
                intent.putExtra("email",usercomplaint.getEmail());
                intent.putExtra("complaint",usercomplaint.getComplaint());
                mcontext.startActivity(intent);

            }
        });
        return listitemview;
    }

}
