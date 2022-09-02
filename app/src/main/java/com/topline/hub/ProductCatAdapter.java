package com.topline.hub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProductCatAdapter extends RecyclerView.Adapter<ProductCatAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<ProductCat_model> cats;

    public ProductCatAdapter(Context mCtx, List<ProductCat_model> cats) {
        this.mCtx = mCtx;
        this.cats = cats;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.productcategory_list, null);
        return new ProductViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final ProductCat_model cat = cats.get(position);



        final Integer id = cat.getId();

        /*final String cat_id = cats.get(position).getCat_id();
        final String catcolor_id = cats.get(position).getCatcolor_id();

        if (cat_id.equals(catcolor_id)){

            holder.cardView.setEnabled(true);
            holder.cardView.getResources().getColor(R.color.green);

            holder.cardView.setCardBackgroundColor(R.color.green);

        } */

        //final String cat_id = cat.getCat_id();
        holder.cat_name.setText(cat.getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                  openDetailActivity(id.toString(), cat.getCat_id(),cat.getName());
            }
        });



    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView cat_name;
        // RelativeLayout relativeLayout;
        CardView cardView;


        public ProductViewHolder(View itemView) {
            super(itemView);

            cat_name = itemView.findViewById(R.id.txtProductCategory);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }



    private void openDetailActivity(String id, String cat_id, String name){




        Intent i = new Intent(mCtx, FocusBrandsActivity.class);

        //PACK DATA
        i.putExtra("ID_KEY", id);
        i.putExtra("CAT_ID", cat_id);
        i.putExtra("CAT_NAME", name);

        mCtx.startActivity(i);

    }







}
