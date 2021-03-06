package com.netwokz.linkednotes;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class EditNameDialogFragment extends DialogFragment implements View.OnClickListener {

    Button yes_button, no_button;
    NameCommunicator communicator;
    TextView etName;
    String mName;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof NameCommunicator) {
            communicator = (NameCommunicator) getActivity();
        } else {
            throw new ClassCastException(activity.toString() + " must implemenet EditNameDialogFragment.communicator");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setCancelable(false);
        getDialog().setTitle("Title");

        View view = inflater.inflate(R.layout.fragment_name, null, false);

        etName = view.findViewById(R.id.textView1);
        yes_button = view.findViewById(R.id.yesbtn);
        no_button = view.findViewById(R.id.nobtn);

        // setting onclick listener for buttons
        yes_button.setOnClickListener(this);
        no_button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yesbtn:
                dismiss();
                mName = etName.getText().toString();
                communicator.messageName(mName);
                break;

            case R.id.nobtn:
                dismiss();
                communicator.messageName("Dialog No btn clicked");
                break;
        }

    }

    public interface NameCommunicator {
        void messageName(String data);
    }
}
