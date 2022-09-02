package com.topline.hub;

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

public class MySosProductActivity extends AppCompatActivity {

    private ConnectionDetector cd;
    public static String user_id;
    public static String admin_id;
    public static String user_name;
    public static String outlet_id;
    public static String outlet_name;

    public static String product_name;
    public static String product_code;
    public static String product_sku;

    TextView outlet;
    EditText batch_no,quantity,expiry_date,comments;
    Button uploadReport;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sos_product);

        //RECEIVE INTENT
        Intent i = this.getIntent();
        String id = i.getExtras().getString("ID_KEY");
        product_name = i.getExtras().getString("PRODUCT_NAME_KEY");
        product_code = i.getExtras().getString("PRODUCT_CODE_KEY");
        product_sku = i.getExtras().getString("PRODUCT_SKU");

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
        batch_no = (EditText)findViewById(R.id.batchNo);
        quantity = (EditText)findViewById(R.id.quantity);
        expiry_date = (EditText)findViewById(R.id.expiryDate);
        comments = (EditText)findViewById(R.id.comments);

        outlet.setText(product_name + " In " + outlet_name);


        cd = new ConnectionDetector(MySosProductActivity.this);

        cd = new ConnectionDetector(getApplicationContext());


//        expiry_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final Calendar myCalendar = Calendar.getInstance();
//
//                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                          int dayOfMonth) {
//                        // TODO Auto-generated method stub
//                        myCalendar.set(Calendar.YEAR, year);
//                        myCalendar.set(Calendar.MONTH, monthOfYear);
//                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                        String myFormat = "yyyy-MM-dd"; //In which you need put here
//                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
//
//                        expiry_date.setText(sdf.format(myCalendar.getTime()));
//
//                    }
//
//                };
//
//
//
//                new DatePickerDialog(MySosProductActivity.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//
//            }
//        });

        uploadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    if(batch_no.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(MySosProductActivity.this, "Field is required", Toast.LENGTH_SHORT).show();
                    }else {
                        submitExpiryTracker();
                    }

                } else {
                    Toast.makeText(MySosProductActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });







    }



    private void submitExpiryTracker(){

        final String e_quantity = quantity.getText().toString().trim();
        //final String e_expiry_date = expiry_date.getText().toString().trim();
        final String e_batch_no = batch_no.getText().toString().trim();
        final String e_comment = comments.getText().toString().trim();
        final String e_product_name = product_name;
        final String e_product_code = product_code;
        final String e_product_sku = product_sku;

        final String userId = SharedPrefManager.getInstance(this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(this).getUsername().trim();
        final String outlet = SharedPrefManager.getInstance(this).getOutletName().trim();
        final String outlet_id = SharedPrefManager.getInstance(this).getOutletId().toString().trim();
        final String adminId = SharedPrefManager.getInstance(this).getUserUnit().trim();


        progressDialog.setMessage("Submitting Report please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_SOSNEW_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        //Toast.makeText(CompetitiveActivity.this, "ACTIVITY SUCCESS", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(CompetitiveActivity.this, QuestionsActivity.class));
//                        CompetitiveActivity.this.finish();

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                MySosProductActivity.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Success !");

                        // Setting Dialog Message
                        alertDialog.setMessage("Sos Successfully Submitted");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.tick_green);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(MySosProductActivity.this, MySosListActivity.class);
                                startActivity(intent);
                                MySosProductActivity.this.finish();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                        //Toast.makeText(CartActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(MySosProductActivity.this, "Error Occurred Try again", Toast.LENGTH_LONG).show();
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
                params.put("product_sku", e_product_sku);
                params.put("product_code", e_product_code);
                params.put("batch_no", e_batch_no);
                params.put("quantity", e_quantity);
               // params.put("expiry_date", e_expiry_date);
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
