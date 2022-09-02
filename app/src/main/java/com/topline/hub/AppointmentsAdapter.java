package com.topline.hub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Math.abs;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.MyViewHolder> {

    private List<Appointments> apps;
    private Context context;
    public static String currentTime;

    public AppointmentsAdapter(List<Appointments> apps, Context context) {
        this.apps = apps;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointments, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Integer id = apps.get(position).getId();
        final String myoutlet_id = apps.get(position).getOutlet_id();
        final String status = apps.get(position).getStatus();

        String pattern = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        currentTime = DateFormat.getTimeInstance().format(new Date());
        final String current_time = currentTime;


        if (status.equals("Completed")){
           // holder.cardView.setEnabled(false);
            holder.cardView.setBackgroundColor(R.color.logout);
        }else if(status.equals("Delivered")){
            holder.cardView.setEnabled(false);
            holder.cardView.setBackgroundColor(R.color.warning);
            holder.status.setTextColor(R.color.red);
        }else if(status.equals("Pending Approval")){
            holder.cardView.setEnabled(false);
            //holder.cardView.setBackgroundColor(R.color.warning);
            holder.status.setTextColor(R.color.red);
        }else if(status.equals("Approval Declined")){
            holder.cardView.setEnabled(false);
            holder.cardView.setBackgroundColor(R.color.warning);
            holder.status.setTextColor(R.color.red);
        }else{
            holder.cardView.setEnabled(true);

        }

        holder.outlet.setText(apps.get(position).getOutlet());
        holder.appointdate.setText(apps.get(position).getDate());
//        holder.day.setText(apps.get(position).getDay());
        holder.start_time.setText(apps.get(position).getDate());
        holder.end_time.setText(apps.get(position).getDate());
        holder.status.setText(apps.get(position).getStatus());
        holder.charges.setText(apps.get(position).getAmount());


//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               /*Toast.makeText(context, "You clicked" + apps.get(position).getOutlet(),
//                    Toast.LENGTH_LONG).show();*/
//
//                // Intent i = new Intent(mCtx, SubCategoryActivity.class);
//                //  mCtx.startActivity(i);
//
//               openDetailActivity(id.toString(),apps.get(position).getOutlet(),myoutlet_id,apps.get(position).getLatitude(),apps.get(position).getLongitude(),apps.get(position).getEndTime(), apps.get(position).getStatus());
//            }
//        });





    }

    @Override
    public int getItemCount() {
        return apps.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView outlet,appointdate,day,start_time,end_time,status,agenda,checkin, charges, paid;

        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            outlet = itemView.findViewById(R.id.outlet);
            appointdate = itemView.findViewById(R.id.date);
            //day = itemView.findViewById(R.id.day);
            start_time = itemView.findViewById(R.id.start_time);
            end_time = itemView.findViewById(R.id.end_time);
            charges = itemView.findViewById(R.id.charges);
            paid = itemView.findViewById(R.id.paid);
            status = itemView.findViewById(R.id.status);
            checkin = itemView.findViewById(R.id.mycheckin);

            cardView = itemView.findViewById(R.id.cardView);

        }
    }



     private void openDetailActivity(String id,String outlet_name,String outlet_id, String latitude, String longitude, String end_time, String status) {

         currentTime = DateFormat.getTimeInstance().format(new Date());
         final String current_time = currentTime;
         final String my_end = end_time;


             Intent i = new Intent(context, AppointmentAction.class);

             //PACK DATA
             i.putExtra("ID_KEY", id);
             i.putExtra("NAME_KEY", outlet_name);
             i.putExtra("OUTLET_ID_KEY", outlet_id);
             i.putExtra("LATITUDE_KEY", latitude);
             i.putExtra("LONGITUDE_KEY", longitude);
             i.putExtra("END_TIME_KEY", end_time);


         Intent j = new Intent(context, MyRequests.class);

         //PACK DATA
         j.putExtra("ID_KEY", id);
         j.putExtra("NAME_KEY", outlet_name);
         j.putExtra("OUTLET_ID_KEY", outlet_id);
         j.putExtra("LATITUDE_KEY", latitude);
         j.putExtra("LONGITUDE_KEY", longitude);
         j.putExtra("END_TIME_KEY", end_time);
         j.putExtra("STATUS_KEY", status);



         if(SharedPrefManager.getInstance(context).getUserLastname().equalsIgnoreCase("55")){
             context.startActivity(i);
         }else if (SharedPrefManager.getInstance(context).getUserLastname().equalsIgnoreCase("56")){
             context.startActivity(j);
         }


     }



}
