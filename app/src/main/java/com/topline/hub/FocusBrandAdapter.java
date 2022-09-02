package com.topline.hub;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class FocusBrandAdapter extends RecyclerView.Adapter<FocusBrandAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<FocusBrand_model> products;

    @Override
    public void onViewAttachedToWindow(@NonNull ProductViewHolder holder) {
//        if (holder instanceof ProductViewHolder) {
//            holder.setIsRecyclable(false);
//        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ProductViewHolder holder) {
//        if (holder instanceof ProductViewHolder) {
//            holder.setIsRecyclable(true);
//        }
        super.onViewDetachedFromWindow(holder);
    }

    public FocusBrandAdapter(Context mCtx, List<FocusBrand_model> products) {
        this.mCtx = mCtx;
        this.products = products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.focusbrands_list_items, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final FocusBrand_model product = products.get(position);

        final Integer id = product.getId();
        final String sku = product.getSku();
        //final String availability = product.getAvailability();

        holder.product_name.setText(product.getProduct_name() +" "+ product.getSku());
        //holder.sku.setText(product.getSku());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  openDetailActivity(id.toString(),productModel.getTitle());
            }
        });




//        holder.mCheckBox.setChecked(products.get(position).isSelected());
//
//        holder.mCheckBox.setTag(products.get(position));
//
//
//        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                FocusBrand_model available = (FocusBrand_model) cb.getTag();
//
//                available.setSelected(cb.isChecked());
//                products.get(position).setSelected(cb.isChecked());
//
//                Toast.makeText(
//                        v.getContext(),
//                        "Clicked on Checkbox: " + cb.getText() + " is "
//                                + cb.isChecked(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//
//
//
//
//
//        holder.nCheckBox.setChecked(products.get(position).isSelected());
//
//        holder.nCheckBox.setTag(products.get(position));
//
//
//        holder.nCheckBox.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                FocusBrand_model notListed = (FocusBrand_model) cb.getTag();
//
//                notListed.setSelected(false);
//                products.get(position).setSelected(false);
//
//                Toast.makeText(
//                        v.getContext(),
//                        "Clicked on Checkbox: " + cb.getText() + " is "
//                                + cb.isChecked(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//        holder.nlCheckBox.setChecked(products.get(position).isSelected());
//        holder.nlCheckBox.setTag(products.get(position));
//        holder.nlCheckBox.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                FocusBrand_model notListed = (FocusBrand_model) cb.getTag();
//                notListed.setSelected(false);
//                products.get(position).setType("Not listed");
//            }
//        });
//
//        holder.nsCheckBox.setChecked(products.get(position).isSelected());
//        holder.nsCheckBox.setTag(products.get(position));
//        holder.nsCheckBox.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                products.get(position).setType("Not Stocked");
//            }
//        });
//
//
//
//////////////// UNCHECK THE CHECKBOX ONCE ONE IS SELECTED
//
//        holder.mCheckBox.setOnCheckedChangeListener(null);
//
//        //holder.mCheckBox.setSelected(product.isSelected());
//
//        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                holder.nCheckBox.setChecked(!isChecked);
//            }
//        });
//
//
//        holder.nCheckBox.setOnCheckedChangeListener(null);
//
//        //holder.mCheckBox.setSelected(product.isSelected());
//
//        holder.nCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//
//                holder.mCheckBox.setChecked(!isChecked);
//
//
//            }
//        });
//
//        holder.nsCheckBox.setOnCheckedChangeListener(null);
//
//        //holder.mCheckBox.setSelected(product.isSelected());
//
//        holder.nsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                holder.nsCheckBox.setChecked(!isChecked);
//            }
//        });
//
//
//        holder.nlCheckBox.setOnCheckedChangeListener(null);
//
//        //holder.mCheckBox.setSelected(product.isSelected());
//
//        holder.nlCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//
//                holder.nlCheckBox.setChecked(!isChecked);
//
//
//            }
//        });





    }
//    @Override
//    public int getItemViewType(int position) {
//        return 1;
//    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView product_name;
        // RelativeLayout relativeLayout;
        CardView cardView;

        private CheckBox mCheckBox, nCheckBox, nlCheckBox,nsCheckBox;

        RadioGroup rg;
        RadioButton rdAvailable,rdNotAvailable,rdNotStocked,rdNotListed;


        public ProductViewHolder(View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.focusbrand);
            cardView = itemView.findViewById(R.id.cardView);

            mCheckBox = (CheckBox)itemView.findViewById(R.id.checkBoxAvail);
            nCheckBox = (CheckBox)itemView.findViewById(R.id.checkBoxNot);
            nlCheckBox = (CheckBox)itemView.findViewById(R.id.checkBoxNotList);
            nsCheckBox = (CheckBox)itemView.findViewById(R.id.checkBoxNotStock);


        }
    }







}
