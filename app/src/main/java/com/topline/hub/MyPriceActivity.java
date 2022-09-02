package com.topline.hub;

import android.app.ProgressDialog;
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

public class MyPriceActivity extends AppCompatActivity {

    private ConnectionDetector cd;
    public static String user_id;
    public static String admin_id;
    public static String user_name;
    public static String outlet_id;
    public static String outlet_name;

    public static String product_name;
    public static String product_code;

    TextView outlet, expiry_date,manu_date;
    EditText initialPrice,newPrice, changetype,comments;
    Button uploadReport;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_price);

        //RECEIVE INTENT
        Intent i = this.getIntent();
        String id = i.getExtras().getString("ID_KEY");
        product_name = i.getExtras().getString("PRODUCT_NAME_KEY");
        product_code = i.getExtras().getString("PRODUCT_CODE_KEY");

        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        outlet_name = SharedPrefManager.getInstance(this).getOutletName();
        outlet_id = SharedPrefManager.getInstance(this).getOutletId().toString();
        user_name = SharedPrefManager.getInstance(this).getUsername();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Location Please Wait...");

        uploadReport = (Button)findViewById(R.id.saveClient);

        outlet = (TextView)findViewById(R.id.appmisc);
        initialPrice = (EditText)findViewById(R.id.initialPrice);
        newPrice = (EditText)findViewById(R.id.newPrice);
        changetype = (EditText)findViewById(R.id.changeType);
        comments = (EditText)findViewById(R.id.comments);

        outlet.setText(product_name +"  In  "+ outlet_name);


        cd = new ConnectionDetector(MyPriceActivity.this);

        cd = new ConnectionDetector(getApplicationContext());




        uploadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    if(initialPrice.getText().toString().trim().equalsIgnoreCase("") || newPrice.getText().toString().trim().equalsIgnoreCase("") || changetype.getText().toString().trim().equalsIgnoreCase("") ||comments.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(MyPriceActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    }else {
                        submitExpiryTracker();
                    }

                } else {
                    Toast.makeText(MyPriceActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });







    }



    private void submitExpiryTracker(){

        final String e_initial = initialPrice.getText().toString().trim();
        final String e_new = newPrice.getText().toString().trim();
        final String e_change = changetype.getText().toString().trim();
        final String e_comment = comments.getText().toString().trim();
        final String e_product_name = product_name;
        final String e_product_code = product_code;

        final String userId = SharedPrefManager.getInstance(this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(this).getUsername().trim();
        final String outlet = SharedPrefManager.getInstance(this).getOutletName().trim();
        final String outlet_id = SharedPrefManager.getInstance(this).getOutletId().toString().trim();
        final String adminId = SharedPrefManager.getInstance(this).getUserUnit().trim();


        progressDialog.setMessage("Submitting Report please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_PRICE_WATCH_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(MyPriceActivity.this, "Report Submitted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MyPriceActivity.this, QuestionsActivity.class));
                        MyPriceActivity.this.finish();

//                        AlertDialog alertDialog = new AlertDialog.Builder(
//                                MyPriceActivity.this).create();
//
//                        // Setting Dialog Title
//                        alertDialog.setTitle("Success !");
//
//                        // Setting Dialog Message
//                        alertDialog.setMessage("Expiry Successfully Submitted");
//
//                        // Setting Icon to Dialog
//                        alertDialog.setIcon(R.drawable.tick_green);
//
//                        // Setting OK Button
//                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                Intent intent = new Intent(MyPriceActivity.this, PriceListActivity.class);
//                                startActivity(intent);
//                                MyPriceActivity.this.finish();
//                            }
//                        });

                        // Showing Alert Message
                        //alertDialog.show();

                        //Toast.makeText(CartActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(MyPriceActivity.this, "Error Occurred Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("outlet_name", outlet);
                params.put("outlet_id", outlet_id);
                params.put("admin_id", adminId);

                params.put("product_name", e_product_name);
                params.put("product_code", e_product_code);
                params.put("initial_price", e_initial);
                params.put("new_price", e_new);
                params.put("change_type", e_change);
                params.put("comment", e_comment);


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
