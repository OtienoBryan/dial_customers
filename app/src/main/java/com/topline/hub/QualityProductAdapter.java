package com.topline.hub;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class QualityProductAdapter extends RecyclerView.Adapter<QualityProductAdapter.MyViewHolder> {

    private List<QualityProduct_model> products;
    private Context context;

    public QualityProductAdapter(List<QualityProduct_model> products, Context context) {
        this.products = products;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_quality_product_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Integer id = products.get(position).getId();

        holder.product_name.setText(products.get(position).getProduct_name()+" "+products.get(position).getSku());
        holder.product_code.setText(products.get(position).getProduct_code());





        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openDetailActivity(id.toString(),products.get(position).getProduct_name()+" "+products.get(position).getSku(),products.get(position).getProduct_code());
            }
        });





    }

    @Override
    public int getItemCount() {
        return products.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView product_name,product_code;
        LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.productName);
            product_code = itemView.findViewById(R.id.productCode);
            linearLayout = itemView.findViewById(R.id.linearLayout);



        }
    }



    private void openDetailActivity(String product_id,String product_name, String product_code){




        Intent i = new Intent(context, MyQualityActivity.class);

        //PACK DATA
        i.putExtra("ID_KEY", product_id);
        i.putExtra("PRODUCT_NAME_KEY", product_name);
        i.putExtra("PRODUCT_CODE_KEY", product_code);

        context.startActivity(i);

    }


}
