package com.topline.hub;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MpaActivity extends AppCompatActivity {


    public static String user_id;
    public static String outlet_name;
    public static String category_id;

    List<FocusBrand_model> products;
    RecyclerView recyclerView;
    TextView txtFound;

    private ProgressBar progressBar;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpa);
        getSupportActionBar().setTitle("MPA Availability");

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMpa);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar)findViewById(R.id.progressMpa);
        btnSave = (Button)findViewById(R.id.btnMpaSave);

        txtFound = (TextView) findViewById(R.id.txtMpaAvailabilityFound);


        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        outlet_name = SharedPrefManager.getInstance(this).getOutletName();

        //initializing the productlist
        products = new ArrayList<>();

        loadProducts();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMpa();
            }
        });
    }

    private void loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_MPA_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            if(array.length() > 0) {
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);

                                    //adding the Products to Product list
                                    FocusBrand_model obj = new FocusBrand_model(
                                            product.getInt("id"),
                                            product.getString("product_name"),
                                            product.getString("sku"),
                                            "",
                                            false


                                    );
                                }

                                //creating adapter object and setting it to recyclerview
                                FocusBrandAdapter adapter = new FocusBrandAdapter(MpaActivity.this, products);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setVisibility(View.VISIBLE);
                                txtFound.setVisibility(View.GONE);
                            }else{
                                recyclerView.setVisibility(View.GONE);
                                txtFound.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MpaActivity.this, "Error Loading Product Try again", Toast.LENGTH_SHORT).show();

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void submitMpa(){
        final String userId = user_id;
        final String outlet = outlet_name ;
        Gson gson=new Gson();
        final String newDataArray=gson.toJson(products); // dataarray is list aaray

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_MPA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        final String result=response.toString();
                        //Log.d("response", "result : "+result); //when response come i will log it
                        Toast.makeText(MpaActivity.this, "Saved Successfully", Toast.LENGTH_LONG).show();
                        SharedPrefManager.getInstance(MpaActivity.this).doneMPA();
                        MpaActivity.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        error.getMessage(); // when error come i will log it
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<String, String>();
                param.put("array",newDataArray); // array is key which we will use on server side
                param.put("user_id",userId);
                param.put("outlet",outlet);


                return param;
            }
        };
        Volley.newRequestQueue(MpaActivity.this).add(stringRequest);
    }

}
