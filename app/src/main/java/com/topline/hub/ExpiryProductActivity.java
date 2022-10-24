package com.topline.hub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
    public static String product_abv;
    public static String product_sub;
    public static String product_brand;
    public static String product_country;
    public static String product_details;

    TextView outlet, p_name, p_price, p_description, sub_category, brand, country, abv, p_details;
    EditText batch_no,quantity,comments;
    Button uploadReport, notify;
    LinearLayout myqty;
    CardView cvWhatsApp, cvCall;
    ImageView image, stock, stockout, fav;
    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    List<ProductModel> cats;

    @SuppressLint("MissingInflatedId")
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
        product_abv = i.getExtras().getString("PRODUCT_ABV");
        product_sub = i.getExtras().getString("PRODUCT_SUB");
        product_brand = i.getExtras().getString("PRODUCT_BRAND");
        product_country = i.getExtras().getString("PRODUCT_COUNTRY");
        product_details = i.getExtras().getString("PRODUCT_DETAILS");


        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        user_name = SharedPrefManager.getInstance(this).getUsername();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        cats = new ArrayList<>();
        loadCategories();

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
        p_details = (TextView)findViewById(R.id.product_details);
        p_description = (TextView)findViewById(R.id.description);
        brand = (TextView)findViewById(R.id.brand);
        sub_category = (TextView)findViewById(R.id.sub_category);
        country = (TextView)findViewById(R.id.country);
        abv = (TextView)findViewById(R.id.abv);
        image = (ImageView) findViewById(R.id.image);
        stock = (ImageView) findViewById(R.id.stock);
        fav = (ImageView) findViewById(R.id.fav);
        stockout = (ImageView) findViewById(R.id.stockout);
        myqty = (LinearLayout) findViewById(R.id.myqty);
        cvWhatsApp = (CardView) findViewById(R.id.cvWhatsApp);
        cvCall = (CardView) findViewById(R.id.cvCall);

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
        sub_category.setText(product_sub);
        brand.setText(product_brand);
        country.setText(product_country);
        p_details.setText(product_details);
        abv.setText("ABV "+product_abv +"%");


        cd = new ConnectionDetector(ExpiryProductActivity.this);

        cd = new ConnectionDetector(getApplicationContext());

        cvWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+254 723 688108";
                String message = "Hello, I would like to order " + product_name + "@" + product_price;
                String url = "https://api.whatsapp.com/send?phone="+phone+ "&text=" + message;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));

                startActivity(i);

            }
        });

        cvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+254 723 688108";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));

                if (ActivityCompat.checkSelfPermission(ExpiryProductActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    //ActivityCompat.requestPermissions(context, new String[] { permission }, requestCode);

                    Toast.makeText(ExpiryProductActivity.this, "Enable call permission in APP Settings", Toast.LENGTH_SHORT).show();
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions

                    return;
                }
                startActivity(intent);

            }
        });



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

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    notify_me();
                } else {
                    Toast.makeText(ExpiryProductActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    favorite();
                } else {
                    Toast.makeText(ExpiryProductActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void loadCategories() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_RELATED + product_sub,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //progressBar.setVisibility(View.GONE);

                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject cat = array.getJSONObject(i);

                                //adding the Task to Task list
                                cats.add(new ProductModel(
                                        cat.getInt("id"),
                                        cat.getString("cat_id"),
                                        cat.getString("cat_name"),
                                        cat.getString("image"),
                                        cat.getString("price"),
                                        cat.getString("description"),
                                        cat.getString("usage"),
                                        cat.getString("status"),
                                        cat.getString("abv"),
                                        cat.getString("sub"),
                                        cat.getString("brand"),
                                        cat.getString("country"),
                                        cat.getString("details")
                                        //cat.getString("catcolor_id")

                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            ProductAdapter adapter = new ProductAdapter(ExpiryProductActivity.this, cats);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(ExpiryProductActivity.this, "Error Loading Product Category Try again", Toast.LENGTH_SHORT).show();

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
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
    private void notify_me(){

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
                    Constants.URL_POST_NOTIFY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            Toast.makeText(ExpiryProductActivity.this, "Request sent Successfully, You will be notified soon", Toast.LENGTH_LONG).show();
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

    private void favorite(){

        final String e_quantity = "1";
        final String e_product_name = product_name;
        final String e_product_code = product_code;
        final String e_product_price = product_price;
        final String e_product_image = product_image;

        final String userId = SharedPrefManager.getInstance(ExpiryProductActivity.this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(ExpiryProductActivity.this).getUsername().trim();
        final String adminId = SharedPrefManager.getInstance(ExpiryProductActivity.this).getUserUnit().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_POST_FAVORITE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(ExpiryProductActivity.this, "Added to Favourite List", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(Cart.this, Cart.class));
//                        Cart.this.finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

        RequestHandler.getInstance(ExpiryProductActivity.this).addToRequestQueue(stringRequest);



    }

    public void onBackPressed(){
        //super.onBackPressed();

        startActivity(new Intent(ExpiryProductActivity.this, MainActivity.class));
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
