package com.abhishekshrinath.garbagemanagementsystem;

import android.app.Activity;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UpdateBinAdapter extends ArrayAdapter {

    private Activity mcontext;
    List<Update_Bin_Object> update_bin_objects;

    public UpdateBinAdapter(@NonNull Activity mcontext, List<Update_Bin_Object> update_bin_objects) {
        super(mcontext, R.layout.update_bin_list, update_bin_objects);
        this.mcontext = mcontext;
        this.update_bin_objects = update_bin_objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mcontext.getLayoutInflater();
        View listitemview = inflater.inflate(R.layout.update_bin_list, null, true);
        TextView id = listitemview.findViewById(R.id.Bin_id);
        TextView status = listitemview.findViewById(R.id.update_bin_status);
        TextView taskcompleted = listitemview.findViewById(R.id.update_bin_dateTime);
        TextView area = listitemview.findViewById(R.id.update_bin_area);
        TextView locality = listitemview.findViewById(R.id.update_bin_locality);
        TextView city = listitemview.findViewById(R.id.update_bin_city);
        TextView load = listitemview.findViewById(R.id.update_bin_load);
        TextView Driver = listitemview.findViewById(R.id.update_bin_driver);
        TextView cleaning = listitemview.findViewById(R.id.update_bin_cleaning);
        TextView route = listitemview.findViewById(R.id.update_bin_route);
        TextView latitute = listitemview.findViewById(R.id.update_bin_latitute);
        TextView longitute = listitemview.findViewById(R.id.update_bin_longitute);
        Button updatemap=listitemview.findViewById(R.id.updatemap);
        Button delete=listitemview.findViewById(R.id.delete);

        Update_Bin_Object updateBinObject = update_bin_objects.get(position);
        id.setText(updateBinObject.getId());
        status.setText(updateBinObject.getStatus());
        taskcompleted.setText(updateBinObject.getWorkcompletetime());
        area.setText(updateBinObject.getArea());
        locality.setText(updateBinObject.getLocality());
        city.setText(updateBinObject.getCity());
        load.setText(updateBinObject.getLoad());
        Driver.setText(updateBinObject.getDriver());
        cleaning.setText(updateBinObject.getCleaning());
        route.setText(updateBinObject.getRoute());
        latitute.setText(updateBinObject.getLatitude());
        longitute.setText(updateBinObject.getLongtiude());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentActivity activity = (FragmentActivity)(mcontext);
                activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                FragmentManager fm = activity.getSupportFragmentManager();
                delete_bin_dialogbox dialogbox=new delete_bin_dialogbox();

                dialogbox.show(fm,"Did You Want to Delete Record?");
                activity.getIntent().putExtra("id",updateBinObject.getId());
            }
        });

        updatemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,MapsActivity.class);
                intent.putExtra("id",updateBinObject.getId());
                mcontext.startActivity(intent);

            }
        });
        return listitemview;
    }
}
