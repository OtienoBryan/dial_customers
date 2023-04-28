package com.topline.hub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<OrderModel> cats;

    public OrderAdapter(Context mCtx, List<OrderModel> cats) {
        this.mCtx = mCtx;
        this.cats = cats;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_order_adapter, null);
        return new ProductViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final OrderModel cat = cats.get(position);
        final String status = cats.get(position).getImage();



        final Integer id = cat.getId();

        holder.quantity.setText("Date: "+cat.getQuantity());
        holder.price.setText("Amount: Ksh."+cat.getPrice());
        //holder.subTotal.setText("Status:"+cat.getSub_total());

        switch (status) {
            case "1":

                holder.subTotal.setText("Pending");
                break;
            case "2":

                holder.subTotal.setText("Canceled");
                holder.cardView.setBackgroundColor(R.color.red);
                break;
            case "3":

                holder.subTotal.setText("In Progress");
                break;
            case "4":

                holder.subTotal.setText("Delivered");
                holder.cardView.setBackgroundColor(R.color.logout);
                break;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDetailActivity(id.toString(), cat.getCat_id(),cat.getName(), cat.getImage());
            }
        });



    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView cat_name, quantity, price, subTotal;
        // RelativeLayout relativeLayout;
        CardView cardView;
        ImageView product_image;


        public ProductViewHolder(View itemView) {
            super(itemView);

            //cat_name = itemView.findViewById(R.id.txtProductCategory);
            cardView = itemView.findViewById(R.id.cardView);
            //product_image = itemView.findViewById(R.id.product_image);
            quantity = itemView.findViewById(R.id.txtQuantity);
            price = itemView.findViewById(R.id.unitPrice);
            subTotal = itemView.findViewById(R.id.subTotal);


        }
    }

    private void openDetailActivity(String id, String cat_id, String name, String image){

        Intent i = new Intent(mCtx, CartItems.class);

        //PACK DATA
        i.putExtra("ID_KEY", id);
        i.putExtra("CAT_ID", cat_id);
        i.putExtra("CAT_NAME", name);
        i.putExtra("STATUS", image);

        mCtx.startActivity(i);

    }







}
