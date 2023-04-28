package com.topline.hub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

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


    // bunch of location related apis

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    private ApiInterface apiInterface;

    //// BLOCK CODE FOR GPS LOCATION SERVICES..................END



    TextView gpsAddress;

    ArrayList<String> accountName = new ArrayList<>();
    ArrayList<String> accountId = new ArrayList<>();
    Button btnAppointments, btnCollect, btnOutlet;
    AlertDialog showLoginDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(SharedPrefManager.getInstance(this).getUsername() + " " + SharedPrefManager.getInstance(this).getUserLastname());
        //setGravity(Gravity.CENTER);
        setContentView(R.layout.activity_main);




        ////////////////////////////The function for checking the Network connectivity is Called.............START../////////////////////////
        if (!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else {

        }

        ////////////////////////////The function for checking the Network connectivity is Called.............END../////////////////////////

//        /// For Alarm Notification.....Start 
//        Calendar calendar = Calendar.getInstance(); 
//        calendar.set(Calendar.HOUR_OF_DAY,20); 
//        calendar.set(Calendar.MINUTE,5); 
//        //calendar.set(Calendar.SECOND,10); 
//        Intent intent = new Intent(getApplicationContext(),NotificationReciever.class); 
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent, PendingIntent.FLAG_UPDATE_CURRENT); 
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); 
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);  
//        // / For Alarm Notification....End



        reccuringUpdate(); ////SEND THE RECCURING REQUEST TO UPDATE THE APPOINTMENT WHERE DAY IS NOT EQUAL TO TODAY


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
//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        mRequestingLocationUpdates = true;
//                        //startLocationUpdates();
//
//                       // Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();
//
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        if (response.isPermanentlyDenied()) {
//                            // open device settings when the permission is
//                            // denied permanently
//                            openSettings();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();




        ///CHECK IF THE USER IS LOGGED IN
//        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
//            finish();
//            startActivity(new Intent(this, Login.class));
//        }


        btnAppointments = (Button) findViewById(R.id.btnAppointments);
        btnCollect = (Button) findViewById(R.id.btnCollect);
        btnOutlet = (Button) findViewById(R.id.btnOutlet);


        btnAppointments.setBackgroundColor(getResources().getColor(R.color.gray1));
        btnAppointments.setTextColor(getResources().getColor(R.color.colorPrimaryDark));




        btnAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new AppointmentsFragment());

                btnAppointments.setBackgroundColor(getResources().getColor(R.color.gray1));
                btnAppointments.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                btnCollect.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnCollect.setTextColor(getResources().getColor(R.color.white));

                btnOutlet.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnOutlet.setTextColor(getResources().getColor(R.color.white));




            }
        });


        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new CollectFragment());

                btnCollect.setBackgroundColor(getResources().getColor(R.color.gray1));
                btnCollect.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                btnAppointments.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnAppointments.setTextColor(getResources().getColor(R.color.white));

                btnOutlet.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnOutlet.setTextColor(getResources().getColor(R.color.white));

            }
        });

        btnOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new OutletFragment());

                btnOutlet.setBackgroundColor(getResources().getColor(R.color.gray1));
                btnOutlet.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                btnAppointments.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnAppointments.setTextColor(getResources().getColor(R.color.white));

                btnCollect.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnCollect.setTextColor(getResources().getColor(R.color.white));

            }
        });

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        setFragment(new AppointmentsFragment());

        //myBundle();



    }

