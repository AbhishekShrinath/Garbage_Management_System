package com.abhishekshrinath.garbagemanagementsystem;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class userview_complaint_Adapter extends ArrayAdapter {
    Activity mcontext;
    List<usercomplaint> usercomplaintList;

    public userview_complaint_Adapter(@NonNull Activity mcontext, List<usercomplaint> usercomplaintList) {
        super(mcontext, R.layout.user_complaint_listview, usercomplaintList);
        this.mcontext = mcontext;
        this.usercomplaintList = usercomplaintList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mcontext.getLayoutInflater();
        View listitemview = inflater.inflate(R.layout.user_complaint_listview, null, true);
        TextView id = listitemview.findViewById(R.id.ids);
        TextView area = listitemview.findViewById(R.id.Comlaint_Area);
        TextView status = listitemview.findViewById(R.id.status);
        TextView complaint = listitemview.findViewById(R.id.view_complaint);
        Button delete=listitemview.findViewById(R.id.deletecomplaintbtn);

        usercomplaint usercomplaint = usercomplaintList.get(position);
        id.setText(usercomplaint.getId());
        area.setText(usercomplaint.getArea());
        status.setText(usercomplaint.getStatus());
        complaint.setText(usercomplaint.getComplaint());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentActivity activity = (FragmentActivity)(mcontext);
                activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                FragmentManager fm = activity.getSupportFragmentManager();
                deletecomplaint_dialogbox dialogbox=new deletecomplaint_dialogbox();

                dialogbox.show(fm,"Did You Want Realy to Delete Record?");
                activity.getIntent().putExtra("id",usercomplaint.getId());
            }
        });
        return listitemview;
    }
}