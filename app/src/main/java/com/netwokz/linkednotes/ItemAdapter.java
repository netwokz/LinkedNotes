package com.netwokz.linkednotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    private final List<GroceryListItem> mGroceryLists;

    private final OnItemClickListener listener;

    interface OnItemClickListener {
        void onItemClick(int position);

        void onEditClick(int position);

        void onDeleteClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        public TextView tvName;
        public TextView tvItem;
        public CheckBox cbCurrent;

        public ViewHolder(View itemView) {
            super(itemView);
            prefs = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());

            tvName = itemView.findViewById(R.id.item_name);
            tvItem = itemView.findViewById(R.id.item_item);

            cbCurrent = itemView.findViewById(R.id.item_checkbox);
            cbCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed() && listener != null) {
                        GroceryListItem mItem = mGroceryLists.get(getAdapterPosition());
                        mItem.setChecked(isChecked);
                        edit = prefs.edit();
                        edit.putBoolean(mItem.getKey(), isChecked);
                        edit.commit();
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                Log.d("ItemAdapter", "OnLongClick item: " + position);
                    showPopupMenu(v);
                    return false;
                }
            });
        }

        public void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.menu_context);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    Log.d("ItemAdapter", "OnMenuItemClick: Edit " + getAdapterPosition());
                    listener.onEditClick(getAdapterPosition());
                    return true;

                case R.id.action_delete:
                    Log.d("ItemAdapter", "OnMenuItemClick: Delete " + getAdapterPosition());
                    listener.onDeleteClick(getAdapterPosition());
                    return true;

                default:
                    return false;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.recycle_item_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroceryListItem mItem = mGroceryLists.get(position);

        TextView mItemView = holder.tvItem;
        mItemView.setText(mItem.getItem());

        TextView mNameView = holder.tvName;
        mNameView.setText(mItem.getPerson());

        CheckBox mCheckBox = holder.cbCurrent;
        mCheckBox.setChecked(prefs.getBoolean(mItem.getKey(), false));
    }

    @Override
    public int getItemCount() {
        return mGroceryLists.size();
    }

    public ItemAdapter(List<GroceryListItem> lists, final OnItemClickListener listener) {
        mGroceryLists = lists;
        this.listener = listener;
    }

    public void clearItems() {
        mGroceryLists.clear();
    }

}
