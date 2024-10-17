package com.bijana.doms.apps.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bijana.doms.apps.R;

import java.util.ArrayList;

public class Adaptor extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<GetData> model;
    public Adaptor(Context context, ArrayList<GetData> model)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model   = model;
    }
    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View view, ViewGroup viewGroup) {
        TextView date, nominal, type;
        @SuppressLint({"ViewHolder", "InflateParams"})
        View viewData = inflater.inflate(R.layout.list_data, null);

        if (viewData != null) {
            date = viewData.findViewById(R.id.dateItem);
            nominal = viewData.findViewById(R.id.nominal);
            type = viewData.findViewById(R.id.type);
            TextView icon = viewData.findViewById(R.id.tambahHijau);
            String due = "Due date : ";

            date.setText( due += model.get(position).getDate());
            nominal.setText(model.get(position).getNominal());
            type.setText(model.get(position).getType());


            // Cek jenis (type) dan atur warna teks sesuai
            String itemType = model.get(position).getType();
            if ("income".equals(itemType)) {
                nominal.setTextColor(ContextCompat.getColor(context, R.color.TextHijau));
                type.setTextColor(ContextCompat.getColor(context, R.color.TextHijau));
            } else if ("spending".equals(itemType)) {
                nominal.setTextColor(ContextCompat.getColor(context, R.color.TextMerah));
                icon.setRotation(90);
                icon.setBackgroundResource(R.drawable.panah_merah);
                type.setTextColor(ContextCompat.getColor(context, R.color.TextMerah));
            }
        }

        return viewData;
    }

}
