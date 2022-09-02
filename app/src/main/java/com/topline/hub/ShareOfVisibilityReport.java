package com.topline.hub;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ShareOfVisibilityReport extends AppCompatActivity {

    List<ShareVisibilityReport> shares = new ArrayList<>();
    //the recyclerview
    Button btnShareReport;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_of_visibility_report);
        setTitle(getIntent().getExtras().getString("SHARE_NAME"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerShareReport);
        btnShareReport = (Button) findViewById(R.id.btnShareReport);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar)findViewById(R.id.progressShareReport);
        progressDialog = new ProgressDialog(this);

        String categoryId = getIntent().getExtras().getString("CATEGORY_ID","0");
        String productId = getIntent().getExtras().getString("PRODUCT_ID","0");

        if(!categoryId.equalsIgnoreCase("0")){
            loadProducts(SharedPrefManager.getInstance(this).getUserAccount(),categoryId);
        }else if(!productId.equalsIgnoreCase("0")) {
            shares.add(new ShareVisibilityReport(
                    Integer.parseInt(productId),
                    getIntent().getExtras().getString("PRODUCT_NAME","Product"),
                    ""
            ));

            AdapterVisibilityReport adapter = new AdapterVisibilityReport(shares);
            recyclerView.setAdapter(adapter);
        }


        btnShareReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                final String userId = SharedPrefManager.getInstance(ShareOfVisibilityReport.this).getUserId().toString();
                final String userName = SharedPrefManager.getInstance(ShareOfVisibilityReport.this).getUsername() + " " + SharedPrefManager.getInstance(ShareOfVisibilityReport.this).getUserLastname();
                final String outletId = ""+SharedPrefManager.getInstance(ShareOfVisibilityReport.this).getOutletId();
                final String categoryId = getIntent().getExtras().getString("CATEGORY_ID","0");
                final String categoryName = getIntent().getExtras().getString("CATEGORY_NAME","0");
                final String shareId = getIntent().getExtras().getString("SHARE_ID","0");
                final String shareName = getIntent().getExtras().getString("SHARE_NAME","0");
                final String adminId = SharedPrefManager.getInstance(ShareOfVisibilityReport.this).getUserAccount().trim();
                final String outlet = SharedPrefManager.getInstance(ShareOfVisibilityReport.this).getOutletName();

                Gson gson=new Gson();
                final String newDataArray=gson.toJson(shares);
                final String server_url="https://impulsepromotions.co.ke/impulse/mobile/v1/share_visibility_report.php"; // url of server check this 100 times it must be working

                StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {

                                progressDialog.dismiss();

                                final String result=response.toString();
                                Log.d("response", "result : "+result); //when response come i will log it
                                Toast.makeText(ShareOfVisibilityReport.this, "Share of visibility report Saved Successfully", Toast.LENGTH_LONG).show();
                                onBackPressed();
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
                        param.put("user_name",userName);
                        param.put("outlet_id",outletId);
                        param.put("category_id",categoryId);
                        param.put("category_name",categoryName);
                        param.put("share_id",shareId);
                        param.put("share_name",shareName);
                        return param;
                    }
                };
                Volley.newRequestQueue(ShareOfVisibilityReport.this).add(stringRequest);
            }
        });


    }

    public class AdapterVisibilityReport extends RecyclerView.Adapter<AdapterVisibilityReport.viewHolderShare>{
        List<ShareVisibilityReport> shares;

        public AdapterVisibilityReport(List<ShareVisibilityReport> shares) {
            this.shares = shares;
        }

        @NonNull
        @Override
        public viewHolderShare onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.share_of_visibility_report_model,viewGroup,false);
            viewHolderShare holder = new viewHolderShare(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull viewHolderShare holder, int i) {
            final ShareVisibilityReport obj = shares.get(i);
            holder.txtName.setText(obj.getProduct_name());


        }

        @Override
        public int getItemCount() {
            return shares.size();
        }

        public class viewHolderShare extends RecyclerView.ViewHolder{

            TextView txtName;
            EditText edt;
            public viewHolderShare(@NonNull View itemView) {
                super(itemView);
                txtName = (TextView) itemView.findViewById(R.id.txtAssetReportModel);
                edt = (EditText) itemView.findViewById(R.id.edtAssetReportModel);
                edt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        shares.get(getAdapterPosition()).setEditTextValue(edt.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
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
    private void loadProducts(String admin, String category) {
        String url = "https://impulsepromotions.co.ke/bidco/mobile/v1/getProducts.php?admin="+admin+"&&category="+category;
        Log.e("REPORt","="+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject asset = array.getJSONObject(i);
                                shares.add(new ShareVisibilityReport(
                                        asset.getInt("id"),
                                        asset.getString("name") + " " +asset.getString("sku"),
                                        ""
                                ));
                            }

                            AdapterVisibilityReport adapter = new AdapterVisibilityReport(shares);
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
                        Toast.makeText(ShareOfVisibilityReport.this, "Error Loading products Try again", Toast.LENGTH_SHORT).show();

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
