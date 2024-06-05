package org.thu.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


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
        // defining the adapter and setting it the currency list view
        CurrencyListAdapter adapter = new CurrencyListAdapter(tempArray);
        ListView listView = findViewById(R.id.currency_list_view);
        listView.setAdapter(adapter);

        // onClick method for list view items to open the corresponding map activity
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            CurrencyListEntry selectedEntry = (CurrencyListEntry)adapter.getItem(i);
            Intent detailsIntent = new Intent(CurrencyListActivity.this, MapActivity.class);
            // sending the capital name as an extra info to the intent
            detailsIntent.putExtra("capital", obj.getCapital(selectedEntry.name));
            startActivity(detailsIntent);
        });
    }
}
