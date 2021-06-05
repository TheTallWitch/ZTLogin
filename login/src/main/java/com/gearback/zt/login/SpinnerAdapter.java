package com.gearback.zt.login;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gearback.methods.Methods;
import com.gearback.zt.login.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter implements android.widget.SpinnerAdapter {

    List<String> items;
    Activity context;
    boolean replace;
    Methods methods = new Methods();

    public SpinnerAdapter(Activity context, List<String> items, boolean replace) {
        this.items = items;
        this.context = context;
        this.replace = replace;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_row, parent, false);
        TextView text = row.findViewById(R.id.spinnerText);
        if (replace) {
            text.setText(methods.ReplaceNumber(items.get(position)));
        }
        else {
            text.setText(items.get(position));
        }
        return row;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_row, viewGroup, false);
        TextView text = row.findViewById(R.id.spinnerText);
        if (replace) {
            text.setText(methods.ReplaceNumber(items.get(i)));
        }
        else {
            text.setText(items.get(i));
        }
        return row;
    }
}
