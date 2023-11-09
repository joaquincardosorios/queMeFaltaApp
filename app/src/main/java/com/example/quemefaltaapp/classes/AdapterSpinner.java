package com.example.quemefaltaapp.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quemefaltaapp.R;

import java.util.List;

public class AdapterSpinner extends ArrayAdapter<String> {
    public AdapterSpinner(Context context, List<String> items){
        super(context, R.layout.spinner_item, items);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        return view;
    }

}