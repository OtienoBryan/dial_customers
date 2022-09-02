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

public class ExpiryProductAdapter extends RecyclerView.Adapter<ExpiryProductAdapter.MyViewHolder> {

    private List<ExpiryProduct_model> products;
    private Context context;

    public ExpiryProductAdapter(List<ExpiryProduct_model> products, Context context) {
        this.products = products;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expiry_product_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Integer id = products.get(position).getId();

        holder.product_name.setText(products.get(position).getProduct_name()+" "+products.get(position).getSku());
        holder.product_code.setText("Ksh." +products.get(position).getProduct_code());





        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               openDetailActivity(id.toString(),products.get(position).getProduct_name()+" "+products.get(position).getSku(),products.get(position).getPrice(), products.get(position).getImage(), products.get(position).getProduct_code(), products.get(position).getStatus());
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



     private void openDetailActivity(String product_id,String product_name, String price, String product_image, String product_code, String status){




        Intent i = new Intent(context, ExpiryProductActivity.class);

        //PACK DATA
        i.putExtra("ID_KEY", product_id);
         i.putExtra("PRODUCT_NAME_KEY", product_name);
         i.putExtra("PRODUCT_CODE_KEY", product_code);
         i.putExtra("PRODUCT_STATUS", status);
         i.putExtra("PRODUCT_IMAGE", product_image);
         i.putExtra("PRODUCT_PRICE", price);

        context.startActivity(i);

    }


}
