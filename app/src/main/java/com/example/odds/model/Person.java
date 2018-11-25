package com.example.odds.model;


import com.example.odds.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

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
