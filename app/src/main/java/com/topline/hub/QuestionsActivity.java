package com.topline.hub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionsActivity extends AppCompatActivity {

    private ConnectionDetector cd;

    public static String currentTime;


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



    Button focusBrand,competitorActivity,assetsTracking,posmTracking,shortExpiry,priceTracker,shareOfShelf, checkout;
    CardView cvCheck,btnShare,btnOrders;
    Button cvAvailability, cvExpiry, cvCompe, cvSoS, cvPrice, cvQuality,cvMarket, cvPromotion, cvOut,cvBActivity;
    TextView check;
    TextView gpsAddress;
    private TextView txtlatitude,txtlongitude,currentDate, appointId,outId,battery_level;
    private ProgressDialog progressDialog, checkOutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        cvAvailability = (Button) findViewById(R.id.cvAvailability);
        cvExpiry = (Button) findViewById(R.id.cvExpiry);
        cvCompe = (Button) findViewById(R.id.cvCompe);
        cvSoS = (Button) findViewById(R.id.cvSoS);
        cvPrice = (Button) findViewById(R.id.cvPrice);
        cvQuality = (Button) findViewById(R.id.cvQuality);
        cvMarket = (Button) findViewById(R.id.cvMarket);
        cvPromotion = (Button) findViewById(R.id.cvPromotion);
        cvOut = (Button) findViewById(R.id.cvOut);
        cvBActivity = (Button) findViewById(R.id.cvBActivity);
        check= (TextView) findViewById(R.id.check);
        txtlatitude= (TextView)findViewById(R.id.mylocation_latitude);
        txtlongitude= (TextView)findViewById(R.id.mylocation_longitude);
        gpsAddress = (TextView)findViewById(R.id.mytxtAddress);
        cvCheck = (CardView) findViewById(R.id.cvCheck);


        /// GPS LOCATION BLOCK...........START
        ButterKnife.bind(this);
        // initialize the necessary libraries
        init();
        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
        /// GPS LOCATION BLOCK ...........END


       // checkout = (Button) findViewById(R.id.checkOut);

        checkOutProgress = new ProgressDialog(this);
        checkOutProgress.setMessage("Please Wait... You are being checked Out");
        //SharedPrefManager.getInstance(this).getOutletName();

        cd = new ConnectionDetector(QuestionsActivity.this);
        cd = new ConnectionDetector(getApplicationContext());
        currentTime = DateFormat.getTimeInstance().format(new Date());


        final Integer shelf_id = 1;


        cvAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openProductCatActivity(shelf_id.toString());

            }
        });


        cvCompe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(QuestionsActivity.this, CompetitiveActivity.class));
//                QuestionsActivity.this.finish();
                Intent intent = new Intent(QuestionsActivity.this, MyCompetitior.class);
                startActivity(intent);
            }
        });

        cvPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(QuestionsActivity.this, CompetitiveActivity.class));
//                QuestionsActivity.this.finish();
                Intent intent = new Intent(QuestionsActivity.this, PromotionActivity.class);
                startActivity(intent);
            }
        });

//        assetsTracking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(QuestionsActivity.this, AssetTrackerActivity.class);
//                startActivity(intent);
//            }
//        });

//        posmTracking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(QuestionsActivity.this, PosmAllocationActivity.class);
//                startActivity(intent);
//            }
//        });

        cvExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, ExpiryProductViewActivity.class);
                startActivity(intent);
            }
        });

        cvPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, PriceViewActivity.class);
                startActivity(intent);
            }
        });

        cvQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, QualityViewActivity.class);
                startActivity(intent);
            }
        });

        cvMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, MarketViewActivity.class);
                startActivity(intent);
            }
        });

        cvOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, StockOut.class);
                startActivity(intent);
            }
        });

        cvBActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivity.this, BidcoActivity.class);
                startActivity(intent);
            }
        });


//        shareOfShelf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(QuestionsActivity.this, MySos.class);
//                startActivity(intent);
//            }
//        });


        //checkout.setText(SharedPrefManager.getInstance(this).getAppointId()+" "+SharedPrefManager.getInstance(this).getOutletId().toString());


        cvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {


                    checkoutDialog();


                } else {
                    Toast.makeText(QuestionsActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }


//                Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });


        cvSoS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionsActivity.this, MySos.class);
                startActivity(intent);
            }
        });

//        btnShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(QuestionsActivity.this, ShareOfVisibility.class);
//                startActivity(intent);
//            }
//        });
//        btnOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(QuestionsActivity.this, OrderAndSales.class);
//                startActivity(intent);
//            }
//        });

    }

    private void openProductCatActivity(String shelf_id){

        Intent i = new Intent(this, ProductCategoryActivity.class);

        //PACK DATA
        i.putExtra("SHELF_KEY", shelf_id);


        this.startActivity(i);

    }


    public void onBackPressed(){
        //super.onBackPressed();

        //checkoutDialog();
        //finish();
        Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
        startActivity(intent);
    }




    private void checkout(){
        currentTime = DateFormat.getTimeInstance().format(new Date());

        SharedPrefManager.getInstance(this);
        final String user_id = SharedPrefManager.getUserId().toString();
        final String appoint_id = SharedPrefManager.getInstance(this).getAppointId().trim(); // the appointment ID.
        final String outlet_address = gpsAddress.getText().toString().trim();
        final String latitude = txtlatitude.getText().toString().trim();
        final String longitude = txtlongitude.getText().toString().trim();
        //progressDialog.show();
        final String current_time = currentTime;


        checkOutProgress.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        checkOutProgress.dismiss();

                        try {
                            JSONObject obj =new JSONObject(response);
                            if (!obj.getBoolean("error")){

                                Toast.makeText(
                                        QuestionsActivity.this,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();

                                Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
                                startActivity(intent);



                            }else {
                                Toast.makeText(
                                        QuestionsActivity.this,
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
                        checkOutProgress.dismiss();

                        Toast.makeText(
                                QuestionsActivity.this,
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
                params.put("appoint_id", appoint_id);
                params.put("currentTime", current_time);
                params.put("outlet_address", outlet_address);
                params.put("latitude", latitude);
                params.put("longitude", longitude);

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
                                    rae.startResolutionForResult(QuestionsActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(QuestionsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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





    public void checkoutDialog(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final String outlet_address = gpsAddress.getText().toString().trim();
        alertDialogBuilder.setMessage("You are about to checkout from" + "\n" + SharedPrefManager.getInstance(this).getOutletName());
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

//                        startActivity(new Intent(QuestionsActivity.this, MainActivity.class));
                        if(outlet_address.equals("Address")){
                            Toast.makeText(getApplicationContext(), "Geo_Locate to Checkout", Toast.LENGTH_LONG).show();


                        }else{
                            checkout();

                        }



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



}
