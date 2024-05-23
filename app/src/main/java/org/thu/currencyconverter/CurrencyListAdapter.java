
package org.thu.currencyconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.thu.currencyconverter.R;

public class CurrencyListAdapter extends BaseAdapter {
    private CurrencyListEntry[] data;
    public CurrencyListAdapter(CurrencyListEntry[] data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        // Get data
        CurrencyListEntry entry = data[position];

        if (convertView == null){


            // Create UI views from layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.currency_list_view_item, null, false);
        }

        TextView rate = convertView.findViewById(R.id.currency_item_rate_view);
        rate.setText("rate: " + String.valueOf(entry.exchangeRate));
        //rate.setText("rate: " +String.format("%.2f", entry.exchangeRate));

        TextView textView = convertView.findViewById(R.id.currency_item_text_view);
        textView.setText(entry.name);

        ImageView img = convertView.findViewById(R.id.currency_item_image_view);
        int imageId = context.getResources().getIdentifier("flag_" + entry.name.toLowerCase(), "drawable", context.getPackageName());
        img.setImageResource(imageId);
        return convertView;
    }
}

