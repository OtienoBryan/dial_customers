package com.topline.hub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewOutletActivity extends AppCompatActivity {

    private EditText editTextName, editTextKeyaccount;
    private Button buttonRegister;
    private Spinner region,channel,classification;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_outlet);

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editTextName = (EditText)findViewById(R.id.editTextOutlet);
        editTextKeyaccount = (EditText)findViewById(R.id.editTextKeyaccount);
        region = (Spinner)findViewById(R.id.spRegion);
        channel = (Spinner)findViewById(R.id.spChannel);
        classification = (Spinner)findViewById(R.id.spClassification);

        buttonRegister = (Button)findViewById(R.id.btnRegister);




    }


    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String keyaccount = editTextKeyaccount.getText().toString().trim();
        final String o_region = region.getSelectedItem().toString();
        final String o_channel = channel.getSelectedItem().toString();
        final String o_classification = classification.getSelectedItem().toString();


        progressDialog.setMessage("Registering the Outlet...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_NEW_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //Toast.makeText(act, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            //setFragment(new NewUserFragment());


                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    NewOutletActivity.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Success !");

                            // Setting Dialog Message
                            alertDialog.setMessage(jsonObject.getString("message"));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.tick_green);

                            // Setting OK Button
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //Intent intent = new Intent(act, OrderHistory.class);
                                    //startActivity(intent);
                                    // setFragment(new NewUserFragment());
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();







                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(NewOutletActivity.this, "Error Occured Try again ", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("name", name);
                params.put("keyAccount", keyaccount);
                params.put("region", o_region);
                params.put("channel", o_channel);
                params.put("classification", o_classification);


                return params;
            }
        };


        RequestHandler.getInstance(NewOutletActivity.this).addToRequestQueue(stringRequest);


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
