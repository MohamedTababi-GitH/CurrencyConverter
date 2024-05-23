package org.thu.currencyconverter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private MenuItem shareItem;
    private ShareActionProvider act;
    // Get the currency list from the ExchangeRateDatabase
    ExchangeRateDatabase obj = new ExchangeRateDatabase();
    String[] currencyList = obj.getCurrencies();
    CurrencyListAdapter adapter2;
    // Spinners
    Spinner inputspinner;
    Spinner outputspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/




        // Button
        Button convertbtn = (Button) findViewById(R.id.convert_btn);


        TextView inputVal = (TextView) findViewById(R.id.input_val);
        TextView outputVal = (TextView) findViewById(R.id.output_val);

        inputspinner = (Spinner) findViewById(R.id.input_spinner);
        outputspinner = (Spinner) findViewById(R.id.output_spinner);


        ArrayList<CurrencyListEntry> tempList = new ArrayList<>();
        for (String currency : currencyList) {
            tempList.add(new CurrencyListEntry(currency, obj.getExchangeRate(currency)));
        }

        CurrencyListEntry[] tempArray = new CurrencyListEntry[tempList.size()];
        tempList.toArray(tempArray);

        adapter2 = new CurrencyListAdapter(tempArray);




        // Set adapters to spinners
        inputspinner.setAdapter(adapter2);
        outputspinner.setAdapter(adapter2);


        // Button onclick method convert
        convertbtn.setOnClickListener(new View.OnClickListener() {
                                          @SuppressLint("DefaultLocale")
                                          @Override
                                          public void onClick(View view) {
                                              Object inputselectedItem = inputspinner.getSelectedItem();
                                              Object outputselectedItem = outputspinner.getSelectedItem();

                                              CurrencyListEntry currencyEntry = (CurrencyListEntry) inputselectedItem;
                                              CurrencyListEntry outputEntry = (CurrencyListEntry) outputselectedItem;

                                              String inputCurrencyName = currencyEntry.getName();
                                              String outputCurrencyName = outputEntry.getName();
                                              if (inputVal != null && !inputVal.getText().toString().isEmpty()) {
                                                  double value = Double.parseDouble(inputVal.getText().toString());
                                                  double result = obj.convert(value, inputCurrencyName, outputCurrencyName);
                                                  outputVal.setText(String.format("%.2f", result));
                                                  setShareText("Conversion result: " + String.valueOf(result));
                                              }

                                          }
                                      }
        );

        // Specify the layout to use when the l²ist of choices appears.0
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


    }

    String type;
    private final OkHttpClient client = new OkHttpClient();
    String queryString = "https://www.floatrates.com/daily/eur.json";

    public void updateCurrencies() {
        try {
            Request request = new Request.Builder().url(queryString).build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            // B) Analyze JSON and extract information
            // ...
            JSONObject root = new JSONObject(responseBody);

            for (String currency : currencyList) {
                // Convert the currency to lowercase to match the JSON keys
                String currencyKey = currency.toLowerCase();

                // Check if the JSON object contains the key for the currency
                if (root.has(currencyKey)) {
                    JSONObject searchResults = root.getJSONObject(currencyKey);
                    double searchEntry = searchResults.getDouble("rate");
                    obj.setExchangeRate(currency, searchEntry);
                } else {
                    System.out.println("Currency " + currency + " not found in JSON.");
                }
            }

        /*for (int i = 0; i<searchResults.length(); i++) {
            JSONObject searchEntry = searchResults.getJSONObject(i);
            type = searchEntry.getString("rate");
        }*/

        } catch (IOException exception) {
            Log.e("Refresher", "Can't refresh from DB");
            exception.printStackTrace();
        } catch (JSONException exception) {
            Log.e("OmdbViewer", "Error parsing JSON.");
            exception.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        shareItem = menu.findItem(R.id.action_share);
        act = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareText(null);
        return true;
    }

    private void setShareText(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (text != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        }
        act.setShareIntent(shareIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent detailsIntent = new Intent(MainActivity.this, CurrencyListActivity.class);
        if (item.getItemId() == R.id.my_menu_currency_list) {
            Log.i("AppBarExample", "Yes, you clicked!");
            startActivity(detailsIntent);
            return true;
        } else if (item.getItemId() == R.id.my_menu_refresh_rates) {
            Log.i("AppBarExample2", "Yes, you refreshed!");
            updateCurrencies();
            adapter2.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);

    }
}

