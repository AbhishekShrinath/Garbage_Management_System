package com.abhishekshrinath.garbagemanagementsystem;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class only_userview_Complaint_Adapter extends ArrayAdapter {
    Activity mcontext;
    List<usercomplaint> usercomplaintList;

    public only_userview_Complaint_Adapter(@NonNull Activity mcontext, List<usercomplaint> usercomplaintList) {
        super(mcontext, R.layout.only_user_complaint_listview, usercomplaintList);
        this.mcontext = mcontext;
        this.usercomplaintList = usercomplaintList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mcontext.getLayoutInflater();
        View listitemview = inflater.inflate(R.layout.only_user_complaint_listview, null, true);
        TextView id = listitemview.findViewById(R.id.U_ids);
        TextView area = listitemview.findViewById(R.id.U_Comlaint_Area);
        TextView status = listitemview.findViewById(R.id.U_status);
        TextView complaint = listitemview.findViewById(R.id.U_view_complaint);

        usercomplaint usercomplaint = usercomplaintList.get(position);
        id.setText(usercomplaint.getId());
        area.setText(usercomplaint.getArea());
        status.setText(usercomplaint.getStatus());
        complaint.setText(usercomplaint.getComplaint());

        return listitemview;
    }
}
