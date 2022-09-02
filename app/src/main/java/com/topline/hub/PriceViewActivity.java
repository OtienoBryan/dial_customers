package com.topline.hub;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class PriceViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<PriceView_model> expiry;
    private PriceViewAdapter adapter;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;

    private EditText editSearch;
    String userId;
    Button return_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_view);

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
                fetchExpiryReport( newText.toString(), userId);
            }
        });



        fetchExpiryReport("",userId);


        return_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharedPrefManager.getInstance(PriceViewActivity.this).getUserLastname().equalsIgnoreCase("55")){
                    startActivity(new Intent(PriceViewActivity.this, QuestionsActivity.class));
                    PriceViewActivity.this.finish();
                }else {
                    startActivity(new Intent(PriceViewActivity.this, QuestionsActivity.class));
                    PriceViewActivity.this.finish();

                }
            }
        });


    }


    public void fetchExpiryReport(String key, String userId){

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<List<PriceView_model>> call = apiInterface.getPriceReports(key,userId);

        call.enqueue(new Callback<List<PriceView_model>>() {
            @Override
            public void onResponse(retrofit2.Call<List<PriceView_model>> call, Response<List<PriceView_model>> response) {

                progressBar.setVisibility(View.GONE);

                expiry = response.body();
                adapter = new PriceViewAdapter(expiry, PriceViewActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<List<PriceView_model>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(PriceViewActivity.this, "Error Loading Data Try again", Toast.LENGTH_SHORT).show();

            }
        });


    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }else if(id == R.id.add){

            Intent intent = new Intent(PriceViewActivity.this, PriceListActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }



}
