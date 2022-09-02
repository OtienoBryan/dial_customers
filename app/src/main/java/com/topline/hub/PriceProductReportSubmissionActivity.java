package com.topline.hub;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;

public class PriceProductReportSubmissionActivity extends AppCompatActivity {

    private ConnectionDetector cd;
    public static String user_id;
    public static String user_name;
    public static String outlet_id;
    public static String outlet_name;
    public static String admin_id;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<PriceProductCompetitor_model> productCompetitor;
    private PriceProductCompetitorAdapter adapter;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;

    TextView status;
    Button submitPrice_report;

    private EditText ourProduct,notes;
    public static String userId, competId, product_name, product_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_product_report_submission);
        //RECEIVE INTENT
        Intent i = this.getIntent();
        competId = i.getExtras().getString("ID_KEY");
        product_name = i.getExtras().getString("PRODUCT_NAME_KEY");
        product_code = i.getExtras().getString("PRODUCT_CODE_KEY");
        setTitle(product_name);

        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        outlet_name = SharedPrefManager.getInstance(this).getOutletName();
        outlet_id = SharedPrefManager.getInstance(this).getOutletId().toString();
        user_name = SharedPrefManager.getInstance(this).getUsername();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar)findViewById(R.id.progress);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        userId = SharedPrefManager.getInstance(this).getUserId().toString();

        status = (TextView)findViewById(R.id.status);
        submitPrice_report = (Button)findViewById(R.id.submitProduct_priceReport);

        ourProduct = (EditText) findViewById(R.id.ourProduct);
        notes = (EditText) findViewById(R.id.notes);
        status.setText("Submitting for Product "+product_name+" at "+outlet_name);
        ourProduct.setHint(product_name);


        productCompetitor = new ArrayList<>();


      fetchCompetitorProducts(competId);


      submitPrice_report.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if( TextUtils.isEmpty(ourProduct.getText())){

                  ourProduct.setError( "This field is required!" );

              }else if(TextUtils.isEmpty(notes.getText())){

                  notes.setError( "This field is required!" );

              }else{

                  sendPrice();
              }



          }
      });






    }


    public void fetchCompetitorProducts(String competId){

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<List<PriceProductCompetitor_model>> call = apiInterface.getCompetitorProducts(competId);

        call.enqueue(new Callback<List<PriceProductCompetitor_model>>() {
            @Override
            public void onResponse(retrofit2.Call<List<PriceProductCompetitor_model>> call, Response<List<PriceProductCompetitor_model>> response) {

                progressBar.setVisibility(View.GONE);

                productCompetitor = response.body();
                adapter = new
                        PriceProductCompetitorAdapter(productCompetitor, PriceProductReportSubmissionActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<List<PriceProductCompetitor_model>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(PriceProductReportSubmissionActivity.this, "Error Loading Data Try again", Toast.LENGTH_SHORT).show();

            }
        });


    }




    public void sendPrice(){

        progressBar.setVisibility(View.VISIBLE);

        final String userId = user_id;
        final String userName = user_name ;
        final String outlet = outlet_name ;
        final String outletId = outlet_id ;
        final String productName = product_name ;
        final String ourPrice = ourProduct.getText().toString().trim();
        final String note = notes.getText().toString().trim();
        final String adminId = SharedPrefManager.getInstance(this).getUserUnit().trim();
        // now here we convert this list array into json string

        Gson gson=new Gson();

        final String newDataArray=gson.toJson(productCompetitor); // dataarray is list aaray

        // now we have json string lets send it to server using volly

        final String server_url="https://impulsepromotions.co.ke/bidco/mobile/v1/priceTracker_report.php"; // url of server check this 100 times it must be working

        // volley

        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        progressBar.setVisibility(View.GONE);

                        final String result=response.toString();
                        Log.d("response", "result : "+result); //when response come i will log it
                        Toast.makeText(PriceProductReportSubmissionActivity.this, "Price Tracker Saved Successfully", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(PriceProductReportSubmissionActivity.this, PriceProductSelectionActivity.class));
                        PriceProductReportSubmissionActivity.this.finish();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressBar.setVisibility(View.GONE);
                        error.printStackTrace();
                        error.getMessage(); // when error come i will log it
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<String, String>();
                //param.put("array",newDataArray); // array is key which we will use on server side
                param.put("user_id",userId);
                param.put("outlet_name",outlet);
                param.put("user_name",userName);
                param.put("outlet_id",outletId);
                param.put("admin_id", adminId);

                param.put("ourProduct_name",productName);
                param.put("ourProduct_price",ourPrice);
                param.put("notes",note);



                return param;
            }
        };
        //Vconnection.getnInstance(this).addRequestQue(stringRequest); // vConnection i claas which used to connect volley
        Volley.newRequestQueue(PriceProductReportSubmissionActivity.this).add(stringRequest);



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
