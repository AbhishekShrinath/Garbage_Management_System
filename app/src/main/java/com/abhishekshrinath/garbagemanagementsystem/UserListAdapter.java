package com.abhishekshrinath.garbagemanagementsystem;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class UserListAdapter extends ArrayAdapter {
    private Activity mcontext;
    List<UserDetails> userDetailsList;

    public UserListAdapter(@NonNull Activity mcontext, List<UserDetails> userDetailsList) {
        super(mcontext,R.layout.user_list,userDetailsList);
        this.mcontext=mcontext;
        this.userDetailsList=userDetailsList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=mcontext.getLayoutInflater();
        View listitemview=inflater.inflate(R.layout.user_list,null,true);
        TextView tvname=listitemview.findViewById(R.id.list_user_name);
        TextView tvemail=listitemview.findViewById(R.id.list_user_Email);
        TextView tvnumber=listitemview.findViewById(R.id.list_user_Number);

        UserDetails userDetails=userDetailsList.get(position);
        tvname.setText(userDetails.getU_name());
        tvemail.setText(userDetails.getU_email());
        tvnumber.setText(userDetails.getU_number());

        return listitemview;

    }

}
