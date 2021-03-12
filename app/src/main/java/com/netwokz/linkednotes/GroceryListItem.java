package com.netwokz.linkednotes;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class GroceryListItem {


    private String mItem;
    private boolean isChecked;
    private String mPerson; //Person who originally added item
    private String mKey;

    public GroceryListItem() {
    }

    public GroceryListItem(String name, String item) {
        mPerson = name;
        mItem = item;
    }

    public void setItem(String item) {
        mItem = item;
    }

    public void setPerson(String person) {
        mPerson = person;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getItem() {
        return mItem;
    }

    public String getPerson() {
        return mPerson;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
