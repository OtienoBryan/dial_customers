package com.topline.hub;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WineTaste extends AppCompatActivity {

    public static String user_id;
    public static String admin_id;
    public static String outlet_name;

    public static String category_id;
    public static String cat_id;
    public static String cat_name;
    LinearLayout lnCat;

    //a list to store all the products
    List<ProductModel> cats;

    //the recyclerview
    RecyclerView recyclerView;

    private ProgressBar progressBar;
    private final int MY_PERMISSIONS_CALL= 7;

    Button back_to_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outlet_name = SharedPrefManager.getInstance(this).getOutletName();

        setContentView(R.layout.activity_wine_taste);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RECEIVE INTENT
        Intent i = this.getIntent();
        String cat_id = i.getExtras().getString("CAT_ID");
        String cat_name = i.getExtras().getString("CAT_NAME");
        String shelf_id = "1";

        setTitle(cat_name);

        category_id = cat_id;

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        progressBar = (ProgressBar)findViewById(R.id.progress);
        back_to_report = (Button) findViewById(R.id.back_to_report);
        lnCat = (LinearLayout) findViewById(R.id.lnCat);


        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();


        //initializing the productlist
        cats = new ArrayList<>();

        loadCategories();

        if(category_id.equals("17")){
            lnCat.setVisibility(View.VISIBLE);
        }

        if (ContextCompat.checkSelfPermission(WineTaste.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) WineTaste.this,
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions((Activity) WineTaste.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_CALL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }



        back_to_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WineTaste.this, MainActivity.class));
            }
        });

        lnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WineTaste.this, WineCategory.class));
            }
        });

    }


    private void loadCategories() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_WINE_TASTE+category_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

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
                            ProductAdapter adapter = new ProductAdapter(WineTaste.this, cats);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(WineTaste.this, "Error Loading Product Category Try again", Toast.LENGTH_SHORT).show();

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void onBackPressed(){
        super.onBackPressed();

        //startActivity(new Intent(ProductCategoryActivity.this, QuestionsActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topping, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ses) {
            startActivity(new Intent(WineTaste.this, ExpiryProductListActivity.class));

            return true;
        }else if(id == R.id.call){
            call();
            return  true;
        }else if(id == R.id.orders){
            //startActivity(new Intent(MainActivity.this, AppointmentsActivity.class));
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void call(){

        String phone = "+254 723 688108";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));

        if (ActivityCompat.checkSelfPermission(WineTaste.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            //ActivityCompat.requestPermissions(context, new String[] { permission }, requestCode);

            Toast.makeText(WineTaste.this, "Enable call permission in APP Settings", Toast.LENGTH_SHORT).show();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        startActivity(intent);


    }


}
