package com.netwokz.linkednotes;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class GroceryItemDialogFragment extends DialogFragment implements View.OnClickListener {

    Button ok_button, cn_button;
    ItemCommunicator itemCommunicator;
    TextView etItem;
    String mItem;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof ItemCommunicator) {
            itemCommunicator = (ItemCommunicator) getActivity();
        } else {
            throw new ClassCastException(activity.toString() + " must implemenet EditNameDialogFragment.communicator");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setCancelable(false);
        getDialog().setTitle("Title");

        View view = inflater.inflate(R.layout.fragment_add_grocery_item, null, false);

        etItem = view.findViewById(R.id.tv_item_add);
        ok_button = view.findViewById(R.id.ok_button);
        cn_button = view.findViewById(R.id.cn_button);

        // setting onclick listener for buttons
        ok_button.setOnClickListener(this);
        cn_button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_button:
                dismiss();
                mItem = etItem.getText().toString();
                itemCommunicator.messageItem(mItem);
                break;

            case R.id.cn_button:
                dismiss();
                itemCommunicator.messageItem("Dialog No btn clicked");
                break;
        }

    }

    public interface ItemCommunicator {
        void messageItem(String item);
    }
}
