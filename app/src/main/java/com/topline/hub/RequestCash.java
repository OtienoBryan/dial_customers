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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RequestCash extends RecyclerView.Adapter<RequestCash.ProductViewHolder>{
    private Context mCtx;
    private List<CompleteModel> cats;

    public RequestCash(Context mCtx, List<CompleteModel> cats) {
        this.mCtx = mCtx;
        this.cats = cats;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_request_cash, null);
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

        final String userPhone = SharedPrefManager.getInstance(mCtx).getUserTelephone().toString();
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDetailActivity(userPhone, cat.getTotal());
            }
        });



    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView cat_name, territory, delivey_fee, subTotal;
        // RelativeLayout relativeLayout;
        CardView cardView;
        Button pay;

        public ProductViewHolder(View itemView) {
            super(itemView);

            pay = itemView.findViewById(R.id.pay);


        }
    }

    private void openDetailActivity(String customer, String total){
         Intent i = new Intent(mCtx, MyMpesa.class);

        //PACK DATA
        i.putExtra("TOTAL", total);
        i.putExtra("CUSTOMER", customer);

        mCtx.startActivity(i);

    }
}
