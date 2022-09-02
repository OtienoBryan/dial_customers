package com.topline.hub;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareOfVisibility extends AppCompatActivity {

    List<ShareVisibility> shares = new ArrayList<>();
    //the recyclerview
    RecyclerView recyclerView;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_of_visibility);
        setTitle("Share of Visibility");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerShareVisibility);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar)findViewById(R.id.progressShareVisibility);


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        retrofit2.Call<List<ShareVisibility>> call = apiInterface.getShareVisibility(SharedPrefManager.getInstance(this).getUserAccount());
        retrofit2.Call<List<ShareVisibility>> call = apiInterface.getShareVisibility("14");
        call.enqueue(new Callback<List<ShareVisibility>>() {
            @Override
            public void onResponse(Call<List<ShareVisibility>> call, Response<List<ShareVisibility>> response) {
                progressBar.setVisibility(View.GONE);

                shares = response.body();
                adapterShare adapter = new adapterShare(shares);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<ShareVisibility>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ShareOfVisibility.this, "No share of visibility setup", Toast.LENGTH_LONG).show();
            }
        });

    }

    public class adapterShare extends RecyclerView.Adapter<adapterShare.viewHolderShare>{
        List<ShareVisibility> shares;

        public adapterShare(List<ShareVisibility> shares) {
            this.shares = shares;
        }

        @NonNull
        @Override
        public viewHolderShare onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.share_of_visibility_model,viewGroup,false);
            viewHolderShare holder = new viewHolderShare(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull viewHolderShare holder, int i) {
            final ShareVisibility obj = shares.get(i);
            holder.txt.setText(obj.getShareName());
            holder.txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShareOfVisibility.this,ShareOfVisibilityReport.class);
                    intent.putExtra("CATEGORY_ID",obj.getCategoryId());
                    intent.putExtra("CATEGORY_NAME",obj.getCategoryName());
                    intent.putExtra("PRODUCT_ID",obj.getProductId());
                    intent.putExtra("PRODUCT_NAME",obj.getProductName());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return shares.size();
        }

        public class viewHolderShare extends RecyclerView.ViewHolder{

            TextView txt;
            public viewHolderShare(@NonNull View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txtShareVisibilityModel);
            }
        }
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
