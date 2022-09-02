package com.topline.hub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentAction extends AppCompatActivity {



    ///// BOCK CODE FOR GPS LOCATION SERVICES....................START

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.location_result)
    TextView txtLocationResult;

    @BindView(R.id.location_longitude)
    TextView txtLocationLongitude;

    @BindView(R.id.updated_on)
    TextView txtUpdatedOn;

    @BindView(R.id.btn_start_location_updates)
    Button btnStartUpdates;

    @BindView(R.id.btn_stop_location_updates)
    Button btnStopUpdates;

    // location last updated time
    private String mLastUpdateTime;

    //location updates interval - 2min
    // location updates interval - 10SEC
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 10 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    public static String user_id;
    public static String admin_id;
    public static String outlet_ids;
    public static String my_outlet;


    // bunch of location related apis

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    //// BLOCK CODE FOR GPS LOCATION SERVICES..................END

    public static String currentTime;

    public static String appoint_id;


    Button btnCancel,btnFollowup,btnGeo,btnCheckin;

    private TextView txtlatitude,txtlongitude,currentDate, appointId,outId,battery_level;
    TextView gpsAddress;
    private ProgressDialog progressDialog, checkinProgress, cancelProgress;

    //public static String outlet_id;

    //public static String battery_level;

    //// Block of code for Getting the battry purcentage level

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            battery_level.setText(String.valueOf(level));
            //battery_level = String.valueOf(level);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //RECEIVE INTENT
        Intent i = this.getIntent();
        String id = i.getExtras().getString("ID_KEY");
        String outlet_name = i.getExtras().getString("NAME_KEY");
        String outlet_id = i.getExtras().getString("OUTLET_ID_KEY");
        String end_time = i.getExtras().getString("END_TIME_KEY");

        final String appoint_latitude = i.getExtras().getString("LATITUDE_KEY");
        final String appoint_longitude = i.getExtras().getString("LONGITUDE_KEY");

        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();
        outlet_ids = SharedPrefManager.getInstance(this).getOutletId().toString();
        my_outlet = outlet_id;

        visitValidation();

        //String value = getIntent().getExtras().getString("LATITUDE_KEY");

        setTitle(outlet_name);
        setContentView(R.layout.activity_appointment_action);

        appoint_id = id;

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        checkinProgress = new ProgressDialog(this);
        cancelProgress = new ProgressDialog(this);
        progressDialog.setMessage("Updating Location Please Wait...");
        checkinProgress.setMessage("Check In to Outlet Please Wait...");
        cancelProgress.setMessage("Canceling Appointment Please Wait...");


        /// GPS LOCATION BLOCK...........START
        ButterKnife.bind(this);
        // initialize the necessary libraries
        init();
        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
        /// GPS LOCATION BLOCK ...........END
        //startLocationUpdates();
        //updateLocationUI();





        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();

                        // Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();








        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnFollowup = (Button)findViewById(R.id.btnFollowup);
        btnGeo = (Button)findViewById(R.id.btnCoordinate);
        btnCheckin = (Button)findViewById(R.id.btnCheckin);

        gpsAddress = (TextView)findViewById(R.id.mytxtAddress);

        txtlatitude= (TextView)findViewById(R.id.mylocation_latitude);
        txtlongitude= (TextView)findViewById(R.id.mylocation_longitude);
        appointId= (TextView)findViewById(R.id.txtId);
        outId= (TextView)findViewById(R.id.txtoutId);
        battery_level= (TextView)findViewById(R.id.batteryLevel);

        appointId.setText(id);
        outId.setText(outlet_id);


        //// Block of code for Getting the battry purcentage level...START
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        //// Block of code for Getting the battry purcentage level...END



        currentDate= (TextView)findViewById(R.id.currentDate);

       /* if (appoint_latitude=='' && appoint_longitude=){

            btnGeo.setEnabled(false);

        }else{

            btnGeo.setEnabled(false);
            btnGeo.setBackgroundColor(getResources().getColor(R.color.grey));
        } */


