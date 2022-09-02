package com.topline.hub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class OutletFragment extends Fragment {
    Activity act;

    Button btnOutlet,btnVisited,btnRegisterOutlet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_outlet, container, false);
        act=getActivity();



        btnOutlet = (Button) v.findViewById(R.id.btnOutlet);
        btnVisited = (Button) v.findViewById(R.id.btnVisited);
       // btnRegisterOutlet = (Button) v.findViewById(R.id.registerOutlet);


        btnOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, Outlets.class));

            }
        });

        btnVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, OutletsVisited.class));

            }
        });


//        btnRegisterOutlet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(act, NewOutletActivity.class);
//                startActivity(intent);
//
//            }
//        });


        return v;
    }

}
