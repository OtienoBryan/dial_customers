package com.topline.hub;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CollectFragment extends Fragment {
    Activity act = getActivity();

    public static String currentTime;


    Button btnNew,btnGeo,btnCollect;

    private TextView txtlatitude,txtlongitude,currentDate;
    TextView gpsAddress;
    private ProgressDialog progressDialog, checkinProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_collect, container, false);
        act=getActivity();


        progressDialog = new ProgressDialog(act);
        checkinProgress = new ProgressDialog(act);
        progressDialog.setMessage("Updating Location Please Wait...");
        checkinProgress.setMessage("Check In to Outlet Please Wait...");


        //// BLOCK TO GET THE LATITUDE AND LONGITUDE LOCATION FROM FEEDBACK ACTIVITY VIA BUNDLE

        gpsAddress = (TextView)v.findViewById(R.id.txtAddress);

        txtlatitude= (TextView) v.findViewById(R.id.location_latitude);
        txtlongitude= (TextView) v.findViewById(R.id.location_longitude);

        currentDate= (TextView) v.findViewById(R.id.currentDate);


        //UNPACK OUR DATA FROM OUR BUNDLE
        String latitude = null;
        String longitude = null;
        if (getArguments() != null) {
            latitude = this.getArguments().getString("LATITUDE_KEY");
            longitude = this.getArguments().getString("LONGITUDE_KEY");
        }

        txtlatitude.setText(latitude);
        txtlongitude.setText(longitude);

        double latt = Double.parseDouble(txtlatitude.getText().toString());
        double longi = Double.parseDouble(txtlongitude.getText().toString());

        getAddress(latt,longi);
        //getAddress(-1.3003341,36.7841735);

        /// TO GET THE CURRENT DATE

       /* Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());*/


        //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
       currentTime = DateFormat.getTimeInstance().format(new Date());




        btnNew = (Button)v.findViewById(R.id.btnNew);
        btnGeo = (Button)v.findViewById(R.id.btnCoordinate);
        btnCollect = (Button)v.findViewById(R.id.btnStart);

        //btnGeo.setText(currentDateTimeString);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(act, NewOutletActivity.class);
                startActivity(intent);

            }
        });


        btnGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               updateLocation();
            }
        });

        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userCheckin();
                /*Intent intent = new Intent(act, ImageActivity.class);
                startActivity(intent);*/
            }
        });


        currentDate();









        return v;
    }


    public void currentDate(){

        //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        currentDate.setText(formattedDate);

    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(act, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String   add = obj.getAddressLine(0);
            String myaddress = obj.getAddressLine(0); /////The Current Address
            String  currentAddress = obj.getSubAdminArea() + ","
                    + obj.getAdminArea();
            double   latitude = obj.getLatitude();
            double longitude = obj.getLongitude();
            String currentCity= obj.getSubAdminArea();
            String currentState= obj.getAdminArea();
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();


            System.out.println("obj.getCountryName()"+obj.getCountryName());
            System.out.println("obj.getCountryCode()"+obj.getCountryCode());
            System.out.println("obj.getAdminArea()"+obj.getAdminArea());
            System.out.println("obj.getPostalCode()"+obj.getPostalCode());
            System.out.println("obj.getSubAdminArea()"+obj.getSubAdminArea());
            System.out.println("obj.getLocality()"+obj.getLocality());
            System.out.println("obj.getSubThoroughfare()"+obj.getSubThoroughfare());


            gpsAddress.setText(myaddress);
            Log.v("IGA", "Address" + myaddress);



            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(act, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    private void updateLocation(){
        final String outlet_latitude = txtlatitude.getText().toString().trim();
        final String outlet_longitude = txtlongitude.getText().toString().trim();
        final String outlet_address = gpsAddress.getText().toString().trim();
        final String user_id = SharedPrefManager.getInstance(act).getUserId().toString();
        final String appoint_date = currentDate.getText().toString().trim();


        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_TERMINATE_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj =new JSONObject(response);
                            if (!obj.getBoolean("error")){

                                Toast.makeText(
                                        act,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();

                                //showStartActivation();

                                //setFragment(new LoginFragment());

                            }else {
                                Toast.makeText(
                                        act,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                act,
                                "Error Ocured try again",
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("outlet_latitude", outlet_latitude);
                params.put("outlet_longitude", outlet_longitude);
                params.put("outlet_address", outlet_address);
                params.put("appoint_date", appoint_date);

                return params;

            }
        };


        RequestHandler.getInstance(act).addToRequestQueue(stringRequest);
    }




    private void userCheckin(){
        final String outlet_address = gpsAddress.getText().toString().trim();
        final String user_id = SharedPrefManager.getInstance(act).getUserId().toString();
        final String outlet_date = currentDate.getText().toString().trim();

        final String current_time = currentTime;


        checkinProgress.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_CHECKIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        checkinProgress.dismiss();

                        try {
                            JSONObject obj =new JSONObject(response);
                            if (!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(act)
                                        .userCheckin(
                                                obj.getString("id"),
                                                obj.getInt("outlet_id"),
                                                obj.getString("outlet_name")


                                        );

                                startActivity(new Intent(act, ImageActivity.class));
                                act.finish();



                            }else {
                                Toast.makeText(
                                        act,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkinProgress.dismiss();

                        Toast.makeText(
                                act,
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("appoint_date", outlet_date);
                params.put("outlet_address", outlet_address);
                params.put("currentTime", current_time);

                return params;

            }
        };


        RequestHandler.getInstance(act).addToRequestQueue(stringRequest);
    }









    public void setFragment(Fragment f){

        FragmentManager fManager = getFragmentManager();
        FragmentTransaction ft = fManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.frame, f);
        ft.commit();

    }
}
