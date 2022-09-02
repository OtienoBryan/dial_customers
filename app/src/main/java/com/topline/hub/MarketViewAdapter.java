package com.topline.hub;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class MarketViewAdapter extends RecyclerView.Adapter<MarketViewAdapter.MyViewHolder> {

    private List<MarketView_model> expiries;
    private Context context;

    public MarketViewAdapter(List<MarketView_model> expiries, Context context) {
        this.expiries = expiries;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_market_view_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Integer id = expiries.get(position).getId();
        final String comments = expiries.get(position).getComment();


        holder.outlet.setText(expiries.get(position).getOutlet());
        holder.product_name.setText(expiries.get(position).getProduct_name());
        // holder.product_code.setText(expiries.get(position).getProduct_code());
        holder.date.setText(expiries.get(position).getDate());
        holder.activity.setText(expiries.get(position).getActivity());
        holder.impact.setText(expiries.get(position).getImpact());
        holder.comment.setText(expiries.get(position).getComment());
        holder.imple_date.setText(expiries.get(position).getImple_date());
        holder.date.setText(expiries.get(position).getDate());


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /*Toast.makeText(context, "You clicked" + apps.get(position).getOutlet(),
                    Toast.LENGTH_LONG).show();*/

                // Intent i = new Intent(mCtx, SubCategoryActivity.class);
                //  mCtx.startActivity(i);

                openDetailActivity(id.toString(),expiries.get(position).getOutlet(),expiries.get(position).getProduct_name(),expiries.get(position).getProduct_code(),expiries.get(position).getActivity(),expiries.get(position).getImpact(),expiries.get(position).getComment(),expiries.get(position).getImple_date(),expiries.get(position).getDate());
            }
        });





    }

    @Override
    public int getItemCount() {
        return expiries.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView outlet,product_name,imple_date,activity,date,impact,comment;
        TableRow tableRow;
        RelativeLayout relativeLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            outlet = itemView.findViewById(R.id.client);
            product_name = itemView.findViewById(R.id.product);
            imple_date = itemView.findViewById(R.id.imple_date);
            activity = itemView.findViewById(R.id.activity);
            impact = itemView.findViewById(R.id.impact);
            comment = itemView.findViewById(R.id.comment);
            date = itemView.findViewById(R.id.date);
            tableRow = itemView.findViewById(R.id.tableQuantity);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);



        }
    }



    private void openDetailActivity(String expiry_id,String outlet,String product_name, String product_code, String batch_no, String expiry_date, String quantity, String comments, String manu_date){




        Intent i = new Intent(context, NewExpiryUpdate.class);

        //PACK DATA
        i.putExtra("ID_KEY", expiry_id);
        i.putExtra("OUTLET_KEY", outlet);
        i.putExtra("PRODUCT_NAME_KEY", product_name);
        i.putExtra("PRODUCT_CODE_KEY", product_code);
        i.putExtra("BATCH_NO_KEY", batch_no);
        i.putExtra("EXPIRY_DATE_KEY", expiry_date);
        i.putExtra("QUANTITY_KEY", quantity);
        i.putExtra("COMMENTS_KEY", comments);
        i.putExtra("MANU_DATE_KEY", manu_date);

        //context.startActivity(i);

    }


}
