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
    public static String c_password;
    public static String s_password;

    EditText name,lname,phone,email,address,house,password,cpassword;
    Button btnSubmit;

    private ProgressDialog pd, progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Sign Up");


        ///// initialise the views................
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        name =(EditText) findViewById(R.id.name);
        lname =(EditText) findViewById(R.id.lname);
        phone =(EditText) findViewById(R.id.phone);
        email =(EditText) findViewById(R.id.email);
        address =(EditText) findViewById(R.id.address);
        house =(EditText) findViewById(R.id.house);
        password =(EditText) findViewById(R.id.password);
        cpassword =(EditText) findViewById(R.id.cpassword);
        btnSubmit =(Button) findViewById(R.id.submit_button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if(name.getText().toString().trim().equalsIgnoreCase("") || phone.getText().toString().trim().equalsIgnoreCase("") || email.getText().toString().trim().equalsIgnoreCase("") ||
                            address.getText().toString().trim().equalsIgnoreCase("") || lname.getText().toString().trim().equalsIgnoreCase("") || password.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(Register.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    }else if(!password.getText().toString().trim().equals(cpassword.getText().toString().trim())){

                        Toast.makeText(Register.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();

                    }
                    else {
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
        final String l_name = lname.getText().toString().trim();
        final String e_phone = phone.getText().toString().trim();
        final String e_email = email.getText().toString().trim();
        final String e_address = address.getText().toString().trim();
        final String e_house = house.getText().toString().trim();
        final String e_password = password.getText().toString().trim();


        progressDialog.setMessage("Submitting please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_OBJECTIVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
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

                params.put("fname", e_name);
                params.put("lname", l_name);
                params.put("phone", e_phone);
                params.put("email", e_email);
                params.put("address", e_address);
                params.put("house", e_house);
                params.put("password", e_password);

                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);



    }

    public void onBackPressed(){
        //super.onBackPressed();

        startActivity(new Intent(Register.this, Login.class));
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
