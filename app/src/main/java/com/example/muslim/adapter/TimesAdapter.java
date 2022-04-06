package com.example.muslim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.muslim.R;

import java.util.ArrayList;

public class TimesAdapter extends ArrayAdapter {
    Context context;
    ArrayList<TimesData> data;

    public TimesAdapter(Context context, ArrayList<TimesData> data){
        super(context, R.layout.date_structure);
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null)
            v = LayoutInflater.from(context).inflate(R.layout.time_structure, null, false);

        TextView txv_name = v.findViewById(R.id.name);
        TextView txv_time = v.findViewById(R.id.time);
        txv_name.setText(data.get(i).getName());
        txv_time.setText(data.get(i).getTime());
        return v;
    }
}
