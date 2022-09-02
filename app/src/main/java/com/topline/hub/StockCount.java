package com.topline.hub;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

public class StockCount extends AppCompatActivity {

    public static String user_id;
    public static String outlet_name;
    public static String admin_id;

    //a list to store all the Assets
    List<Asset_model> assets;

    //the recyclerview
    RecyclerView recyclerView;

    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_count);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Stock Count");



        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerStock);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar)findViewById(R.id.progressStock);
        btnSave = (Button)findViewById(R.id.btnSaveStock);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Stock Count Report ...");

        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        outlet_name = SharedPrefManager.getInstance(this).getOutletName();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();

        //initializing the Assets list
        assets = new ArrayList<>();

        loadAssets();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                final String userId = user_id;
                final String outlet = outlet_name ;
                final String adminId = SharedPrefManager.getInstance(StockCount.this).getUserUnit().trim();
                // now here we convert this list array into json string

                Gson gson=new Gson();

                final String newDataArray=gson.toJson(assets); // dataarray is list aaray

                // now we have json string lets send it to server using volly

                final String server_url="https://impulsepromotions.co.ke/bidco/mobile/v1/stock_count.php"; // url of server check this 100 times it must be working

                // volley

                StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {

                                progressDialog.dismiss();

                                final String result=response.toString();
                                Log.d("response", "result : "+result); //when response come i will log it
                                Toast.makeText(StockCount.this, "Stock Count Saved Successfully", Toast.LENGTH_LONG).show();

                                startActivity(new Intent(StockCount.this, QuestionsActivity.class));
                                StockCount.this.finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                progressDialog.dismiss();
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
                        param.put("admin_id",adminId);
                        //param.put("productCat_id",productCat_id);
                        //param.put("productCat_name",productCat_name);
                        //param.put("Qcategory",Qcategory);


                        return param;
                    }
                };
                //Vconnection.getnInstance(this).addRequestQue(stringRequest); // vConnection i claas which used to connect volley
                Volley.newRequestQueue(StockCount.this).add(stringRequest);



            }
        });
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

    private void loadAssets() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_STOCKS+admin_id,
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
                                JSONObject asset = array.getJSONObject(i);

                                //adding the Assets to Assets list
                                assets.add(new Asset_model(
                                        asset.getInt("id"),
                                        asset.getString("stock_count"),
                                        ""
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            AssetAdapter adapter = new AssetAdapter(StockCount.this, assets);
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
                        Toast.makeText(StockCount.this, "Error Loading Assets Try again", Toast.LENGTH_SHORT).show();

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
