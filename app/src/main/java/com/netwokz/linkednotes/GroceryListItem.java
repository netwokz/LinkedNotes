package com.netwokz.linkednotes;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class GroceryListItem {
    boolean mIsCurrent;
    String mItem;
    String mPerson; //Person who originally added item

    public GroceryListItem() {
    }

    public GroceryListItem(String name, String item, Boolean isCurrent) {
        mPerson = name;
        mItem = item;
        mIsCurrent = isCurrent;
    }

    public boolean isActive() {
        return mIsCurrent;
    }

    public String getItem() {
        return mItem;
    }

    public String getPerson() {
        return mPerson;
    }
}
