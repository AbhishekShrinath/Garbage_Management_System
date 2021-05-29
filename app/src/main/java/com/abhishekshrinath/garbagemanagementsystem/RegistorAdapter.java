package com.abhishekshrinath.garbagemanagementsystem;

import android.app.Activity;
import android.content.Context;
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

public class RegistorAdapter extends ArrayAdapter {
    Activity mcontext;
    List<Update_Bin_Object> update_bin_objects;

    public RegistorAdapter(@NonNull Activity mcontext, List<Update_Bin_Object> update_bin_objects) {
        super(mcontext, R.layout.update_bin_list, update_bin_objects);
        this.mcontext = mcontext;
        this.update_bin_objects = update_bin_objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mcontext.getLayoutInflater();
        View listitemview = inflater.inflate(R.layout.registor_list, null, true);

        TextView id = listitemview.findViewById(R.id.R_id);
        TextView area = listitemview.findViewById(R.id.R_area);
        TextView locality = listitemview.findViewById(R.id.R_locality);
        TextView city = listitemview.findViewById(R.id.R_city);
        Button ticket=listitemview.findViewById(R.id.ticket);

        Update_Bin_Object updateBinObject = update_bin_objects.get(position);
        id.setText(updateBinObject.getId());
        area.setText(updateBinObject.getArea());
        locality.setText(updateBinObject.getLocality());
        city.setText(updateBinObject.getCity());
        ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,RegistorComplaintActivity2.class);
                intent.putExtra("id",updateBinObject.getId());
                intent.putExtra("area",updateBinObject.getArea());
                intent.putExtra("Locality",updateBinObject.getLocality());
                intent.putExtra("City",updateBinObject.getCity());
                mcontext.startActivity(intent);
            }
        });
        return listitemview;
    }
}
