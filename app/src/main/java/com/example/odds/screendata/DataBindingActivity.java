package com.example.odds.screendata;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.odds.screendata.databinding.ActivityDataBindingBinding;

public class DataBindingActivity extends AppCompatActivity {
    String TAG = getClass().getName();
    Person person = new Person();
    ActivityDataBindingBinding binding;
    int current = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
//        ActivityDataBindingBinding viewDataBinding = DataBindingUtil.setContentView(this,
//                R.layout.activity_data_binding);
//
         binding = DataBindingUtil.setContentView(
                this, R.layout.activity_data_binding
        );
        person.setName("wang");
        binding.setVariable(BR.vm, person);

        person.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.i(TAG, sender + "  --  " + propertyId);
//                Log.i(TAG, person.toString());
            }
        });

        binding.editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, s.toString());
                person.setName(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    public void reNamePerson(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    current += 1000;
                    person.setName(String.valueOf(current));
                    Log.i(TAG, "update name " + current);
                }

            }
        }).start();

    }
}
