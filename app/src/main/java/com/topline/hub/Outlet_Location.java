package com.topline.hub;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Outlet_Location extends Fragment{

    TextView txtName,txtLocation,txtAddress;
    String name,address,location;
    Double latitude,longitude;
    Context context;

    @SuppressLint("ValidFragment")
    public Outlet_Location(String name, String address, String location, String latitude, String longitude, Context context) {
        this.name = name;
        this.address = address;
        this.location = location;
        try {
            this.latitude = Double.parseDouble(latitude);
            this.longitude = Double.parseDouble(longitude);
        }catch (Exception e){
            this.latitude = 0.0;
            this.longitude = 0.0;
        }
        this.context = context;
    }

    public Outlet_Location() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_outlet__location, container, false);
        txtName = v.findViewById(R.id.txtOutletLocationName);
        txtAddress = v.findViewById(R.id.txtOutletLocationAddress);
        txtLocation = v.findViewById(R.id.txtOutletLocationCoordinates);
        Button btnMap = (Button) v.findViewById(R.id.btnOutletLocationMap);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(latitude == 0.0 || longitude == 0.0) {
                    Toast.makeText(context, "The outlet coordinates are not setup.", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(context.getApplicationContext(), OutletMapsActivity.class);
                    intent.putExtra("LATITUDE", latitude);
                    intent.putExtra("LONGITUDE", longitude);
                    intent.putExtra("NAME", name);
                    startActivity(intent);
                }
            }
        });

        txtName.setText(name);
        txtAddress.setText(address);
        txtLocation.setText(location);
        return v;
    }

}