//        if(TextUtils.isEmpty(appoint_latitude) || TextUtils.isEmpty(appoint_longitude)){
//            btnGeo.setEnabled(true);
//        }else {
//            btnGeo.setEnabled(false);
//            btnGeo.setBackgroundColor(getResources().getColor(R.color.grey));
//        }


        btnFollowup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = null;
                //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", appoint_latitude, appoint_longitude);
                intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:"+appoint_latitude+","+appoint_longitude+""));
                startActivity(intent);

//                Toast.makeText(
//                        AppointmentAction.this, "Just a test",
//                        Toast.LENGTH_LONG
//                ).show();
            }
        });



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChangeLangDialog();

            }
        });



        btnGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateLocation();
            }
        });

        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String outlet_address = gpsAddress.getText().toString().trim();

                if(outlet_address.equals("Address")){
                    Toast.makeText(getApplicationContext(), "Geo_Locate to Check in", Toast.LENGTH_LONG).show();


                }else{
                    userCheckin();

                }


            }
        });



        //UNPACK OUR DATA FROM OUR BUNDLE

        currentDate();







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
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    private void updateLocation(){
        final String outlet_latitude = txtlatitude.getText().toString().trim();
        final String outlet_longitude = txtlongitude.getText().toString().trim();
        final String outlet_address = gpsAddress.getText().toString().trim();
        final String user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        final String appoint_id = appointId.getText().toString().trim();
        final String outlet_id = outId.getText().toString().trim();
       // final String appoint_date = currentDate.getText().toString().trim();


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
                                        AppointmentAction.this,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();

                                //showStartActivation();

                                //setFragment(new LoginFragment());

                            }else {
                                Toast.makeText(
                                        AppointmentAction.this,
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
                                AppointmentAction.this,
                                "Error Occurred try again",
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
                //params.put("appoint_date", appoint_date);
                params.put("appoint_id", appoint_id);
                params.put("outlet_id", outlet_id);

                return params;

            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void userCheckin(){
        currentTime = DateFormat.getTimeInstance().format(new Date());
        final String outlet_address = gpsAddress.getText().toString().trim();
        final String user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        final String outlet_date = currentDate.getText().toString().trim();
        final String batteryLevel = battery_level.getText().toString().trim();
        final String appointment_id = appoint_id.trim(); // Outlet Id is the appointment ID.

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
                                SharedPrefManager.getInstance(AppointmentAction.this)
                                        .userCheckin(
                                                obj.getString("id"),
                                                obj.getInt("outlet_id"),
                                                obj.getString("outlet_name")


                                        );

                                startActivity(new Intent(AppointmentAction.this, ImageActivity.class));
                                //finish();



                            }else {
                                Toast.makeText(
                                        AppointmentAction.this,
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
                                AppointmentAction.this,
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
                params.put("battery_level", batteryLevel);
                params.put("appointment_id", appointment_id);

                return params;

            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    ////// THIS BLOCK OF CODES FOR GPS LOCATION SERVICES...........................START...


    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }


    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
           /* txtLocationResult.setText(
                    "Lat: " + mCurrentLocation.getLatitude() + ", " +
                            "Lng: " + mCurrentLocation.getLongitude()
            );*/
            txtLocationResult.setText(
                    ""+ mCurrentLocation.getLatitude()
            );

            txtLocationLongitude.setText(
                    ""+mCurrentLocation.getLongitude()
            );

            // giving a blink animation on TextView
            txtLocationResult.setAlpha(0);
            txtLocationResult.animate().alpha(1).setDuration(300);
            txtLocationLongitude.setAlpha(0);
            txtLocationLongitude.animate().alpha(1).setDuration(300);

            // location last updated time
            txtUpdatedOn.setText("Last updated on: " + mLastUpdateTime);

            txtlatitude.setText(txtLocationResult.getText());
            txtlongitude.setText(txtLocationLongitude.getText());

             double latt = Double.parseDouble(txtlatitude.getText().toString());
             double longi = Double.parseDouble(txtlongitude.getText().toString());

            getAddress(latt,longi);
        }

        toggleButtons();
        ///THE FUNCTION FOR UPDATING THE LOCATION IN DATABASE IS CALLED userLocationUpdate().....
        // userLocationUpdate();  //this is commented temporary

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }

    private void toggleButtons() {
        if (mRequestingLocationUpdates) {
            btnStartUpdates.setEnabled(false);
            btnStopUpdates.setEnabled(true);
        } else {
            btnStartUpdates.setEnabled(true);
            btnStopUpdates.setEnabled(false);
        }
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();


                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(AppointmentAction.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(AppointmentAction.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    @OnClick(R.id.btn_start_location_updates)
    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();

                        //Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @OnClick(R.id.btn_stop_location_updates)
    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                       // Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                        toggleButtons();
                    }
                });
    }

    @OnClick(R.id.btn_get_last_location)
    public void showLastKnownLocation() {
        if (mCurrentLocation != null) {
            Toast.makeText(getApplicationContext(), "Lat: " + mCurrentLocation.getLatitude()
                    + ", Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Last known location is not available!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();

        //userLocationUpdate(); /// THIS THE METHOD FOR SENDING THE LOCATION TO THE DATABASE......
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates

            stopLocationUpdates();
            // startLocationUpdates();
        }
    }

    ////// THIS BLOCK OF CODES FOR GPS LOCATION SERVICES..............................END...

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Cancel Appointment");
        dialogBuilder.setMessage("Enter Below the reason to why you want to cancel this Appointment");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();

                    //final String user_id = SharedPrefManager.getUserId().toString();
                    final String appointment_id = appoint_id; // Outlet Id is the appointment ID.
                    final String cancel_reson = edt.getText().toString().trim();

                    cancelProgress.show();

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            Constants.URL_CANCEL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    cancelProgress.dismiss();

                                    try {
                                        JSONObject obj =new JSONObject(response);
                                        if (!obj.getBoolean("error")){

                                            Toast.makeText(
                                                    AppointmentAction.this,
                                                    obj.getString("message"),
                                                    Toast.LENGTH_LONG
                                            ).show();

                                            Intent intent = new Intent(AppointmentAction.this, AppointmentsActivity.class);
                                            startActivity(intent);



                                        }else {
                                            Toast.makeText(
                                                    AppointmentAction.this,
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
                                    cancelProgress.dismiss();

                                    Toast.makeText(
                                            AppointmentAction.this,
                                            "Error Ocured try again",
                                            Toast.LENGTH_LONG
                                    ).show();

                                }
                            }

                    ){

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            //params.put("user_id", user_id);
                            params.put("appointment_id", appointment_id);
                            params.put("cancel_reson", cancel_reson);

                            return params;

                        }
                    };

                    RequestHandler.getInstance(AppointmentAction.this).addToRequestQueue(stringRequest);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }



    private void visitValidation(){
        final String userId = user_id;
        final String outletId = my_outlet;
        //final String token = SharedPrefManager.getInstance(act).getDeviceToken();


        //progressDialog.show();



        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_VISIT_VALIDATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();

                        try {
                            JSONObject obj =new JSONObject(response);
                            if (!obj.getBoolean("error")){

                                Toast.makeText(
                                        AppointmentAction.this,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();



                                startActivity(new Intent(AppointmentAction.this, ImageActivity.class));
                                AppointmentAction.this.finish();

//                                if(SharedPrefManager.getInstance(ImageActivity.this).getUserLastname().equalsIgnoreCase("56")){
//                                    startActivity(new Intent(ImageActivity.this, Coaching.class));
//                                    ImageActivity.this.finish();
//                                }else {
//                                    startActivity(new Intent(ImageActivity.this, QuestionsActivity.class));
//                                    ImageActivity.this.finish();
//
//                                }

                            }else {
                                Toast.makeText(
                                        AppointmentAction.this,
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
                        // progressDialog.dismiss();

                        Toast.makeText(
                                AppointmentAction.this,
                                "Error Ocured try again",
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("outlet_id", outletId);

                return params;

            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }








}
