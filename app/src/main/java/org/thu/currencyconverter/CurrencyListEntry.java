package org.thu.currencyconverter;

import android.widget.ImageView;

public class CurrencyListEntry {
    public String name;
    public double exchangeRate;
    public ImageView img;
    public CurrencyListEntry(String name, double rate){
        this.name = name;
        this.exchangeRate = rate;
    }

    public String getName() {
        return name;
    }
}
