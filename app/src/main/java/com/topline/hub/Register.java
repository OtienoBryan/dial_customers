package com.topline.hub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

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

    EditText name,phone,email,location,county,password;
    Button btnSubmit;

    private ProgressDialog pd, progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Register");


        ///// initialise the views................
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        name =(EditText) findViewById(R.id.name);
        phone =(EditText) findViewById(R.id.phone);
        email =(EditText) findViewById(R.id.email);
        location =(EditText) findViewById(R.id.location);
        county =(EditText) findViewById(R.id.county);
        password =(EditText) findViewById(R.id.password);
        btnSubmit =(Button) findViewById(R.id.submit_button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if(name.getText().toString().trim().equalsIgnoreCase("") || phone.getText().toString().trim().equalsIgnoreCase("") || email.getText().toString().trim().equalsIgnoreCase("") ||
                            location.getText().toString().trim().equalsIgnoreCase("") || county.getText().toString().trim().equalsIgnoreCase("") || password.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(Register.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    }else {
                        submitCompetitive();
                    }

                } else {
                    Toast.makeText(Register.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });


        cd = new ConnectionDetector(Register.this);

        cd = new ConnectionDetector(getApplicationContext());


    }




    private void submitCompetitive(){

        final String e_name = name.getText().toString().trim();
        final String e_phone = phone.getText().toString().trim();
        final String e_email = email.getText().toString().trim();
        final String e_location = location.getText().toString().trim();
        final String e_county = county.getText().toString().trim();
        final String e_password = password.getText().toString().trim();
        final String e_role = "Client";
        final String e_role_id = "56";


        progressDialog.setMessage("Submitting please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_OBJECTIVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(Register.this, "Registration Successfully Completed", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Register.this, Login.class));
                        Register.this.finish();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(Register.this, "Error occured Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("name", e_name);
                params.put("phone", e_phone);
                params.put("email", e_email);
                params.put("location", e_location);
                params.put("county", e_county);
                params.put("password", e_password);
                params.put("role_id", e_role_id);
                params.put("role", e_role);


                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);



    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
