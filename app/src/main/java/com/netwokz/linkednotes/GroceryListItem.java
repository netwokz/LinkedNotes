package com.netwokz.linkednotes;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class GroceryListItem {
    String mIsCurrent;
    String mId;
    String mItem;
    String mPerson; //Person who originally added item

    public GroceryListItem() {
    }

    public GroceryListItem(String id, String name, String item, String isCurrent) {
        mId = id;
        mPerson = name;
        mItem = item;
        mIsCurrent = isCurrent;
    }

    public String getId() {
        return mId;
    }

    public String isActive() {
        return mIsCurrent;
    }

    public String getItem() {
        return mItem;
    }

    public String getPerson() {
        return mPerson;
    }
}
