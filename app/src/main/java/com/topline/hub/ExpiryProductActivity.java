package com.topline.hub;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ExpiryProductActivity extends AppCompatActivity {

    private ConnectionDetector cd;
    public static String user_id;
    public static String admin_id;
    public static String user_name;
    public static String outlet_id;
    public static String outlet_name;

    public static String product_name;
    public static String product_code;
    public static String product_image;
    public static String product_price;
    public static String product_description;
    public static String product_usage;
    public static String product_status;

    TextView outlet, expiry_date,manu_date, p_name, p_price, p_description, p_usage, p_status;
    EditText batch_no,quantity,comments;
    Button uploadReport, notify;
    LinearLayout myqty;
    ImageView image, stock, stockout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiry_product);



        //RECEIVE INTENT
        Intent i = this.getIntent();
        String id = i.getExtras().getString("ID_KEY");
        product_name = i.getExtras().getString("PRODUCT_NAME_KEY");
        product_code = i.getExtras().getString("ID_KEY");
        product_image = i.getExtras().getString("PRODUCT_IMAGE");
        product_price = i.getExtras().getString("PRODUCT_PRICE");
        product_description = i.getExtras().getString("PRODUCT_DESC");
        product_usage = i.getExtras().getString("PRODUCT_USAGE");
        product_status = i.getExtras().getString("PRODUCT_STATUS");

        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        user_name = SharedPrefManager.getInstance(this).getUsername();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();

        setTitle(product_name);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Location Please Wait...");

        uploadReport = (Button)findViewById(R.id.saveClient);
        notify = (Button)findViewById(R.id.notify);

        outlet = (TextView)findViewById(R.id.appmisc);
        batch_no = (EditText)findViewById(R.id.batchNo);
        quantity = (EditText)findViewById(R.id.quantity);
        p_name = (TextView)findViewById(R.id.product_name);
        p_price = (TextView)findViewById(R.id.product_price);
        p_description = (TextView)findViewById(R.id.description);
        //p_usage = (TextView)findViewById(R.id.usage);
        image = (ImageView) findViewById(R.id.image);
        stock = (ImageView) findViewById(R.id.stock);
        stockout = (ImageView) findViewById(R.id.stockout);
        myqty = (LinearLayout) findViewById(R.id.myqty);

        int p_status = Integer.parseInt(product_status);

        if(p_status == 1){
            stock.setVisibility(View.VISIBLE);
            notify.setVisibility(View.GONE);
            stockout.setVisibility(View.GONE);
            uploadReport.setVisibility(View.VISIBLE);
            myqty.setVisibility(View.VISIBLE);
        }else if(p_status == 0){
            stock.setVisibility(View.GONE);
            stockout.setVisibility(View.VISIBLE);
            uploadReport.setVisibility(View.GONE);
            myqty.setVisibility(View.GONE);
            notify.setVisibility(View.VISIBLE);

        }

        Glide.with(this).load(product_image).into(image);
        p_name.setText(product_name);
        p_price.setText("Ksh. "+product_price);
        p_description.setText(product_description);
        //p_usage.setText(product_usage);


        cd = new ConnectionDetector(ExpiryProductActivity.this);

        cd = new ConnectionDetector(getApplicationContext());



        uploadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    if(quantity.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(ExpiryProductActivity.this, "Quantity is required", Toast.LENGTH_SHORT).show();
                    }else {
                        submitExpiryTracker();
                    }

                } else {
                    Toast.makeText(ExpiryProductActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    private void submitExpiryTracker(){

        final String e_quantity = quantity.getText().toString().trim();
        final String e_product_name = product_name;
        final String e_product_code = product_code;
        final String e_product_price = product_price;
        final String e_product_image = product_image;

        final String userId = SharedPrefManager.getInstance(this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(this).getUsername().trim();
        final String adminId = SharedPrefManager.getInstance(this).getUserUnit().trim();


        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_POST_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(ExpiryProductActivity.this, "Item Added to Cart", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ExpiryProductActivity.this, MainActivity.class));
                        ExpiryProductActivity.this.finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(ExpiryProductActivity.this, "Error Occurred Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("admin_id", adminId);

                params.put("product_name", e_product_name);
                params.put("product_code", e_product_code);
                params.put("product_price", e_product_price);
                params.put("product_image", e_product_image);
                params.put("quantity", e_quantity);

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
