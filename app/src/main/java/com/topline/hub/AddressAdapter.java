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

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<CompleteModel> cats;

    public AddressAdapter(Context mCtx, List<CompleteModel> cats) {
        this.mCtx = mCtx;
        this.cats = cats;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_address_adapter, null);
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
        holder.address.setText("Address: "+cat.getTotal());
        holder.instruction.setText("Instructions: "+cat.getTerritory_name());

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

        TextView address, instruction, delivey_fee, subTotal;
        // RelativeLayout relativeLayout;
        CardView cardView;
        ImageView product_image;


        public ProductViewHolder(View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.address);
            cardView = itemView.findViewById(R.id.cardView);
            instruction = itemView.findViewById(R.id.instruction);

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
