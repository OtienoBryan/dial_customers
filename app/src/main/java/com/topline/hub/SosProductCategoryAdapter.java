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

public class SosProductCategoryAdapter extends RecyclerView.Adapter<SosProductCategoryAdapter.MyViewHolder> {

    private List<SosProductCategory_model> categories;
    private Context context;

    public SosProductCategoryAdapter(List<SosProductCategory_model> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sos_product_category_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final Integer id = categories.get(position).getId();

        holder.category_name.setText(categories.get(position).getCategory_name());
        //holder.check.setText(categories.get(position).getChecklist());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openDetailActivity(id.toString(),categories.get(position).getCategory_name());
            }
        });





    }

    @Override
    public int getItemCount() {
        return categories.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView category_name;
        CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.txtProductCategory);
            cardView = itemView.findViewById(R.id.cardView);



        }
    }



    private void openDetailActivity(String category_id,String category_name){




        Intent i = new Intent(context, SosActivity.class);

        //PACK DATA
        i.putExtra("ID_KEY", category_id);
        i.putExtra("CATEGORY_NAME_KEY", category_name);


        context.startActivity(i);

    }


}
