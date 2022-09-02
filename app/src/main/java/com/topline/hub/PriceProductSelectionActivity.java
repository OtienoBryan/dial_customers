package com.topline.hub;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class PriceProductSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<PriceProduct_model> product;
    private PriceProductAdapter adapter;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;

    private EditText editSearch;
    String userId;
    public static String admin_id;
    Button return_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_product_selection);
        setTitle("Price Tracker");
        //getSupportActionBar().setHomeButtonEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar)findViewById(R.id.progress);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        editSearch = (EditText)findViewById(R.id.searchbox);
        return_back = (Button) findViewById(R.id.return_back);

        userId = SharedPrefManager.getInstance(this).getUserId().toString();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();



        editSearch.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void afterTextChanged(Editable newText) {
                //after the change calling the method and passing the search input
                //filter(editable.toString());
                fetchExpiryProducts( newText.toString(),admin_id);
            }
        });



        fetchExpiryProducts("",admin_id);

        return_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PriceProductSelectionActivity.this, QuestionsActivity.class));
            }
        });


    }


    public void fetchExpiryProducts(String key, String adminId){

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<List<PriceProduct_model>> call = apiInterface.getPriceProducts(key,adminId);

        call.enqueue(new Callback<List<PriceProduct_model>>() {
            @Override
            public void onResponse(retrofit2.Call<List<PriceProduct_model>> call, Response<List<PriceProduct_model>> response) {

                progressBar.setVisibility(View.GONE);

                product = response.body();
                adapter = new PriceProductAdapter(product, PriceProductSelectionActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<List<PriceProduct_model>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(PriceProductSelectionActivity.this, "Error Loading Data Try again", Toast.LENGTH_SHORT).show();

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

    public void onBackPressed(){
        //super.onBackPressed();

        //checkoutDialog();
        //finish();
        startActivity(new Intent(PriceProductSelectionActivity.this, QuestionsActivity.class));
    }

}
