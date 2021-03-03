package com.netwokz.linkednotes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvItem;
        public CheckBox cbCurent;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvName = itemView.findViewById(R.id.item_name);
            tvItem = itemView.findViewById(R.id.item_item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbCurent.setChecked(!cbCurent.isChecked());
                }
            });
            cbCurent = itemView.findViewById(R.id.item_checkbox);
            cbCurent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cbCurent.setChecked(isChecked);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.recycle_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroceryListItem mItem = mGroceryLists.get(position);

        TextView mItemView = holder.tvItem;
        mItemView.setText(mItem.getItem());

        TextView mNameView = holder.tvName;
        mNameView.setText(mItem.getPerson());

        CheckBox mCheckBox = holder.cbCurent;
        mCheckBox.setChecked(mItem.isActive());
    }

    @Override
    public int getItemCount() {
        return mGroceryLists.size();
    }


    private List<GroceryListItem> mGroceryLists;

    public ItemAdapter(List<GroceryListItem> lists) {
        mGroceryLists = lists;
    }

}
