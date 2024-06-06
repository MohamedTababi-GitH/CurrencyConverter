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

        // Getting an instance of the ExchangeRateDatabase
        ExchangeRateDatabase obj = new ExchangeRateDatabase();

        // defining the adapter with the static shared data and setting it the currency list view
        CurrencyListAdapter adapter = new CurrencyListAdapter(CurrencyListAdapter.data);
        ListView listView = findViewById(R.id.currency_list_view);
        adapter.notifyDataSetChanged();
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
