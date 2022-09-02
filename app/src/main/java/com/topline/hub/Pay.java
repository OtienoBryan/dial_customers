package com.topline.hub;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
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

public class Pay extends AppCompatActivity {

    public static String user_id;
    public static String payment;
    //a list to store all the products
    List<PayModel> outletList;
    //the recyclerview
    RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RECEIVE INTENT
        Intent i = this.getIntent();
        final String id = i.getExtras().getString("ID_KEY");
        setTitle("My Payments :#" +id);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar)findViewById(R.id.progress);

        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(act));

        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        payment = id;

        //initializing the productlist
        outletList = new ArrayList<>();

        loadOutlets();


    }

    private void loadOutlets() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_PAY+payment,
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
                                JSONObject outlet = array.getJSONObject(i);

                                //adding the Task to Task list
                                outletList.add(new PayModel(
                                        outlet.getInt("id"),
                                        outlet.getString("outlet_id"),
                                        outlet.getString("outlet_name"),
                                        outlet.getString("time"),
                                        outlet.getString("amount"),
                                        outlet.getString("balance")



                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            PayAdapter adapter = new PayAdapter(Pay.this, outletList);
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
                        Toast.makeText(Pay.this, "Error Loading Outlets Try again", Toast.LENGTH_SHORT).show();

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
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
