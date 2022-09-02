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

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<CompleteModel> cats;

    public CompleteAdapter(Context mCtx, List<CompleteModel> cats) {
        this.mCtx = mCtx;
        this.cats = cats;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_complete_adapter, null);
        return new ProductViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final CompleteModel cat = cats.get(position);



        final Integer id = cat.getId();
        final String sub_total = cat.getTotal();
        final String d_fee = cat.getDelivery_fee();

        int a =Integer.parseInt(sub_total);
        int b =Integer.parseInt(d_fee);
        int c = a+ b;

        //final String cat_id = cat.getCat_id();
        holder.cat_name.setText("Cart Value: KES "+cat.getTotal());
        holder.territory.setText("Territory: "+cat.getTerritory_name());
        holder.delivey_fee.setText("Delivery Fee: KES "+cat.getDelivery_fee());
        holder.subTotal.setText("Amount Payable: KES "+c);
//        Glide.with(mCtx).load(cat.getImage()).into(holder.product_image);

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                openDetailActivity(id.toString(), cat.getCat_id(),cat.getName());
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView cat_name, territory, delivey_fee, subTotal;
        // RelativeLayout relativeLayout;
        CardView cardView;
        ImageView product_image;


        public ProductViewHolder(View itemView) {
            super(itemView);

            cat_name = itemView.findViewById(R.id.txtProductCategory);
            cardView = itemView.findViewById(R.id.cardView);
            territory = itemView.findViewById(R.id.territory);
            delivey_fee = itemView.findViewById(R.id.delivery_fee);
            subTotal = itemView.findViewById(R.id.subTotal);


        }
    }



//    private void openDetailActivity(String id, String cat_id, String name){
//
//
//
//
//        Intent i = new Intent(mCtx, CategoryProducts.class);
//
//        //PACK DATA
//        i.putExtra("ID_KEY", id);
//        i.putExtra("CAT_ID", cat_id);
//        i.putExtra("CAT_NAME", name);
//
//        mCtx.startActivity(i);
//
//    }







}
