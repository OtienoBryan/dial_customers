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

public class QualityViewAdapter extends RecyclerView.Adapter<QualityViewAdapter.MyViewHolder> {

    private List<QualityView_model> expiries;
    private Context context;

    public QualityViewAdapter(List<QualityView_model> expiries, Context context) {
        this.expiries = expiries;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_quality_view_adapter, parent, false);
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
        holder.batch_no.setText(expiries.get(position).getBatch_no());
        holder.issue.setText(expiries.get(position).getIssue());
        holder.quantity.setText(expiries.get(position).getQuantity());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /*Toast.makeText(context, "You clicked" + apps.get(position).getOutlet(),
                    Toast.LENGTH_LONG).show();*/

                // Intent i = new Intent(mCtx, SubCategoryActivity.class);
                //  mCtx.startActivity(i);

                openDetailActivity(id.toString(),expiries.get(position).getOutlet(),expiries.get(position).getProduct_name(),expiries.get(position).getProduct_code(),expiries.get(position).getBatch_no(),expiries.get(position).getIssue(),expiries.get(position).getQuantity(),expiries.get(position).getExpiry_date(),expiries.get(position).getDate());
            }
        });





    }

    @Override
    public int getItemCount() {
        return expiries.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView outlet,product_name,product_code,batch_no,date,issue,quantity;
        TableRow tableRow;
        RelativeLayout relativeLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            outlet = itemView.findViewById(R.id.client);
            product_name = itemView.findViewById(R.id.product);
            //product_code = itemView.findViewById(R.id.productCode);
            batch_no = itemView.findViewById(R.id.batchNo);
            issue = itemView.findViewById(R.id.issue);
            quantity = itemView.findViewById(R.id.quantity);
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
