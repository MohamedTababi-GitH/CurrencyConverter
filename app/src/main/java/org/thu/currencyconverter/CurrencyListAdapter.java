package org.thu.currencyconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrencyListAdapter extends BaseAdapter {
    // static data to pass to the adapter
    public static ArrayList<CurrencyListEntry> data = new ArrayList<>();
    public CurrencyListAdapter(ArrayList<CurrencyListEntry> data) {
        CurrencyListAdapter.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        CurrencyListEntry entry = data.get(position);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.currency_list_view_item, null, false);
        }

        TextView rate = convertView.findViewById(R.id.currency_item_rate_view);

        // exchange rates are formatted to 2 decimal places
        rate.setText("rate: " +String.format("%.2f", entry.exchangeRate));

        TextView textView = convertView.findViewById(R.id.currency_item_text_view);
        textView.setText(entry.name);

        ImageView img = convertView.findViewById(R.id.currency_item_image_view);

        // setting the country flag to the corresponding currency
        int imageId = context.getResources().getIdentifier("flag_" + entry.name.toLowerCase(), "drawable", context.getPackageName());
        img.setImageResource(imageId);

        return convertView;
    }
}

