package com.topline.hub;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class PriceProductCompetitorAdapter extends RecyclerView.Adapter<PriceProductCompetitorAdapter.MyViewHolder> {

    private List<PriceProductCompetitor_model> productCompetitors;
    private Context context;

    public PriceProductCompetitorAdapter(List<PriceProductCompetitor_model> productCompetitors, Context context) {
        this.productCompetitors = productCompetitors;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.competitor_product_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Integer id = productCompetitors.get(position).getId();

        holder.product_competitor_name.setText(productCompetitors.get(position).getProduct_competitor_name());
        holder.editproduct_competitor_price.setHint(productCompetitors.get(position).getProduct_competitor_name());

        // Saving the price value
        holder.editproduct_competitor_price.setText(productCompetitors.get(position).getEditTextPrice());
        Log.d("print","yes");


    }

    @Override
    public int getItemCount() {
        return productCompetitors.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView product_competitor_name;
        EditText editproduct_competitor_price;
        //LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            product_competitor_name = itemView.findViewById(R.id.productName);
            editproduct_competitor_price = itemView.findViewById(R.id.productPrice);
            //linearLayout = itemView.findViewById(R.id.linearLayout);



            editproduct_competitor_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    productCompetitors.get(getAdapterPosition()).setEditTextPrice(editproduct_competitor_price.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });



        }
    }



     /*private void openDetailActivity(String product_id,String product_name, String product_code){




        Intent i = new Intent(context, PriceProductReportSubmissionActivity.class);

        //PACK DATA
        i.putExtra("ID_KEY", product_id);
         i.putExtra("PRODUCT_NAME_KEY", product_name);
         i.putExtra("PRODUCT_CODE_KEY", product_code);

        context.startActivity(i);

    }*/


}
