package com.netwokz.linkednotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

//    interface OnClickListener {
//        void onClick(Boolean clickedItem, int position);
//    }

    interface OnItemClickListener {
        void onItemClick(Boolean clickedItem, int position);
    }

    private final OnItemClickListener listener;

//    private OnClickListener mCallback;

//    public void setOnClickListener(OnClickListener callback) {
//        mCallback = callback;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvItem;
        public CheckBox cbCurent;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            prefs = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());

            tvName = itemView.findViewById(R.id.item_name);
            tvItem = itemView.findViewById(R.id.item_item);

            cbCurent = itemView.findViewById(R.id.item_checkbox);
//            cbCurent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (listener != null) {
//                        listener.onItemClick(isChecked, getLayoutPosition());
//
////                        Log.d("ItemAdapter", "getLayoutPosition = " + getLayoutPosition());
//
//                        edit = prefs.edit();
//                        edit.putBoolean("Checkbox" + getLayoutPosition(), isChecked);
//                        edit.commit();
//                    }
//                }
//            });
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
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.onItemClick(isChecked, position);

//                    Log.d("ItemAdapter", "getLayoutPosition = " + position);

                    edit = prefs.edit();
                    edit.putBoolean("Checkbox" + position, isChecked);
                    edit.commit();
                }
            }
        });
//        Log.d("ItemAdapter", "position = " + position);
//        prefs = mCheckBox.getContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        mCheckBox.setChecked(prefs.getBoolean("Checkbox" + position, false));
//        mCheckBox.setChecked(Boolean.parseBoolean(mItem.mIsCurrent));
    }

    @Override
    public int getItemCount() {
        return mGroceryLists.size();
    }


    private List<GroceryListItem> mGroceryLists;

    public ItemAdapter(List<GroceryListItem> lists, final OnItemClickListener listener) {
        mGroceryLists = lists;
        this.listener = listener;
    }

    public void clearItems() {
        mGroceryLists.clear();
    }

}
