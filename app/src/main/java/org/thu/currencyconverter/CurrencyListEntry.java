package org.thu.currencyconverter;

import android.widget.ImageView;

import java.util.ArrayList;

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

    public void setListEntryRate(double rate){
        this.exchangeRate = rate;
    }
}
