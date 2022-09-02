package com.topline.hub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class FocusBrandsActivity extends AppCompatActivity {

    public static String user_id;
    public static String outlet_name;
    public static String outlet_id;
    public static String category_id;
    public static String admin_id;

    //a list to store all the products
    List<FocusBrand_model> products;

    //the recyclerview
    RecyclerView recyclerView;
    LinearLayout ln;

    private ProgressBar progressBar;

    private ProgressDialog progressDialog;

    Button btnSave;


    TextView product_name;
    // RelativeLayout relativeLayout;
    CardView cardView;

    RadioGroup rg;
    RadioButton rdAvailable,rdNotAvailable,rdNotStocked,rdNotListed;

    TextView textView;

    CheckBox mCheckBox;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_brands);
//RECEIVE INTENT
        Intent i = this.getIntent();

      final String cat_id = i.getExtras().getString("CAT_ID");
      final String cat_name = i.getExtras().getString("CAT_NAME");

        setTitle(cat_name);

//getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ln = (LinearLayout) findViewById(R.id.lnFocusBrand);

        category_id = cat_id;

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar)findViewById(R.id.progress);
        btnSave = (Button)findViewById(R.id.btnSave);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait... Report is being sent");

        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        outlet_name = SharedPrefManager.getInstance(this).getOutletName();
        outlet_id = SharedPrefManager.getInstance(this).getOutletId().toString();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();


        //initializing the productlist
        products = new ArrayList<>();

        loadProducts();


        //mCheckBox.setText("Availability");




        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String userId = user_id;
                final String outlet = outlet_name ;
                final String outletId = outlet_id ;
                final String productCat_id = category_id;
                final String productCat_name = cat_name;
                final String Qcategory = "1";
                final String adminId = admin_id;
                // now here we convert this list array into json string

                Gson gson=new Gson();

                final String newDataArray=gson.toJson(products); // dataarray is list aaray

                // now we have json string lets send it to server using volly

                final String server_url="https://impulsepromotions.co.ke/bidco/mobile/v1/volly_array.php"; // url of server check this 100 times it must be working

                // volley
                progressDialog.show();

                StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {

                                progressDialog.dismiss();
                                final String result=response.toString();
                                Log.d("response", "result : "+result); //when response come i will log it

                                //Toast.makeText(FocusBrandsActivity.this, "Saved Successfully", Toast.LENGTH_LONG).show();
                                //startActivity(new Intent(FocusBrandsActivity.this, ProductCategoryActivity.class));
                                //FocusBrandsActivity.this.finish();
                                successDialog();

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
                        param.put("outlet_id",outletId);
                        param.put("productCat_id",productCat_id);
                        param.put("productCat_name",productCat_name);
                        param.put("Qcategory",Qcategory);
                        param.put("admin_id",adminId);

                        return param;
                    }
                };
                //Vconnection.getnInstance(this).addRequestQue(stringRequest); // vConnection i claas which used to connect volley
                Volley.newRequestQueue(FocusBrandsActivity.this).add(stringRequest);



            }
        });




    }

    private void loadRadioButtons(final FocusBrand_model obj){
        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.focusbrands_list_items,null);

        product_name = v.findViewById(R.id.focusbrand);
        cardView = v.findViewById(R.id.cardView);

        product_name.setText(obj.getProduct_name() + " "+ obj.getSku());
        //product_name.setText(obj.getProduct_name());

        rdAvailable = (RadioButton) v.findViewById(R.id.rdAvailable);
        rdNotAvailable = (RadioButton) v.findViewById(R.id.rdNotAvailable);
        rdNotListed = (RadioButton) v.findViewById(R.id.rbNotListed);
        rdNotStocked = (RadioButton) v.findViewById(R.id.rdNotStocked);

        rdNotStocked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    obj.setType("Not Stocked");
                    obj.setSelected(false);
                }
            }
        });
        rdNotListed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    obj.setType("Not Listed");
                    obj.setSelected(false);
                }
            }
        });
        rdAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    obj.setSelected(true);
                    obj.setType("Available");
                }
            }
        });
        rdNotAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    obj.setSelected(false);
                    obj.setType("Not Available");
                }
            }
        });

        ln.addView(v);
    }

    ///// This to try accessing adapters widget from the activity

  /*  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewNum);
            // mCheckBox = (CheckBox)itemView.findViewById(R.id.checkBoxAvail);
        }

        @Override
        public void onClick(View v) {

            textView.setText("Availability");

        }
    } */






    private void loadProducts() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_PRODUCTS+category_id,
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
                                JSONObject product = array.getJSONObject(i);

                                //adding the Products to Product list
                                FocusBrand_model obj = new FocusBrand_model(
                                        product.getInt("id"),
                                        product.getString("product_name"),
                                        product.getString("sku"),
                                        "",
                                        false


                                );
                                products.add(obj);
                                loadRadioButtons(obj);
                            }

                            //creating adapter object and setting it to recyclerview
//                            FocusBrandAdapter adapter = new FocusBrandAdapter(FocusBrandsActivity.this, products);
//                            recyclerView.setAdapter(adapter);
                            //recyclerView.getRecycledViewPool().setMaxRecycledViews(1,0);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FocusBrandsActivity.this, "Error Loading Product Try again", Toast.LENGTH_SHORT).show();

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


    public void successDialog(){
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                FocusBrandsActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Success !");

        // Setting Dialog Message
        alertDialog.setMessage("Availability Successfully Sent");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.tick_green);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

//                Intent intent = new Intent(FocusBrandsActivity.this, QuestionsActivity.class);
//                startActivity(intent);

                startActivity(new Intent(FocusBrandsActivity.this, ProductCategoryActivity.class));
                FocusBrandsActivity.this.finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}
