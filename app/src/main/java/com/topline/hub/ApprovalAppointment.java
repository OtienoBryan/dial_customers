package com.topline.hub;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;



import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class ApprovalAppointment extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Appointments> appoint;
    private ApprovalAppointmentsAdapter adapter;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private RelativeLayout rlRecyclerview, rlBlank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Approve Appointments");

        progressBar = (ProgressBar)findViewById(R.id.progressApproval);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewApproval);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        rlRecyclerview = (RelativeLayout) findViewById(R.id.rlRecyclerviewApproval);
        rlBlank = (RelativeLayout) findViewById(R.id.rlApprovalBlank);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait! Posting ...");

        fetchAppoints(""+SharedPrefManager.getInstance(ApprovalAppointment.this).getUserTeam());

    }

    public void fetchAppoints(String team){

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<List<Appointments>> call = apiInterface.getApprovalAppointments(team);

        call.enqueue(new Callback<List<Appointments>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Appointments>> call, Response<List<Appointments>> response) {

                progressBar.setVisibility(View.GONE);

                appoint = response.body();
                if(appoint.size() > 0) {
                    rlRecyclerview.setVisibility(View.VISIBLE);
                    rlBlank.setVisibility(View.GONE);
                    adapter = new ApprovalAppointmentsAdapter(appoint, ApprovalAppointment.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    rlRecyclerview.setVisibility(View.GONE);
                    rlBlank.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Appointments>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(ApprovalAppointment.this, "Error Loading Data Try again", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_appointments_approval, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }else if(id == R.id.menu_appointment_sync_approval){
            fetchAppoints(""+ SharedPrefManager.getInstance(ApprovalAppointment.this).getUserTeam());
        }
        return super.onOptionsItemSelected(item);
    }
}
