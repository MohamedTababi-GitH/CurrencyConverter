package org.thu.currencyconverter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CurrencyUpdateRunnable runnable;
    private MenuItem shareItem;
    private ShareActionProvider act;

    // Instance of ExchangeRateDatabase
    ExchangeRateDatabase obj = new ExchangeRateDatabase();
    String[] currencyList = obj.getCurrencies();
    CurrencyListAdapter adapter;
    Spinner inputspinner;
    Spinner outputspinner;
    ArrayList<CurrencyListEntry> tempList = new ArrayList<>();
    TextView inputVal;
    TextView outputVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // input and output values
        inputVal = findViewById(R.id.input_val);
        outputVal = findViewById(R.id.output_val);

        // Conversion Button
        Button convertbtn = findViewById(R.id.convert_btn);

        // Spinners for input and output currency lists
        inputspinner = findViewById(R.id.input_spinner);
        outputspinner = findViewById(R.id.output_spinner);

        // Filling the array with currency list names and setting adapter
        for (String currency : currencyList) {
            CurrencyListAdapter.data.add(new CurrencyListEntry(currency, obj.getExchangeRate(currency)));
        }
        adapter = new CurrencyListAdapter(CurrencyListAdapter.data);

        // Setting adapters to spinners
        inputspinner.setAdapter(adapter);
        outputspinner.setAdapter(adapter);

        // Currency Update runnable instance
        runnable = new CurrencyUpdateRunnable(this, inputspinner, outputspinner);

        // Button onclick method convert
        convertbtn.setOnClickListener(new View.OnClickListener() {
                                          @SuppressLint("DefaultLocale")
                                          @Override
                                          public void onClick(View view) {
                                              Object inputSelectedItem = inputspinner.getSelectedItem();
                                              Object outputSelectedItem = outputspinner.getSelectedItem();

                                              CurrencyListEntry currencyEntry = (CurrencyListEntry) inputSelectedItem;
                                              CurrencyListEntry outputEntry = (CurrencyListEntry) outputSelectedItem;

                                              String inputCurrencyName = currencyEntry.getName();
                                              String outputCurrencyName = outputEntry.getName();

                                              if (inputVal != null && !inputVal.getText().toString().isEmpty()) {
                                                  double value = Double.parseDouble(inputVal.getText().toString());

                                                  // Conversion result using the convert method from Exchange Rate DB class
                                                  double result = obj.convert(value, inputCurrencyName, outputCurrencyName);

                                                  // result value is formatted to 2 decimal places
                                                  outputVal.setText(String.format("%.2f", result));

                                                  // updating the result value to be shared
                                                  setShareText("Conversion result: " + String.format("%.2f", result));
                                              }

                                          }
                                      }
        );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        shareItem = menu.findItem(R.id.action_share);
        act = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareText(null);
        return true;
    }

    // method for updating the conversion result to share
    private void setShareText(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (text != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        }
        act.setShareIntent(shareIntent);
    }

    // method to store conversion data in preferences
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String input = inputVal.getText().toString();
        editor.putString("input value", input);

        // storing the spinner selection in preferences based on selected item int position
        int inputSpinnerPos = inputspinner.getSelectedItemPosition();
        editor.putInt("input_spinner_position", inputSpinnerPos);
        int outputSpinnerPos = outputspinner.getSelectedItemPosition();
        editor.putInt("output_spinner_position", outputSpinnerPos);

        // storing the exchange rates in preferences with the currency name as its key
        for (String currency : currencyList) {
                editor.putFloat(currency, (float) obj.getExchangeRate(currency));
            }
        editor.apply();
    }

    // method to retrieve conversion data from preferences
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        String input = prefs.getString("input value", "");
        inputVal.setText(input);
        int inputSpinnerPos = prefs.getInt("input_spinner_position", 0);
        inputspinner.setSelection(inputSpinnerPos);
        int outputSpinnerPos = prefs.getInt("output_spinner_position", 0);
        outputspinner.setSelection(outputSpinnerPos);

        // Load stored exchange rates from SharedPreferences
         for (CurrencyListEntry entry : CurrencyListAdapter.data) {
            float storedRate = prefs.getFloat(entry.name, 0);
                // using the exchange rates stored in preferences
                entry.setListEntryRate(storedRate);
        }
        adapter.notifyDataSetChanged();
    }

    // Method for menu items selection
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent detailsIntent = new Intent(MainActivity.this, CurrencyListActivity.class);
        if (item.getItemId() == R.id.my_menu_currency_list) {
            startActivity(detailsIntent);
            return true;
        } else if (item.getItemId() == R.id.my_menu_refresh_rates) {
            // starting the currency update thread in the backgroung
            new Thread(runnable).start();
        }
        return super.onOptionsItemSelected(item);
    }
}

