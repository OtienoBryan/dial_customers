package com.topline.hub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BidcoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //// for Images
    //Button btnCamera;
    private ImageView ivImage;
    private ConnectionDetector cd;
    private Boolean upflag = false;
    private Uri selectedImage = null;
    private Bitmap bitmap, bitmapRotate;
    private ProgressDialog pDialog;
    String imagepath = "";
    String fname;
    File file;
    //// for Images.........END

    public static String admin_id;

    private int hours,minute, mYear, mMonth, mDay;


    Spinner productCategory, competitorBrand,promotion,effectSales,spnBrand;
    EditText notes,quantity,edtRrp,edtPrice,activity,space,price, edtPriceFrom,edtPriceTo;
    Button btnSubmit;
    ImageView selected_image_preview;
    TextView start_date,end_date;
    LinearLayout select_start_date, select_end_date, select_photo;
    String my_id;

    private ProgressDialog pd, progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidco);

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///// initialise the views................
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        //RECEIVE INTENT
        Intent i = this.getIntent();
        final String id = i.getExtras().getString("ID_KEY");

        setTitle(" # " +id);
        my_id = id;


        notes = (EditText) findViewById(R.id.notes);


        btnSubmit =(Button) findViewById(R.id.submit_button);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    if(notes.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(BidcoActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    }else {
                        submitCompetitive();
                    }

                } else {
                    Toast.makeText(BidcoActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });




        cd = new ConnectionDetector(BidcoActivity.this);

        cd = new ConnectionDetector(getApplicationContext());




    }




    private void submitCompetitive(){


        final String c_note = notes.getText().toString().trim();
        final String thy_id = my_id;

        final String userId = SharedPrefManager.getInstance(this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(this).getUsername().trim();
        final String adminId = SharedPrefManager.getInstance(this).getUserUnit().trim();


        progressDialog.setMessage("Submitting please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CLIENT_INQUIRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(BidcoActivity.this, "Submission Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(BidcoActivity.this, MyRequests.class));
                        BidcoActivity.this.finish();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(BidcoActivity.this, "Error occurred Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("admin_id", adminId);
                params.put("note", c_note);
                params.put("request_id", thy_id);

                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);



    }

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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

    }
}
