package com.example.odds.model;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.odds.odds.BR;

public class Person extends BaseObservable {

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public boolean isChange() {
        return change;
    }
    @Bindable
    public void setChange(boolean change) {
        this.change = change;
        notifyPropertyChanged(BR.change);
    }

    private String name;

    private boolean change;

}