//    private void issueTracker(){
//        final View loginView = getLayoutInflater().inflate(R.layout.issue_tracker_model, null);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setView(loginView);
//        final EditText edtIssue = (EditText) loginView.findViewById(R.id.edtTrackerIssue);
//        final EditText edtName = (EditText) loginView.findViewById(R.id.edtTrackerName);
//        final EditText edtEmail = (EditText) loginView.findViewById(R.id.edtTrackerEmail);
//        final Spinner spnIssue = (Spinner) loginView.findViewById(R.id.spnTrackerAccount);
//        final Button btnSubmit = (Button) loginView.findViewById(R.id.btnIssueSend);
//        Button btnCancel = (Button) loginView.findViewById(R.id.btnIssueCancel);
//        final boolean[] result = {false};
//        ArrayList<String> issues = new ArrayList<>();
//        issues.clear();
//        issues.add("Expiry");
//        issues.add("Damages");
//        issues.add("Quality");
//        issues.add("Placement");
//        issues.add("POSM Usage");
//        issues.add("Order Placement & Delivery");
//        issues.add("Share of Shelf");
//        issues.add("Listings request");
//        issues.add("Asset usage & Status");
//        ArrayAdapter<String> spnAdapter = new ArrayAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, issues);
//        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnIssue.setAdapter(spnAdapter);
//
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!edtIssue.getText().toString().equals("") || !edtName.getText().toString().equals("") || !edtEmail.getText().toString().equals("")) {
//                    btnSubmit.setText("Submitting ...");
//                    btnSubmit.setClickable(false);
//                    apiInterface.postIssue(""+SharedPrefManager.getUserId(),
//                            SharedPrefManager.getInstance(MainActivity.this).getUsername() + " " + SharedPrefManager.getInstance(MainActivity.this).getUserLastname(),
//                            SharedPrefManager.getInstance(MainActivity.this).getUserTelephone(),
//                            SharedPrefManager.getInstance(MainActivity.this).getUserAccount(),
//                            edtIssue.getText().toString(),
//                            edtName.getText().toString(),
//                            edtEmail.getText().toString(),
//                            edtIssue.getText().toString()).enqueue(new Callback<JsonObject>() {
//                        @Override
//                        public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                            btnSubmit.setText("Submit");
//                            btnSubmit.setClickable(true);
//                            if (response.body().equals(null)) {
//                                Toast.makeText(MainActivity.this, "Error! Check your internet connection", Toast.LENGTH_SHORT).show();
//                            } else {
//                                if (response.body().get("status").getAsBoolean()) {
//                                    showLoginDialog.dismiss();
//                                    Toast.makeText(MainActivity.this, response.body().get("message").getAsString(), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(MainActivity.this, "Error! " + response.body().get("message").getAsString(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                            t.printStackTrace();
//                            btnSubmit.setText("Submit");
//                            btnSubmit.setClickable(true);
//                            Toast.makeText(MainActivity.this, "Error! Try again", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(MainActivity.this, "The issue comment, recipient name and email address are required", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showLoginDialog.dismiss();
//            }
//        });
//
//        try {
//
//            showLoginDialog = builder.create();
//            showLoginDialog.show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            final ViewGroup parentViewGroup = (ViewGroup) loginView.getParent();
//            parentViewGroup.removeView(loginView);
//            showLoginDialog = builder.create();
//            showLoginDialog.show();
//        }
//
//    }


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
        }

        toggleButtons();
        ///THE FUNCTION FOR UPDATING THE LOCATION IN DATABASE IS CALLED userLocationUpdate().....
        // userLocationUpdate();  //this is commmented temporary


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
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
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



////// PASS THE LATITUDE AND LONGITUDE TO THE FRAGMENT TO BE USED BY THE FRAGMENT.....

    public void setFragment(Fragment f){


        //PACK DATA IN A BUNDLE
        Bundle bundle = new Bundle();
        bundle.putString("LATITUDE_KEY", txtLocationResult.getText().toString());
        bundle.putString("LONGITUDE_KEY", txtLocationLongitude.getText().toString());

        //PASS OVER THE BUNDLE TO OUR FRAGMENT

        f.setArguments(bundle);


        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction ft = fManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.frame, f);
        ft.commit();


    }







    ////////////////////////////The function for checking the Network connectivity.............START../////////////////////////

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }


    public androidx.appcompat.app.AlertDialog.Builder buildDialog(Context c) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You are not online. Please check your Internet");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }




/////////////////////////// The function for checking the Network connectivity...........CLOSE./////////////////////////

    private void reccuringUpdate(){
        //currentTime = DateFormat.getTimeInstance().format(new Date());

        SharedPrefManager.getInstance(this);
        final String user_id = SharedPrefManager.getUserId().toString();
        //final String appoint_id = SharedPrefManager.getInstance(this).getAppointId().trim(); // the appointment ID.
        //progressDialog.show();
        //final String current_time = currentTime;


        //checkOutProgress.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_RECURRING_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj =new JSONObject(response);
                            if (!obj.getBoolean("error")){

                               /* Toast.makeText(
                                        MainActivity.this,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show(); */

//                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                                startActivity(intent);



                            }else {
                                /*Toast.makeText(
                                        MainActivity.this,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show(); */
                            }


                        }catch (JSONException e){
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(
                                MainActivity.this,
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
                //params.put("appoint_id", appoint_id);
                //params.put("currentTime", current_time);

                return params;

            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {

            logout();

            return true;
        }else if(id == R.id.ses){
            startActivity(new Intent(MainActivity.this, ExpiryProductListActivity.class));
            return  true;
        }else if(id == R.id.cart){
            startActivity(new Intent(MainActivity.this, Cart.class));
            return  true;
        }else if(id == R.id.orders){
            startActivity(new Intent(MainActivity.this, AppointmentsActivity.class));
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void logout(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Logout Dial A Drink!");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Toast.makeText(MainActivity.this,"You clicked yes ",Toast.LENGTH_LONG).show();
                        SharedPrefManager.getInstance(MainActivity.this).logout();
                        finish();
                        startActivity(new Intent(MainActivity.this, Login.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }





    public void exitApp(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You are about to exit the App!..");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        System.exit(0);

                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


//    public void onBackPressed(){
//
//        logout();
//
//    }

    public void alarm(){

        //        /// For Alarm Notification.....Start 
//        Calendar calendar = Calendar.getInstance(); 
//        calendar.set(Calendar.HOUR_OF_DAY,20); 
//        calendar.set(Calendar.MINUTE,5); 
//        //calendar.set(Calendar.SECOND,10); 
//        Intent intent = new Intent(getApplicationContext(),NotificationReciever.class); 
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent, PendingIntent.FLAG_UPDATE_CURRENT); 
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE); 
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);  
//        // / For Alarm Notification....End

    }





}
