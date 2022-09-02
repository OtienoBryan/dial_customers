package com.topline.hub;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<PayModel> outletList;

    public PayAdapter(Context mCtx, List<PayModel> outletList) {
        this.mCtx = mCtx;
        this.outletList = outletList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_pay_adapter, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final PayModel outlet = outletList.get(position);

        //loading the image
      /*  Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);*/

        final Integer id = outlet.getId();

        holder.textViewId.setText(id.toString());
        holder.outletName.setText(outlet.getOutlet_name());
        holder.title.setText(outlet.getTime());
        holder.amount.setText(outlet.getAmount());
        holder.balance.setText(outlet.getBalance());



        //final String title = holder.textViewTitle.getText().toString();

        final String myid = id.toString();





        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Toast.makeText(mCtx, "You clicked" + product.getTitle(),
                    Toast.LENGTH_LONG).show();*/

                // Intent i = new Intent(mCtx, SubCategoryActivity.class);
                //  mCtx.startActivity(i);

                //  openDetailActivity(id.toString(),productModel.getTitle());
            }
        });



    }

    @Override
    public int getItemCount() {
        return outletList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView outletName, textViewId, title, amount, balance;
        // RelativeLayout relativeLayout;
        CardView cardView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            outletName = itemView.findViewById(R.id.outletName);
            title = itemView.findViewById(R.id.title);
            amount = itemView.findViewById(R.id.amount);
            balance = itemView.findViewById(R.id.balance);
            textViewId = itemView.findViewById(R.id.textViewId);


            cardView = itemView.findViewById(R.id.cardView);

            // relativeLayout = itemView.findViewById(R.id.relativeLayout);


        }
    }}




