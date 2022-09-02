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

public class PosmAdapter extends RecyclerView.Adapter<PosmAdapter.MyViewHolder> {

    private List<Posm_model> posms;
    private Context context;

    public PosmAdapter(List<Posm_model> posms, Context context) {
        this.posms = posms;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posm_merchandiser_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Integer id = posms.get(position).getId();

        holder.material.setText(posms.get(position).getMaterial());
        holder.quantity.setText(posms.get(position).getQuantity());




        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /*Toast.makeText(context, "You clicked" + apps.get(position).getOutlet(),
                    Toast.LENGTH_LONG).show();*/

                // Intent i = new Intent(mCtx, SubCategoryActivity.class);
                //  mCtx.startActivity(i);

               openDetailActivity(id.toString(),posms.get(position).getMaterial(),posms.get(position).getQuantity());
            }
        });





    }

    @Override
    public int getItemCount() {
        return posms.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView material,quantity;
        LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            material = itemView.findViewById(R.id.posm);
            quantity = itemView.findViewById(R.id.posm_quantity);
            linearLayout = itemView.findViewById(R.id.linearLayout);



        }
    }



     private void openDetailActivity(String posm_id,String material, String quantity){




        Intent i = new Intent(context, PosmDistributionActivity.class);

        //PACK DATA
        i.putExtra("ID_KEY", posm_id);
         i.putExtra("MATERIAL_KEY", material);
         i.putExtra("QUANTITY_KEY", quantity);

        context.startActivity(i);

    }


}
