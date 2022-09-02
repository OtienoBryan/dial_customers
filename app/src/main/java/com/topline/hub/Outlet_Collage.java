package com.topline.hub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class Outlet_Collage extends Fragment {

    RelativeLayout rlRecycler,rlBlank;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private ApiInterface apiInterface;
    private Context context;
    private List<Appointments> appointments;
    private String outlet = "";
    public Outlet_Collage() {
    }

    @SuppressLint("ValidFragment")
    public Outlet_Collage(Context context, String outlet) {
        this.context = context;
        this.outlet = outlet;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_outlet__collage, container, false);
        recyclerView = v.findViewById(R.id.outletrecyclerView);
        rlRecycler = v.findViewById(R.id.rlOutletRecyclerview);
        rlBlank = v.findViewById(R.id.rlOutletBlank);
        progressBar = v.findViewById(R.id.Outletprogress);
        fetchAppoints();
        return v;
    }

    public void fetchAppoints(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<List<Appointments>> call = apiInterface.getOutletAppointments(outlet,SharedPrefManager.getInstance(context).getUserIdString());

        call.enqueue(new Callback<List<Appointments>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Appointments>> call, Response<List<Appointments>> response) {
                progressBar.setVisibility(View.GONE);
                appointments = response.body();
                if(appointments.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    rlBlank.setVisibility(View.GONE);
                    AppointmentsAdapter adapter = new AppointmentsAdapter(appointments, context.getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                }else{
                    recyclerView.setVisibility(View.GONE);
                    rlBlank.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Appointments>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Error Loading Data Try again", Toast.LENGTH_SHORT).show();

            }
        });


    }

}
