package com.topline.hub;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class ProductCat extends AppCompatActivity {

    public static String user_id;
    public static String admin_id;
    public static String outlet_name;

    public static String category_id;

    //a list to store all the products
    List<ProductCat_model> cats;

    //the recyclerview
    RecyclerView recyclerView;

    private ProgressBar progressBar;

    Button back_to_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outlet_name = SharedPrefManager.getInstance(this).getOutletName();
        setTitle("Shop by Category");
        setContentView(R.layout.activity_product_category);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RECEIVE INTENT
        Intent i = this.getIntent();
        //String shelf_id = i.getExtras().getString("SHELF_KEY");
        String shelf_id = "1";

        category_id = shelf_id;

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar)findViewById(R.id.progress);
        back_to_report = (Button) findViewById(R.id.back_to_report);


        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();


        //initializing the productlist
        cats = new ArrayList<>();

        loadCategories();



        back_to_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProductCat.this, MainActivity.class));
            }
        });



    }


    private void loadCategories() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_PRODUCT_CATEGORY,
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
                                cats.add(new ProductCat_model(
                                        cat.getInt("id"),
                                        cat.getString("cat_id"),
                                        cat.getString("cat_name")
                                        //cat.getString("catcolor_id")

                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            NewProductCatAdapter adapter = new NewProductCatAdapter(ProductCat.this, cats);
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
                        Toast.makeText(ProductCat.this, "Error Loading Product Category Try again", Toast.LENGTH_SHORT).show();

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

//    public void onBackPressed(){
//        //super.onBackPressed();
//
//        startActivity(new Intent(ProductCategoryActivity.this, QuestionsActivity.class));
//    }

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
