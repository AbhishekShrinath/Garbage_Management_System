package com.abhishekshrinath.garbagemanagementsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class Driver_list_Adapter extends ArrayAdapter {
    Activity mcontext;
    List<Update_Bin_Object> update_bin_objects;
    public Driver_list_Adapter(@NonNull Activity mcontext, List<Update_Bin_Object> update_bin_objects) {
        super(mcontext, R.layout.driver_list_view, update_bin_objects);
        this.mcontext = mcontext;
        this.update_bin_objects = update_bin_objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mcontext.getLayoutInflater();
        View listitemview = inflater.inflate(R.layout.driver_list_view, null, true);

        TextView id = listitemview.findViewById(R.id.UP_BIN_ID);
        TextView area = listitemview.findViewById(R.id.UP_area);
        TextView locality = listitemview.findViewById(R.id.UP_locality);
        TextView city = listitemview.findViewById(R.id.UP_city);
        TextView load = listitemview.findViewById(R.id.UP_load);
        TextView Driver = listitemview.findViewById(R.id.UP_driver);
        TextView cleaning = listitemview.findViewById(R.id.UP_cleaning);
        TextView route = listitemview.findViewById(R.id.UP_route);
        TextView latitute = listitemview.findViewById(R.id.UP_latitute);
        TextView longitute = listitemview.findViewById(R.id.UP_longitute);
        TextView status = listitemview.findViewById(R.id.Up_staus);
        Button updateWork=listitemview.findViewById(R.id.D_updateStatus);
        Button updateMap=listitemview.findViewById(R.id.updatemap);


        Update_Bin_Object updateBinObject = update_bin_objects.get(position);
        id.setText(updateBinObject.getId());
        area.setText(updateBinObject.getArea());
        locality.setText(updateBinObject.getLocality());
        city.setText(updateBinObject.getCity());
        load.setText(updateBinObject.getLoad());
        Driver.setText(updateBinObject.getDriver());
        cleaning.setText(updateBinObject.getCleaning());
        route.setText(updateBinObject.getRoute());
        latitute.setText(updateBinObject.getLatitude());
        longitute.setText(updateBinObject.getLongtiude());
        status.setText(updateBinObject.getStatus());

        updateMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,View_Map.class);
                intent.putExtra("lat",updateBinObject.getLatitude());
                intent.putExtra("long",updateBinObject.getLongtiude());
                mcontext.startActivity(intent);
                Toast.makeText(mcontext, "View on Map", Toast.LENGTH_SHORT).show();
            }
        });
        updateWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity)(mcontext);
                activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                FragmentManager fm = activity.getSupportFragmentManager();
                dialogbox_status_update dialogbox=new dialogbox_status_update();

                dialogbox.show(fm,"status");
                activity.getIntent().putExtra("id",updateBinObject.getId());
                Toast.makeText(mcontext, "UPdate Work", Toast.LENGTH_SHORT).show();
            }
        });
        return listitemview;
    }
}
