package com.topline.hub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class UserEdit extends AppCompatActivity {

    private ConnectionDetector cd;
    TextView outletName;
    EditText user_name,phone,new_password,confirm_password;
    Button updateProduct;
    public static String my_id;
    public static String my_password;

    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Product...");


        Intent i = this.getIntent();
        final String id = i.getExtras().getString("ID_KEY");
        final String user_Name = i.getExtras().getString("USER_NAME");
        final String user_phone = i.getExtras().getString("PHONE_KEY");


        my_id = id;



        updateProduct = (Button)findViewById(R.id.btnUpdate);
        user_name = (EditText) findViewById(R.id.user_name);
        phone = (EditText)findViewById(R.id.phone);
        new_password = (EditText)findViewById(R.id.new_password);
        confirm_password = (EditText)findViewById(R.id.confirm_password);

        ///// Set the values to the test

        user_name.setText(user_Name);
        phone.setText(user_phone);

        cd = new ConnectionDetector(UserEdit.this);

        cd = new ConnectionDetector(getApplicationContext());



        updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {

                    UpdateExpiryTracker();

                } else {
                    Toast.makeText(UserEdit.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
                //UpdateExpiryTracker();
            }
        });

        updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if(new_password.getText().toString().trim().equalsIgnoreCase("") || confirm_password.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(UserEdit.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    }else if(!new_password.getText().toString().trim().equals(confirm_password.getText().toString().trim())){

                        Toast.makeText(UserEdit.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        UpdateExpiryTracker();
                    }

                } else {
                    Toast.makeText(UserEdit.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    private void UpdateExpiryTracker(){

        final String this_id = my_id;
        final String this_new_password = new_password.getText().toString().trim();


        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_USER_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                UserEdit.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Success !");

                        // Setting Dialog Message
                        alertDialog.setMessage("Profile Changed Successfully");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.tick_green);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(UserEdit.this, User.class);
                                startActivity(intent);
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(UserEdit.this, "Error Occurred Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("p_id", this_id);
                params.put("password", this_new_password);

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

}
