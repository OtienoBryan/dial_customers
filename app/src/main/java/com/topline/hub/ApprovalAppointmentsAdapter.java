package com.topline.hub;

import android.annotation.SuppressLint;
import android.content.Context;


import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApprovalAppointmentsAdapter extends RecyclerView.Adapter<ApprovalAppointmentsAdapter.MyViewHolder> {

    private List<Appointments> apps;
    private Context context;
    String status = "";

    public ApprovalAppointmentsAdapter(List<Appointments> apps, Context context) {
        this.apps = apps;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_approval_appointments_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        status = "";
        final Integer id = apps.get(position).getId();
        final String myoutlet_id = apps.get(position).getOutlet_id();
        holder.outlet.setText(apps.get(position).getOutlet());
        holder.appointdate.setText(apps.get(position).getDate());
//        holder.day.setText(apps.get(position).getDay());
//        holder.txtName.setText(apps.get(position).getS_name());
//        holder.agenda.setText(apps.get(position).getAgenda());
       // holder.segment.setText(apps.get(position).getSegment());

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_POST_APPROVAL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                Toast.makeText(context, "Approved Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context, ApprovalAppointment.class);
                                context.startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                error.printStackTrace();
                                error.getMessage(); // when error come i will log it
                            }
                        }
                )
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param=new HashMap<String, String>();
                        param.put("appoint",""+apps.get(position).getId());
                        param.put("status","1");


                        return param;
                    }
                };
                Volley.newRequestQueue(context).add(stringRequest);
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_POST_APPROVAL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                Toast.makeText(context, "Journey plan CANCELLED Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context, ApprovalAppointment.class);
                                context.startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                error.printStackTrace();
                                error.getMessage(); // when error come i will log it
                            }
                        }
                )
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param=new HashMap<String, String>();
                        param.put("appoint",""+apps.get(position).getId());
                        param.put("status","2");


                        return param;
                    }
                };
                Volley.newRequestQueue(context).add(stringRequest);
            }
        });


    }

    @Override
    public int getItemCount() {
        return apps.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView outlet,appointdate,day,agenda,segment,txtName;
        Button btnApprove,btnCancel;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            outlet = itemView.findViewById(R.id.outletApproval);
            appointdate = itemView.findViewById(R.id.dateApproval);
            txtName = itemView.findViewById(R.id.nameApproval);
            day = itemView.findViewById(R.id.dayApproval);
            agenda = itemView.findViewById(R.id.agendaApprovals);
            //segment = itemView.findViewById(R.id.segmentApproval);

            btnApprove = (Button) itemView.findViewById(R.id.btnApproveAppointment);
            btnCancel = (Button) itemView.findViewById(R.id.btnApproveAppointmentClose);

            cardView = itemView.findViewById(R.id.cardViewApproval);

        }
    }


}
