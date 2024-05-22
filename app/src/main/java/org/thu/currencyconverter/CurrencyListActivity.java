package org.thu.currencyconverter;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class CurrencyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);

        // Get the currency list from the ExchangeRateDatabase
        ExchangeRateDatabase obj = new ExchangeRateDatabase();
        String[] currencyList = obj.getCurrencies();

        ArrayList<CurrencyListEntry> tempList = new ArrayList<>();
        for (String currency : currencyList) {
            tempList.add(new CurrencyListEntry(currency, obj.getExchangeRate(currency)));
        }

        CurrencyListEntry[] tempArray = new CurrencyListEntry[tempList.size()];
        tempList.toArray(tempArray);

        CurrencyListAdapter adapter = new CurrencyListAdapter(tempArray);
        ListView listView = (ListView)findViewById(R.id.currency_list_view);
        listView.setAdapter(adapter);
    }
}
