package com.topline.hub;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;

public class Complete extends AppCompatActivity {

    public static String user_id;
    public static String admin_id;
    public static String outlet_name;

    public static String category_id;

    //a list to store all the products
    List<CompleteModel> cats;
    List<CompleteModel> cat1;
    private ArrayList<String> daysList = new ArrayList<>();

    //the recyclerview
    RecyclerView recyclerView, recyclerView1;

    private ProgressBar progressBar;
    Spinner spnMethod;
    EditText instruction, address;

    Button confirm, pay;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outlet_name = SharedPrefManager.getInstance(this).getOutletName();
        setTitle("Check Out");
        setContentView(R.layout.activity_complete);
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

        recyclerView1 = (RecyclerView) findViewById(R.id.recycler1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar)findViewById(R.id.progress);

        instruction = (EditText) findViewById(R.id.instruction);
        address = (EditText) findViewById(R.id.address);
        confirm = (Button) findViewById(R.id.confirm);
        spnMethod = (Spinner) findViewById(R.id.spnMethod);

        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(address.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(Complete.this, "Address is required", Toast.LENGTH_SHORT).show();
                    }else {
                        confirming();
                    }

                }

        });


        //initializing the productlist
        cats = new ArrayList<>();
        cat1 = new ArrayList<>();

        loadCategories();
        loadRequest();
//        fetchOutlet();
        loadPay(spnMethod,daysList);

    }

    private void loadPay(Spinner spinner, ArrayList<String> arrayList) {

        daysList.clear();
        daysList.add("Cash on Delivery");
        daysList.add("Mpesa on Delivery");
        daysList.add("Swipe on Delivery (+2.5% processing fee)");

        if(arrayList.size() > 0) {
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(Complete.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);
        }else{
            arrayList.add("Sync to get method");
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(Complete.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);

        }
    }


    private void loadCategories() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_COMPLETE + user_id,
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
                                cats.add(new CompleteModel(
                                        cat.getInt("id"),
                                        cat.getString("cat_id"),
                                        cat.getString("customer_name"),
                                        cat.getString("territory_id"),
                                        cat.getString("territory_name"),
                                        cat.getString("delivery_fee"),
                                        cat.getString("total")
                                        //cat.getString("catcolor_id")

                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            CompleteAdapter adapter = new CompleteAdapter(Complete.this, cats);
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
                        //Toast.makeText(Complete.this, "Error Loading Product Category Try again", Toast.LENGTH_SHORT).show();

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_COMPLETE + user_id,
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
                                cat1.add(new CompleteModel(
                                        cat.getInt("id"),
                                        cat.getString("cat_id"),
                                        cat.getString("customer_name"),
                                        cat.getString("territory_id"),
                                        cat.getString("territory_name"),
                                        cat.getString("delivery_fee"),
                                        cat.getString("total")
                                        //cat.getString("catcolor_id")

                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            RequestCash adapters = new RequestCash(Complete.this, cat1);
                            recyclerView1.setAdapter(adapters);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(Complete.this, "Error Loading Product Category Try again", Toast.LENGTH_SHORT).show();

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void onBackPressed(){
        //super.onBackPressed();

        startActivity(new Intent(Complete.this, Cart.class));
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

    private void confirming(){

        final String userId = SharedPrefManager.getInstance(this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(this).getUsername().trim();
        final String adminId = SharedPrefManager.getInstance(this).getUserUnit().trim();
        final String contact = SharedPrefManager.getInstance(this).getUserTelephone().trim();

        final String c_inst = instruction.getText().toString().trim();
        final String c_address = address.getText().toString().trim();
        final String c_method = spnMethod.getSelectedItem().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_COMPLETE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        startActivity(new Intent(Complete.this, MainActivity.class));
                        Complete.this.finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Complete.this, "Error Occurred Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("admin_id", adminId);
                params.put("user_contact", contact);
                params.put("instruction", c_inst);
                params.put("address", c_address);
                params.put("pay_method", c_method);

                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);



    }

    private void loadSpinner(Spinner spinner, ArrayList<String> arrayList) {
        if(arrayList.size() > 0) {
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(Complete.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);
        }else{
            arrayList.add("Sync to get outlets");
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(Complete.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);

        }
        //spinner.setOnItemSelectedListener(new changeBackground());

    }

}
