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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<CartModel> cats;

    public CartAdapter(Context mCtx, List<CartModel> cats) {
        this.mCtx = mCtx;
        this.cats = cats;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_cart_adapter, null);
        return new ProductViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final CartModel cat = cats.get(position);



        final Integer id = cat.getId();

        //final String cat_id = cat.getCat_id();
        holder.cat_name.setText(cat.getName());
        holder.quantity.setText("Qty: "+cat.getQuantity());
        holder.price.setText("Unit Price: Ksh. "+cat.getPrice());
        holder.subTotal.setText("Sub Total: Ksh. "+cat.getSub_total());
        Glide.with(mCtx).load(cat.getImage()).into(holder.product_image);

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

        TextView cat_name, quantity, price, subTotal;
        // RelativeLayout relativeLayout;
        CardView cardView;
        ImageView product_image;


        public ProductViewHolder(View itemView) {
            super(itemView);

            cat_name = itemView.findViewById(R.id.txtProductCategory);
            cardView = itemView.findViewById(R.id.cardView);
            product_image = itemView.findViewById(R.id.product_image);
            quantity = itemView.findViewById(R.id.txtQuantity);
            price = itemView.findViewById(R.id.unitPrice);
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
