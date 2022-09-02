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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<ProductModel> cats;

    public ProductAdapter(Context mCtx, List<ProductModel> cats) {
        this.mCtx = mCtx;
        this.cats = cats;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_product_adapter2, null);
        return new ProductViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final ProductModel cat = cats.get(position);



        final Integer id = cat.getId();

        //final String cat_id = cat.getCat_id();
        holder.cat_name.setText(cat.getName());
        holder.price.setText("KES "+cat.getPrice());
        Glide.with(mCtx).load(cat.getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDetailActivity(id.toString(), cat.getCat_id(),cat.getName(), cat.getImage(),  cat.getPrice(), cat.getDescription(), cat.getUsage(), cat.getStatus());
            }
        });



    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView cat_name, price;
        // RelativeLayout relativeLayout;
        CardView cardView;
        ImageView image;


        public ProductViewHolder(View itemView) {
            super(itemView);

            cat_name = itemView.findViewById(R.id.txtProductCategory);
            cardView = itemView.findViewById(R.id.cardView);
            image = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price);


        }
    }

    private void openDetailActivity(String product_id,String product_code, String product_name, String product_image, String price, String description, String usage, String status){




        Intent i = new Intent(mCtx, ExpiryProductActivity.class);

        //PACK DATA
        i.putExtra("ID_KEY", product_id);
        i.putExtra("PRODUCT_NAME_KEY", product_name);
        i.putExtra("PRODUCT_CODE_KEY", product_code);
        i.putExtra("PRODUCT_IMAGE", product_image);
        i.putExtra("PRODUCT_PRICE", price);
        i.putExtra("PRODUCT_DESC", description);
        i.putExtra("PRODUCT_USAGE", usage);
        i.putExtra("PRODUCT_STATUS", status);

        mCtx.startActivity(i);

    }



//    private void openDetailActivity(String id, String cat_id, String name){
//
//
//
//
//        Intent i = new Intent(mCtx, MainActivity.class);
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
